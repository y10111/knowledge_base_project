package com.ansteel.knowledge.controller;

import com.ansteel.knowledge.common.Result;
import com.ansteel.knowledge.entity.Document;
import com.ansteel.knowledge.service.DocumentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docs")
@Tag(name = "文档管理")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
        @Operation(summary = "获取文档列表")
    public Result<Page<Document>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer categoryId) {
        Page<Document> documents;
        if (categoryId != null) {
            documents = documentService.findByCategoryId(categoryId, page, size);
        } else {
            documents = documentService.findAll(page, size);
        }
        return Result.success(documents);
    }

    @GetMapping("/{id}")
        @Operation(summary = "获取文档详情")
    public Result<Document> getById(@PathVariable Integer id) {
        Document document = documentService.findById(id);
        return Result.success(document);
    }

    @PostMapping
        @Operation(summary = "创建文档")
    public Result<Document> create(@RequestBody Document document) {
        Document saved = documentService.save(document);
        return Result.success(saved);
    }

    @PutMapping("/{id}")
        @Operation(summary = "更新文档")
    public Result<Document> update(@PathVariable Integer id, @RequestBody Document document) {
        document.setId(id);
        Document saved = documentService.save(document);
        return Result.success(saved);
    }

    @DeleteMapping("/{id}")
        @Operation(summary = "删除文档")
    public Result<Void> delete(@PathVariable Integer id) {
        documentService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/{id}/view")
        @Operation(summary = "增加浏览次数")
    public Result<Void> incrementViews(@PathVariable Integer id) {
        documentService.incrementViews(id);
        return Result.success();
    }

    @GetMapping("/search")
        @Operation(summary = "搜索文档")
    public Result<List<Document>> search(@RequestParam String keyword) {
        List<Document> documents = documentService.search(keyword);
        return Result.success(documents);
    }
}
