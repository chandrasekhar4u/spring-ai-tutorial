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

/**
 * LocalEmbeddingModel is an implementation of the EmbeddingModel interface.
 * This class generates deterministic embeddings for text instructions or entire documents.
 *
 * The embeddings are generated using a simple hash-based technique where each text instruction
 * is mapped to a fixed-size vector. While this method may be quick and efficient for certain applications,
 * it does not capture semantic meaning of the text in a way that more advanced embedding models do.
 *
 * Key Methods:
 * - call(EmbeddingRequest request): Generates embeddings for a list of text instructions.
 * - embed(Document document): Generates a single embedding for a document.
 * - generateFloatEmbedding(String text): Generates a fixed-size embedding vector for the given text.
 * - normalizeVector(float[] vector): Normalizes the embedding vector to unit length.
 *
 * Note: This approach uses a deterministic method based on hash values and simple transformations.
 * While this technique is fast and simple, more advanced methods like word2vec, BERT, etc.,
 * can provide more semantically rich embeddings.
 */

@Component
public class LocalEmbeddingModel implements EmbeddingModel {
    private static final Logger logger = LoggerFactory.getLogger(LocalEmbeddingModel.class);
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
