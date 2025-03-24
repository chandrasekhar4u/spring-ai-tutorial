package com.itsallbinary.tutorial.ai.spring_ai_app.experiments;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MyController {

    //anthropicChatModel
    //openAiChatModel
//    @Autowired
//    @Qualifier("openAiChatModel")
//    private final ChatModel openAiChatModel;
    private final ChatClient chatClient;

    private final OpenAiEmbeddingModel openAiEmbeddingModel;

    public MyController(/*ChatClient.Builder chatClientBuilder,*/
            @Qualifier("openAiChatModel") ChatModel openAiChatModel,
            OpenAiEmbeddingModel openAiEmbeddingModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

        this.openAiEmbeddingModel = openAiEmbeddingModel;

//        EmbeddingClient embeddingClient = new InMemoryEmbeddingClient MockEmbeddingClient();
//
//        // Initialize SimpleVectorStore

        SimpleVectorStore vectorStore = SimpleVectorStore.builder(new InMemoryEmbeddingModel()).build();
//        SimpleVectorStore vectorStore = SimpleVectorStore.builder(openAiEmbeddingModel).build();


        // Sample data
        List<Document> documents = List.of(
                new Document("The capital of France is Paris.", Map.of("country", "France")),
                new Document("Berlin is the capital of Germany.", Map.of("country", "Germany")),
                new Document("Rome is the capital of Italy.", Map.of("country", "Italy"))
        );

        // Store data in the vector store
        vectorStore.add(documents);

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                       , new QuestionAnswerAdvisor(vectorStore)
                        , new SimpleLoggerAdvisor()
                )
//                .defaultTools(new JiraTools())
                .build();
//        this.chatClient = ChatClient.builder(myChatModel).build();

    }

    @GetMapping(CommonHelper.URL_PREFIX + "experiment")
    String generation(String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }
}