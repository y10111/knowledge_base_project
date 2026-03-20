package com.ansteel.knowledge.repository;

import com.ansteel.knowledge.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Page<Document> findByCategoryId(Integer categoryId, Pageable pageable);

    Page<Document> findByTitleContaining(String title, Pageable pageable);

    List<Document> findByTitleContainingOrContentContaining(String title, String content);

    List<Document> findByCategoryId(Integer categoryId);
}
