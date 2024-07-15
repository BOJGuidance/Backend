package com.boj.guidance.controller;

import com.boj.guidance.service.CodeAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/openai")
@CrossOrigin(origins = {"*"})
public class CodeAnalysisController {

    private final CodeAnalysisService codeAnalysisService;

    /**
     * 코드 분석
     *
     * @param codeContent 코드 내용
     */
    @PostMapping("/analyze")
    public String analyzeCode(@RequestBody String codeContent) throws IOException {
        return codeAnalysisService.analyzeCode(codeContent);
    }
}
