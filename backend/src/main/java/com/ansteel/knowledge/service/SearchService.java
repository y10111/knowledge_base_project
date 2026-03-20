package com.ansteel.knowledge.service;

import com.ansteel.knowledge.entity.SearchLog;

import java.util.Map;

public interface SearchService {

    Map<String, Object> search(String keyword, String type);

    void saveSearchLog(SearchLog searchLog);
}
