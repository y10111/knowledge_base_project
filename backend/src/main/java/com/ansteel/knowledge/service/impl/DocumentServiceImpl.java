package com.ansteel.knowledge.service.impl;

import com.ansteel.knowledge.entity.Document;
import com.ansteel.knowledge.repository.DocumentRepository;
import com.ansteel.knowledge.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Document findById(Integer id) {
        Optional<Document> document = documentRepository.findById(id);
        return document.orElse(null);
    }

    @Override
    public Page<Document> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return documentRepository.findAll(pageable);
    }

    @Override
    public Page<Document> findByCategoryId(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return documentRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public List<Document> search(String keyword) {
        return documentRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    @Override
    public void deleteById(Integer id) {
        documentRepository.deleteById(id);
    }

    @Override
    public void incrementViews(Integer id) {
        Document document = findById(id);
        if (document != null) {
            document.setViews(document.getViews() + 1);
            documentRepository.save(document);
        }
    }
}
