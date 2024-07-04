package com.boj.guidance.controller;

import com.boj.guidance.service.CodeAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/openai")
@CrossOrigin(origins = {"*"})
public class CodeAnalysisController {

    private final CodeAnalysisService codeAnalysisService;

    @PostMapping("/analyze")
    public String analyzeCode(@RequestBody String codeContent) throws IOException {
        return codeAnalysisService.analyzeCode(codeContent);
    }
}
