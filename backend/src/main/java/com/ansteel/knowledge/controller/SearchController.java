package com.ansteel.knowledge.controller;

import com.ansteel.knowledge.common.Result;
import com.ansteel.knowledge.service.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "搜索")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
        @Operation(summary = "统一搜索")
    public Result<Map<String, Object>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "all") String type) {
        Map<String, Object> result = searchService.search(q, type);
        return Result.success(result);
    }
}
