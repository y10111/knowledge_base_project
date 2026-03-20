package com.ansteel.knowledge.controller;

import com.ansteel.knowledge.common.Result;
import com.ansteel.knowledge.entity.DocumentCategory;
import com.ansteel.knowledge.service.DocumentCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "文档分类管理")
public class DocumentCategoryController {

    @Autowired
    private DocumentCategoryService categoryService;

    @GetMapping
        @Operation(summary = "获取分类树")
    public Result<List<DocumentCategory>> list() {
        List<DocumentCategory> categories = categoryService.getCategoryTree();
        return Result.success(categories);
    }

    @GetMapping("/{id}")
        @Operation(summary = "获取分类详情")
    public Result<DocumentCategory> getById(@PathVariable Integer id) {
        DocumentCategory category = categoryService.findById(id);
        return Result.success(category);
    }

    @PostMapping
        @Operation(summary = "创建分类")
    public Result<DocumentCategory> create(@RequestBody DocumentCategory category) {
        DocumentCategory saved = categoryService.save(category);
        return Result.success(saved);
    }

    @PutMapping("/{id}")
        @Operation(summary = "更新分类")
    public Result<DocumentCategory> update(@PathVariable Integer id, @RequestBody DocumentCategory category) {
        category.setId(id);
        DocumentCategory saved = categoryService.save(category);
        return Result.success(saved);
    }

    @DeleteMapping("/{id}")
        @Operation(summary = "删除分类")
    public Result<Void> delete(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return Result.success();
    }
}
