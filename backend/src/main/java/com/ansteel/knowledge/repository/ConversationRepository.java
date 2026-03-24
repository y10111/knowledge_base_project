package com.ansteel.knowledge.repository;

import com.ansteel.knowledge.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    Page<Conversation> findByUserId(String userId, Pageable pageable);

    Page<Conversation> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    List<Conversation> findByTitleContaining(String title);
}
