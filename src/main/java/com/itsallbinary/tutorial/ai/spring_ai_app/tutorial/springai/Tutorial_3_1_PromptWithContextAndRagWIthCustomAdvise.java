package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import com.itsallbinary.tutorial.ai.spring_ai_app.experiments.LocalEmbeddingModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class Tutorial_3_1_PromptWithContextAndRagWIthCustomAdvise {

    private final ChatClient chatClient;

    private static final String CUSTOM_USER_TEXT_ADVISE = """

			Context information is below, surrounded by ---------------------

			---------------------
			{question_answer_context}
			---------------------

			Given the context and provided history information and not prior knowledge,
			reply to the user comment. If the answer is not in the context, inform
			the user that context doesn't have answer but I can answer based on general knowledge.
			Then reply to user with general knowledge using prior knowledge
			""";


    public Tutorial_3_1_PromptWithContextAndRagWIthCustomAdvise(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

        /**
         * In memory Vector database
         */
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(new LocalEmbeddingModel()).build();
        // MySpaceCompany internal knowledge
        List<Document> documents = List.of(
                new Document("MySpaceCompany is planning to send a satellite to Jupiter in 2030.", Map.of("planet", "Jupiter")),
                new Document("MySpaceCompany is planning to send a satellite to Mars in 2026.", Map.of("planet", "Mars")),
                new Document("MySpaceCompany is helps control climate change through various programs.", Map.of("planet", "Earth"))
        );
        // Store data in the vector store
        vectorStore.add(documents);

        QuestionAnswerAdvisor questionAnswerAdvisor = new QuestionAnswerAdvisor(vectorStore,
                SearchRequest.builder().build(), CUSTOM_USER_TEXT_ADVISE);


        this.chatClient = chatClientBuilder
                /*
                Add advisor with in memory chat for storing context
                 */
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                        , questionAnswerAdvisor
                        , new SimpleLoggerAdvisor()
                )
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/3.1")
    String generation(String userInput) {

        String aIResponse = this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();

        return CommonHelper.surroundMessage(
                getClass(),
                userInput,
                aIResponse
        );
    }


}
