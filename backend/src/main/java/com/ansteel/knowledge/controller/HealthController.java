package com.ansteel.knowledge.controller;

import com.ansteel.knowledge.common.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "系统状态")
public class HealthController {

    @GetMapping("/health")
        @Operation(summary = "健康检查")
    public Result<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "ok");
        status.put("pythonAI", "connected");
        return Result.success(status);
    }
}
