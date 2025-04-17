package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import com.itsallbinary.tutorial.ai.spring_ai_app.common.MySpaceCompanyPlanRepository;
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

    /**
     * This is custom advise telling LLM how to use retrieved data.
     *
     * This is a prompt template
     * https://docs.spring.io/spring-ai/reference/api/prompt.html#_prompttemplate
     */
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
                new Document(MySpaceCompanyPlanRepository.MYSPACECOMPANY_JUPITER_PLAN_DOCUMENT,
                        Map.of("planet", "Jupiter")),
                new Document(MySpaceCompanyPlanRepository.MYSPACECOMPANY_MARS_PLAN_DOCUMENT,
                        Map.of("planet", "Mars")),
                new Document(MySpaceCompanyPlanRepository.MYSPACECOMPANY_EARTH_PLAN_DOCUMENT,
                        Map.of("planet", "Earth"))
        );
        // Store data in the vector store
        vectorStore.add(documents);

        /**
         * This advisor does following,
         *
         * Retrieval: Converts user's input into embeddings using "SAME" model,
         * then searches MongoDB Atlas for similar embeddings using vector search,
         * then gets actual string text for matched results.
         *
         * Augmentation: It appends the retrieved data to user's prompt as context.
         *
         * Generation: Then it continues the chain so that request goes to LLM for generation.
         *
         * Custom advise - Inform LLM how to use the data from context.
         */
        QuestionAnswerAdvisor questionAnswerAdvisor = new QuestionAnswerAdvisor(vectorStore,
                SearchRequest.builder().build(), CUSTOM_USER_TEXT_ADVISE);


        this.chatClient = chatClientBuilder

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
