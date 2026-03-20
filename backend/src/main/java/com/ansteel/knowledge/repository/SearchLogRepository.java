package com.ansteel.knowledge.repository;

import com.ansteel.knowledge.entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchLogRepository extends JpaRepository<SearchLog, Integer> {
}
