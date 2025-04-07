package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.mongodb.atlas.MongoDBAtlasVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class Tutorial_3_2_RagWithMongoDBVectorSearchAndOpenAIEmbeddingModel {

    private final ChatClient chatClient;

    private final MongoDBAtlasVectorStore mongoDBAtlasVectorStore;

    @Value("${tutorial.rag.first-time-load-data}")
    private String firstTimeLoadData;

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


    public Tutorial_3_2_RagWithMongoDBVectorSearchAndOpenAIEmbeddingModel(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel,
            OpenAiEmbeddingModel openAiEmbeddingModel,
            MongoDBAtlasVectorStore mongoDBAtlasVectorStore,
            @Value("${tutorial.rag.first-time-load-data}") String firstTimeLoadData) {

        /**
         * By default, spring uses OpenAI embedding model for MongoDB Atlas vector store.
         */
        this.mongoDBAtlasVectorStore = mongoDBAtlasVectorStore;

        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

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
         */
        QuestionAnswerAdvisor questionAnswerAdvisor = new QuestionAnswerAdvisor(mongoDBAtlasVectorStore,
                SearchRequest.builder().build(), CUSTOM_USER_TEXT_ADVISE);


        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                        , questionAnswerAdvisor
                        , new SimpleLoggerAdvisor()
                )
                .build();
    }

    /**
     * This needs to run only once. In case of tutorial it is alraedy done.
     * Running this multiple times will create duplicate data in MongoDB.
     *
     * Loading of data in vector database is ideally an offline process.
     * For tutorial purposes we have put in the same class & put it behind the flag.
     * Data has already been loaded in MongoDB so this code is for reference only.
     * @param userInput
     * @return
     */
    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/3.2/loadData")
    String firstTimeLoadData(String userInput,
                             @Value("${spring.ai.vectorstore.mongodb.collection-name}") String collectionName) {

        try {

            /**
             * Loading of data in vector database is ideally an offline process.
             * For tutorial purposes we have put in the same class & put it behind the flag.
             * Data has already been loaded in MongoDB so this code is for reference only.
             */
            if (BooleanUtils.toBoolean(firstTimeLoadData)) {

                /**
                 * Clear all previous data to avoid duplicate data.
                 */
                ((MongoTemplate)mongoDBAtlasVectorStore.getNativeClient().get()).remove(new Query(),collectionName);

                /**
                 * In Mongo DB Atlas Vector database with OpenAI embedding model
                 */
                // MySpaceCompany internal knowledge
                List<Document> documents = List.of(
                        new Document("MySpaceCompany is planning to send a satellite to Jupiter in 2030.", Map.of("planet", "Jupiter")),
                        new Document("MySpaceCompany is planning to send a satellite to Mars in 2026.", Map.of("planet", "Mars")),
                        new Document("MySpaceCompany is helps control climate change through various programs.", Map.of("planet", "Earth"))
                );
                // Store data in the vector store
                mongoDBAtlasVectorStore.add(documents);
            }
        } catch (Exception e){
            e.printStackTrace();
            return "FAILED";
        }
         return "SUCCESS";
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/3.2")
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
