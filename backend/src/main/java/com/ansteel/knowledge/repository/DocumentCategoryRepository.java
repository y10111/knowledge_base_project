package com.ansteel.knowledge.repository;

import com.ansteel.knowledge.entity.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Integer> {

    List<DocumentCategory> findByParentId(Integer parentId);

    List<DocumentCategory> findByParentIdIsNull();

    List<DocumentCategory> findAllByOrderBySortOrderAsc();
}
