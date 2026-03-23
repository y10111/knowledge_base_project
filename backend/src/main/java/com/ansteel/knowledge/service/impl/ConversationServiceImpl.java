package com.ansteel.knowledge.service.impl;

import com.ansteel.knowledge.entity.Conversation;
import com.ansteel.knowledge.entity.ConversationMessage;
import com.ansteel.knowledge.entity.Document;
import com.ansteel.knowledge.repository.ConversationMessageRepository;
import com.ansteel.knowledge.repository.ConversationRepository;
import com.ansteel.knowledge.service.ConversationService;
import com.ansteel.knowledge.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationMessageRepository messageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DocumentService documentService;

    private static final String RAG_SERVICE_URL = "http://localhost:5001/ask";

    @Override
    public Conversation save(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation findById(Integer id) {
        Optional<Conversation> conversation = conversationRepository.findById(id);
        return conversation.orElse(null);
    }

    @Override
    public Page<Conversation> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return conversationRepository.findAllByOrderByUpdatedAtDesc(pageable);
    }

    @Override
    public void deleteById(Integer id) {
        conversationRepository.deleteById(id);
    }

    @Override
    public List<ConversationMessage> getMessages(Integer conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @Override
    public Map<String, Object> sendMessage(Integer conversationId, String content) {
        // 1. 保存用户消息
        ConversationMessage userMessage = new ConversationMessage();
        userMessage.setConversationId(conversationId);
        userMessage.setRole("user");
        userMessage.setContent(content);
        messageRepository.save(userMessage);

        // 2. 调用 Python RAG 服务
        Map<String, Object> ragResponse = new HashMap<>();
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("question", content);

            // 设置超时时间为10秒
            org.springframework.http.client.ClientHttpRequestFactory factory = 
                new org.springframework.http.client.SimpleClientHttpRequestFactory();
            ((org.springframework.http.client.SimpleClientHttpRequestFactory) factory).setConnectTimeout(100000);
            ((org.springframework.http.client.SimpleClientHttpRequestFactory) factory).setReadTimeout(100000);
            RestTemplate tempRestTemplate = new RestTemplate(factory);

            ragResponse = tempRestTemplate.postForObject(
                    RAG_SERVICE_URL, requestBody, Map.class
            );

            if (ragResponse == null) {
                ragResponse = new HashMap<>();
                ragResponse.put("answer", "AI 服务暂时不可用，请稍后再试");
                ragResponse.put("sources", new java.util.ArrayList<>());
                ragResponse.put("thought_process", "AI 服务无响应");
            }
        } catch (Exception e) {
            // 捕获所有异常，确保服务不会崩溃
            System.err.println("调用 RAG 服务失败: " + e.getMessage());
            ragResponse.put("answer", "AI 服务暂时不可用，请稍后再试");
            ragResponse.put("sources", new java.util.ArrayList<>());
            ragResponse.put("thought_process", "调用 AI 服务失败: " + e.getMessage());
        }

        // 3. 保存 AI 消息
        String answer = (String) ragResponse.get("answer");
        String thoughtProcess = (String) ragResponse.get("thought_process");
        Object sourcesObj = ragResponse.get("sources");
        String sourcesJson = null;
        if (sourcesObj != null) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                sourcesJson = mapper.writeValueAsString(sourcesObj);
            } catch (Exception e) {
                System.err.println("Failed to convert sources to JSON: " + e.getMessage());
            }
        }
        ConversationMessage aiMessage = new ConversationMessage();
        aiMessage.setConversationId(conversationId);
        aiMessage.setRole("assistant");
        aiMessage.setContent(answer);
        aiMessage.setSources(sourcesJson);
        aiMessage.setThoughtProcess(thoughtProcess);
        messageRepository.save(aiMessage);

        // 4. 处理 sources，添加文档名称
        List<Map<String, Object>> sourcesWithName = new ArrayList<>();
        if (sourcesObj instanceof List) {
            List<?> sources = (List<?>) sourcesObj;
            for (Object source : sources) {
                if (source instanceof Map) {
                    Map<?, ?> sourceMap = (Map<?, ?>) source;
                    Map<String, Object> newSource = new HashMap<>();
                    newSource.putAll(sourceMap.entrySet().stream()
                            .collect(Collectors.toMap(
                                    e -> e.getKey().toString(),
                                    Map.Entry::getValue
                            )));
                    // 获取文档名称
                    Object docIdObj = sourceMap.get("doc_id");
                    System.out.println("Processing source, docIdObj: " + docIdObj + ", type: " + (docIdObj != null ? docIdObj.getClass() : "null"));
                    if (docIdObj != null) {
                        try {
                            Integer docId = Integer.parseInt(docIdObj.toString());
                            System.out.println("Looking up document with ID: " + docId);
                            Document document = documentService.findById(docId);
                            System.out.println("Found document: " + (document != null ? document.getTitle() : "null"));
                            if (document != null) {
                                newSource.put("doc_name", document.getTitle());
                            } else {
                                System.err.println("Document not found for ID: " + docId);
                            }
                        } catch (Exception e) {
                            System.err.println("Failed to get document name: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    sourcesWithName.add(newSource);
                }
            }
        }

        // 5. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("answer", answer);
        result.put("sources", sourcesWithName);
        result.put("thought_process", ragResponse.get("thought_process"));

        return result;
    }
}
