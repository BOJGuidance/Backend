package com.boj.guidance.service.implement;

import com.boj.guidance.service.CodeAnalysisService;
import com.boj.guidance.util.OpenAIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CodeAnalysisServiceImpl implements CodeAnalysisService {

    private final OpenAIClient openAIClient;

    public String analyzeCode(String codeContent) throws IOException {
        String prompt = "Analyze the following code for time efficiency, space efficiency, and suggest improvements:\n" + codeContent;
        return openAIClient.getGPTResponse(prompt);
    }

}
