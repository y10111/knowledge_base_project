package com.ansteel.knowledge.service;

import com.ansteel.knowledge.entity.Conversation;
import com.ansteel.knowledge.entity.ConversationMessage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ConversationService {

    Conversation save(Conversation conversation);

    Conversation findById(Integer id);

    Page<Conversation> findAll(int page, int size);

    void deleteById(Integer id);

    List<ConversationMessage> getMessages(Integer conversationId);

    ConversationMessage sendMessage(Integer conversationId, String content);
}
