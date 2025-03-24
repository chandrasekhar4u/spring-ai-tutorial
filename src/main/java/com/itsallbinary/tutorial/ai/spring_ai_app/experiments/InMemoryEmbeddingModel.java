package com.itsallbinary.tutorial.ai.spring_ai_app.experiments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryEmbeddingModel implements EmbeddingModel {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryEmbeddingModel.class);
    private static final int VECTOR_SIZE = 10; // Fixed-size embedding vector

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        logger.info("Received embedding request for {} texts", request.getInstructions().size());

        List<Embedding> embeddings = new ArrayList<>();
        for (int i = 0; i < request.getInstructions().size(); i++) {
            String text = request.getInstructions().get(i);
            logger.debug("Generating embedding for text: {}", text);

            float[] vector = generateFloatEmbedding(text);
            embeddings.add(new Embedding(vector, i));
        }

        logger.info("Generated {} embeddings successfully", embeddings.size());
        return new EmbeddingResponse(embeddings);
    }

    @Override
    public float[] embed(Document document) {
        logger.info("Generating embedding for document with content: {}", document.getText());
        float[] embedding = generateFloatEmbedding(document.getText());
        logger.info("Generated document embedding successfully");
        return embedding;
    }

    // Generates a float[] embedding for text
/*    private float[] generateFloatEmbedding(String text) {
        logger.debug("Generating random embedding vector for text: {}", text);

        float[] vector = new float[VECTOR_SIZE];
        Random rand = new Random(text.hashCode());

        for (int i = 0; i < VECTOR_SIZE; i++) {
            vector[i] = rand.nextFloat();
        }

        logger.debug("Generated vector: {}", vector);
        return vector;
    }*/

    private float[] generateFloatEmbedding(String text) {
        logger.debug("Generating deterministic embedding for text: {}", text);

        float[] vector = new float[VECTOR_SIZE];
        int hash = text.hashCode(); // Get a deterministic hash value

        for (int i = 0; i < VECTOR_SIZE; i++) {
            vector[i] = (float) Math.sin(hash + i); // Use a simple transformation to generate different values
        }

        normalizeVector(vector); // Ensure the vector has unit length for proper cosine similarity

        logger.debug("Generated vector: {}", vector);
        return vector;
    }

    // Normalize the vector to unit length (L2 normalization)
    private void normalizeVector(float[] vector) {
        float norm = 0;
        for (float v : vector) {
            norm += v * v;
        }
        norm = (float) Math.sqrt(norm);

        if (norm > 0) {
            for (int i = 0; i < vector.length; i++) {
                vector[i] /= norm; // Scale each component
            }
        }
    }

}
