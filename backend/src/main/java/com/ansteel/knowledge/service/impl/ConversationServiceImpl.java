package com.ansteel.knowledge.service.impl;

import com.ansteel.knowledge.entity.Conversation;
import com.ansteel.knowledge.entity.ConversationMessage;
import com.ansteel.knowledge.repository.ConversationMessageRepository;
import com.ansteel.knowledge.repository.ConversationRepository;
import com.ansteel.knowledge.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationMessageRepository messageRepository;

    @Autowired
    private RestTemplate restTemplate;

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
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("question", content);

        Map<String, Object> ragResponse = restTemplate.postForObject(
                RAG_SERVICE_URL, requestBody, Map.class
        );

        // 3. 保存 AI 消息
        String answer = (String) ragResponse.get("answer");
        ConversationMessage aiMessage = new ConversationMessage();
        aiMessage.setConversationId(conversationId);
        aiMessage.setRole("assistant");
        aiMessage.setContent(answer);
        messageRepository.save(aiMessage);

        // 4. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("answer", answer);
        result.put("sources", ragResponse.get("sources"));
        result.put("thought_process", ragResponse.get("thought_process"));

        return result;
    }
}
