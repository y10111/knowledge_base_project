package com.ansteel.knowledge.service.impl;

import com.ansteel.knowledge.entity.DocumentCategory;
import com.ansteel.knowledge.repository.DocumentCategoryRepository;
import com.ansteel.knowledge.service.DocumentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentCategoryServiceImpl implements DocumentCategoryService {

    @Autowired
    private DocumentCategoryRepository categoryRepository;

    @Override
    public DocumentCategory save(DocumentCategory category) {
        return categoryRepository.save(category);
    }

    @Override
    public DocumentCategory findById(Integer id) {
        Optional<DocumentCategory> category = categoryRepository.findById(id);
        return category.orElse(null);
    }

    @Override
    public List<DocumentCategory> findAll() {
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    public List<DocumentCategory> findRootCategories() {
        return categoryRepository.findByParentIdIsNull();
    }

    @Override
    public List<DocumentCategory> findByParentId(Integer parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<DocumentCategory> getCategoryTree() {
        List<DocumentCategory> allCategories = findAll();
        List<DocumentCategory> rootCategories = new ArrayList<>();
        for (DocumentCategory category : allCategories) {
            if (category.getParentId() == null || category.getParentId() == 0) {
                rootCategories.add(category);
            }
        }
        return rootCategories;
    }
}
