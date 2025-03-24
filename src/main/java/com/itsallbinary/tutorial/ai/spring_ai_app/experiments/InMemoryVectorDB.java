package com.itsallbinary.tutorial.ai.spring_ai_app.experiments;

import java.util.*;

public class InMemoryVectorDB {
    private final Map<String, double[]> vectorStore = new HashMap<>();

    // Converts string to embedding (simple hash-based encoding for tutorial purposes)
    private double[] getEmbedding(String text) {
        double[] vector = new double[10]; // Fixed-size vector
        Random rand = new Random(text.hashCode());
        for (int i = 0; i < vector.length; i++) {
            vector[i] = rand.nextDouble();
        }
        return vector;
    }

    // Stores text with its embedding
    public void store(String text) {
        vectorStore.put(text, getEmbedding(text));
    }

    // Computes cosine similarity
    private double cosineSimilarity(double[] v1, double[] v2) {
        double dotProduct = 0, norm1 = 0, norm2 = 0;
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            norm1 += Math.pow(v1[i], 2);
            norm2 += Math.pow(v2[i], 2);
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // Finds the most similar stored text
    public String search(String query) {
        double[] queryVector = getEmbedding(query);
        String bestMatch = null;
        double bestSimilarity = -1;

        for (Map.Entry<String, double[]> entry : vectorStore.entrySet()) {
            double similarity = cosineSimilarity(queryVector, entry.getValue());
            if (similarity > bestSimilarity) {
                bestSimilarity = similarity;
                bestMatch = entry.getKey();
            }
        }

        return bestMatch;
    }

    public static void main(String[] args) {
        InMemoryVectorDB db = new InMemoryVectorDB();

        // Storing some strings
        db.store("Hello World");
        db.store("Java Programming");
        db.store("Artificial Intelligence");

        // Searching for a similar text
        String query = "Machine Learning";
        System.out.println("Best match for '" + query + "': " + db.search(query));
    }
}
