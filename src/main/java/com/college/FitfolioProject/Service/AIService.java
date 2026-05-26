package com.college.FitfolioProject.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String openRouterApiKey;

    private final String OPENROUTER_URL =
            "https://openrouter.ai/api/v1/chat/completions";

    public String extractTextFromPdf(MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            throw new RuntimeException("PDF file is empty.");
        }

        try (
                InputStream is = file.getInputStream();
                PDDocument document = PDDocument.load(is)
        ) {

            PDFTextStripper stripper =
                    new PDFTextStripper();

            return stripper.getText(document);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid PDF file."
            );
        }
    }

    public String getSuggestions(String inputData) {

        try {

            if (inputData.length() > 2000) {
                inputData =
                        inputData.substring(0, 2000);
            }

            RestTemplate restTemplate =
                    new RestTemplate();

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            headers.setBearerAuth(
                    openRouterApiKey
            );

            headers.add(
                    "HTTP-Referer",
                    "https://fitfolio-frontend.vercel.app"
            );

            headers.add(
                    "X-Title",
                    "Fitfolio"
            );

            String prompt =
                    "You are an expert IT recruiter and career counselor. " +
                            "Analyze the following resume or skills and provide:\n" +
                            "1. Suitable Job Roles\n" +
                            "2. Missing Skills\n" +
                            "3. Technologies To Learn\n" +
                            "4. Career Improvement Suggestions\n\n" +
                            "User Input:\n" + inputData;

            Map<String, Object> requestBody =
                    new HashMap<>();

            requestBody.put(
                    "model",
                    "z-ai/glm-4.5-air:free"
            );

            requestBody.put(
                    "messages",
                    new Object[] {
                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    }
            );

            ObjectMapper mapper =
                    new ObjectMapper();

            String jsonBody =
                    mapper.writeValueAsString(
                            requestBody
                    );

            HttpEntity<String> entity =
                    new HttpEntity<>(
                            jsonBody,
                            headers
                    );

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            OPENROUTER_URL,
                            entity,
                            String.class
                    );

            JsonNode root =
                    mapper.readTree(
                            response.getBody()
                    );

            return root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        } catch (Exception e) {

            e.printStackTrace();

            return "AI service is temporarily busy. Please wait a few seconds and try again.";
        }
    }
}
