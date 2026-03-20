package com.ansteel.knowledge.controller;

import com.ansteel.knowledge.common.Result;
import com.ansteel.knowledge.entity.Conversation;
import com.ansteel.knowledge.entity.ConversationMessage;
import com.ansteel.knowledge.service.ConversationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conversations")
@Tag(name = "对话管理")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping
        @Operation(summary = "获取对话列表")
    public Result<Page<Conversation>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Conversation> conversations = conversationService.findAll(page, size);
        return Result.success(conversations);
    }

    @GetMapping("/{id}")
        @Operation(summary = "获取对话详情")
    public Result<Map<String, Object>> getById(@PathVariable Integer id) {
        Conversation conversation = conversationService.findById(id);
        List<ConversationMessage> messages = conversationService.getMessages(id);
        Map<String, Object> result = new HashMap<>();
        result.put("id", conversation.getId());
        result.put("title", conversation.getTitle());
        result.put("messages", messages);
        return Result.success(result);
    }

    @PostMapping
        @Operation(summary = "创建对话")
    public Result<Conversation> create(@RequestBody(required = false) Map<String, String> body) {
        Conversation conversation = new Conversation();
        if (body != null && body.containsKey("title")) {
            conversation.setTitle(body.get("title"));
        } else {
            conversation.setTitle("新对话");
        }
        Conversation saved = conversationService.save(conversation);
        return Result.success(saved);
    }

    @DeleteMapping("/{id}")
        @Operation(summary = "删除对话")
    public Result<Void> delete(@PathVariable Integer id) {
        conversationService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/{id}/messages")
        @Operation(summary = "发送消息")
    public Result<ConversationMessage> sendMessage(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        String content = body.get("content");
        ConversationMessage message = conversationService.sendMessage(id, content);
        return Result.success(message);
    }
}
