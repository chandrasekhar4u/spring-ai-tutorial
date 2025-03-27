package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConditionalOnProperty(name = "tutorial.type", havingValue = "langchain4j")
@ComponentScan(basePackages = "com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.langchain4j")
@PropertySource("classpath:application-langchain4j.properties")
public class LangChain4jConfig {

/*    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(5); // Keeps only last 5 messages
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        Map<String, ChatMemory> memoryStore = new ConcurrentHashMap<>();

        return memoryId -> memoryStore.computeIfAbsent(memoryId, id ->
                MessageWindowChatMemory.withMaxMessages(5) // Each session has its own memory
        );
    }*/

/*    @Value("${langchain4j.openai.api-key}") // Fetch API key from properties
    private String openAiApiKey;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey) // Inject API key
                .temperature(0.7) // Adjust creativity level
                .build();
    }*/
}