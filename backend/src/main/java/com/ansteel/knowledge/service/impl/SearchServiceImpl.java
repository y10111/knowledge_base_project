package com.ansteel.knowledge.service.impl;

import com.ansteel.knowledge.entity.Conversation;
import com.ansteel.knowledge.entity.Document;
import com.ansteel.knowledge.entity.SearchLog;
import com.ansteel.knowledge.repository.ConversationRepository;
import com.ansteel.knowledge.repository.DocumentRepository;
import com.ansteel.knowledge.repository.SearchLogRepository;
import com.ansteel.knowledge.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private SearchLogRepository searchLogRepository;

    @Override
    public Map<String, Object> search(String keyword, String type) {
        Map<String, Object> result = new HashMap<>();
        if ("doc".equals(type) || "all".equals(type)) {
            List<Document> docs = documentRepository.findByTitleContainingOrContentContaining(keyword, keyword);
            result.put("docs", docs);
        }
        if ("conversation".equals(type) || "all".equals(type)) {
            List<Conversation> conversations = conversationRepository.findByTitleContaining(keyword);
            result.put("conversations", conversations);
        }
        return result;
    }

    @Override
    public void saveSearchLog(SearchLog searchLog) {
        searchLogRepository.save(searchLog);
    }
}
