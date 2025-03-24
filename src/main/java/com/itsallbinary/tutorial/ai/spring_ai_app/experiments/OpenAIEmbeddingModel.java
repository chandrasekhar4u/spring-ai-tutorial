/*
package com.itsallbinary.tutorial.ai.spring_ai_app.experiments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OpenAIEmbeddingModel implements EmbeddingModel {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIEmbeddingModel.class);

    private final OpenAiEmbeddingClient embeddingClient;

    public OpenAIEmbeddingModel(@Value("${spring.ai.openai.api-key}") String apiKey) {
        this.embeddingClient = new OpenAiEmbeddingClient(apiKey);
        logger.info("OpenAIEmbeddingModel initialized with API Key.");
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        logger.info("Received request to generate embeddings for {} texts", request.getTexts().size());

        List<Embedding> embeddings = new ArrayList<>();
        List<float[]> openAiEmbeddings = embeddingClient.call(request.getTexts());

        for (int i = 0; i < openAiEmbeddings.size(); i++) {
            embeddings.add(new Embedding(openAiEmbeddings.get(i), i));
        }

        logger.info("Generated {} embeddings successfully.", embeddings.size());
        return new EmbeddingResponse(embeddings);
    }

    @Override
    public float[] embed(org.springframework.ai.document.Document document) {
        logger.info("Generating embedding for document: {}", document.getContent());
        List<float[]> embeddingList = embeddingClient.call(List.of(document.getContent()));

        if (!embeddingList.isEmpty()) {
            return embeddingList.get(0);
        }

        throw new RuntimeException("Failed to generate embedding for the document.");
    }
}
*/
