package com.boj.guidance.service;

import java.io.IOException;

public interface CodeAnalysisService {
    String analyzeCode(String codeContent) throws IOException;
}
