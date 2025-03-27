package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.langchain4j;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Tutorial_LangChain4j_4_PromptWithContextRagAndTools {

    private static final Logger logger = LoggerFactory.getLogger(Tutorial_LangChain4j_4_PromptWithContextRagAndTools.class);

    private final ChatService chatService;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public Tutorial_LangChain4j_4_PromptWithContextRagAndTools(ChatLanguageModel chatLanguageModel) {
        this.embeddingStore = new InMemoryEmbeddingStore<>();

        this.chatService = AiServices.builder(ChatService.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10)) // Stores conversation history
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore)) // RAG-based retrieval
                .tools(new PlanStatusServiceTool()) // Custom AI Tool
                .build();
    }

    @PostConstruct
    public void initializeKnowledgeBase() {
        List<Document> documents = List.of(
                Document.from("MySpaceCompany is planning to send a satellite to Jupiter in 2030."),
                Document.from("MySpaceCompany is planning to send a satellite to Mars in 2026."),
                Document.from("MySpaceCompany helps control climate change through various programs.")
        );

        //  Ingest documents into the embedding store
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_LANGCHAIN4J + "tutorial/4")
    public String generation(@RequestParam String userInput) {
        String aiResponse = chatService.chat("session-1", userInput);

        return CommonHelper.surroundMessage(
                getClass(),
                userInput,
                aiResponse
        );
    }

    /**
     * Custom AI Tool: Fetches the current status of a plan for a given planet.
     */
    public static class PlanStatusServiceTool {

        @Tool("Returns the current status of a MySpaceCompany plan for a given planet.")
        public String getCurrentStatus(String nameOfPlanet) {
            logger.info("Fetching MySpaceCompany plan status for: " + nameOfPlanet);
            String status = "No planned missions for " + nameOfPlanet;

            switch (StringUtils.lowerCase(StringUtils.trim(nameOfPlanet))) {
                case "jupiter":
                    status = "Implementation is currently at 20%";
                    break;
                case "mars":
                    status = "Implementation is currently at 80%";
                    break;
                case "earth":
                    status = "This is always ongoing";
                    break;
            }

            logger.info("MySpaceCompany - status: " + status);
            return status;
        }
    }
}
