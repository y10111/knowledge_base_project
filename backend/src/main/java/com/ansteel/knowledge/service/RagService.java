package com.ansteel.knowledge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RagService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String RAG_SERVICE_URL = "http://localhost:5001/index";
    private static final String RAG_DELETE_URL = "http://localhost:5001/delete";

    public Map<String, Object> indexDocument(Integer docId, String content) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("doc_id", docId);
        requestBody.put("content", content);

        return restTemplate.postForObject(RAG_SERVICE_URL, requestBody, Map.class);
    }

    public void deleteDocument(Integer docId) {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("doc_id", docId);

        try {
            restTemplate.postForObject(RAG_DELETE_URL, requestBody, Map.class);
        } catch (Exception e) {
            System.err.println("通知 Python 删除索引失败: " + e.getMessage());
        }
    }
}
