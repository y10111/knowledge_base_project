package com.ansteel.knowledge.repository;

import com.ansteel.knowledge.entity.ConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Integer> {

    List<ConversationMessage> findByConversationIdOrderByCreatedAtAsc(Integer conversationId);
}
