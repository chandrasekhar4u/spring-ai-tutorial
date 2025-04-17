package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.config;

import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.mongodb.atlas.MongoDBAtlasVectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConditionalOnProperty(name = "tutorial.type", havingValue = "springai")
@ComponentScan(basePackages = "com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai")
@PropertySource("classpath:application-springai.properties")
public class SpringAiConfig {

    public static final String COLLECTION_NAME = "space_plan_pdfs";

    @Bean("mongoDBAtlasVectorStoreForPdfs")
    public MongoDBAtlasVectorStore mongoDBAtlasVectorStoreForPdfs(MongoTemplate mongoTemplate,
                                     OpenAiEmbeddingModel openAiEmbeddingModel){
        return MongoDBAtlasVectorStore.
                builder(mongoTemplate, openAiEmbeddingModel)
                .collectionName(COLLECTION_NAME)           // Optional: defaults to "vector_store"
                .vectorIndexName("space_plan_pdfs_vector_index")          // Optional: defaults to "vector_index"
                .pathName("embedding")                    // Optional: defaults to "embedding"
                .initializeSchema(true)                        // Optional: defaults to false
                .build();
    }

}
