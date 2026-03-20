package com.ansteel.knowledge.service;

import com.ansteel.knowledge.entity.Document;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DocumentService {

    Document save(Document document);

    Document findById(Integer id);

    Page<Document> findAll(int page, int size);

    Page<Document> findByCategoryId(Integer categoryId, int page, int size);

    List<Document> search(String keyword);

    void deleteById(Integer id);

    void incrementViews(Integer id);
}
