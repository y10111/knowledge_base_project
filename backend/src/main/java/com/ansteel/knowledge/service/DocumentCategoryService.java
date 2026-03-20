package com.ansteel.knowledge.service;

import com.ansteel.knowledge.entity.DocumentCategory;

import java.util.List;

public interface DocumentCategoryService {

    DocumentCategory save(DocumentCategory category);

    DocumentCategory findById(Integer id);

    List<DocumentCategory> findAll();

    List<DocumentCategory> findRootCategories();

    List<DocumentCategory> findByParentId(Integer parentId);

    void deleteById(Integer id);

    List<DocumentCategory> getCategoryTree();
}
