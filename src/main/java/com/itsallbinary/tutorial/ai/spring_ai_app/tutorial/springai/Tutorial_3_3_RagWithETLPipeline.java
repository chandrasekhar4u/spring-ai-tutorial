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
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.mongodb.atlas.MongoDBAtlasVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper.getDocuments;
import static org.springframework.ai.transformer.SummaryMetadataEnricher.SummaryType;

@RestController
public class Tutorial_3_3_RagWithETLPipeline {

    public static final String COLLECTION_NAME = "space_plan_pdfs";
    private final ChatClient chatClient;
    private final ChatModel openAiChatModel;
    private final MongoDBAtlasVectorStore mongoDBAtlasVectorStore;

    @Value("${tutorial.rag.allow-load-data-to-vector-db}")
    private String allowLoadDataToVectorDb;

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

    public Tutorial_3_3_RagWithETLPipeline(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel,
            MongoTemplate mongoTemplate,
            OpenAiEmbeddingModel openAiEmbeddingModel,
            @Qualifier("mongoDBAtlasVectorStoreForPdfs") MongoDBAtlasVectorStore mongoDBAtlasVectorStore) {

        /**
         * This is a different MongoDB vector with different collection.
         */
        this.mongoDBAtlasVectorStore = mongoDBAtlasVectorStore;

        // Chat Client setup
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

        // QuestionAnswerAdvisor setup using vector store
        QuestionAnswerAdvisor questionAnswerAdvisor = new QuestionAnswerAdvisor(
                mongoDBAtlasVectorStore,
                SearchRequest.builder().build(),
                CUSTOM_USER_TEXT_ADVISE);

        // Setup the chat client with advisors for memory, logging, and question answering
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory()),
                        questionAnswerAdvisor,
                        new SimpleLoggerAdvisor()
                )
                .build();
        this.openAiChatModel = openAiChatModel;
    }

    /**
     * ETL Pipeline - Extracting, Transforming, and Loading data to MongoDB Vector Store.
     * This process will read PDF documents, extract their content, split into tokens,
     * enrich with metadata, and store it in the vector store.
     */
    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/3.3/loadData")
    public String loadEtlPipelineData() {

        try {

            /**
             * Loading of data in vector database is ideally an offline process.
             * For tutorial purposes we have put in the same class & put it behind the flag.
             * Data has already been loaded in MongoDB so this code is for reference only.
             */
            if (BooleanUtils.toBoolean(allowLoadDataToVectorDb)) {

                /**
                 * Clear all previous data to avoid duplicate data.
                 */
                ((MongoTemplate) mongoDBAtlasVectorStore.getNativeClient().get()).remove(new Query(), COLLECTION_NAME);


                List<Document> documents = new ArrayList<>();
                documents.addAll(getDocuments("classpath:/MySpaceCompany_Jupiter_Plan.pdf"));
                documents.addAll(getDocuments("classpath:/MySpaceCompany_Mars_Plan.pdf"));
                documents.addAll(getDocuments("classpath:/MySpaceCompany_Earth_Plan.pdf"));


                /**
                 * TokenTextSplitter Configuration
                 *
                 * Splits large text documents into token-based chunks, ideal for use in
                 * RAG (Retrieval-Augmented Generation) pipelines.
                 *
                 * Parameters:
                 *
                 * 100   → defaultChunkSize:
                 *         Target number of tokens per chunk.
                 *         Ensures each chunk is semantically meaningful and fits within LLM token limits.
                 *
                 * 40    → minChunkSizeChars:
                 *         Minimum number of characters per chunk.
                 *         Prevents overly short or meaningless fragments from being created.
                 *
                 * 5     → minChunkLengthToEmbed:
                 *         Chunks with fewer than 5 characters will be discarded and not embedded.
                 *         Filters out low-value content.
                 *
                 * 5000  → maxNumChunks:
                 *         Maximum number of chunks allowed per document.
                 *         Protects against runaway splitting of very large texts.
                 *
                 * true  → keepSeparator:
                 *         Preserves formatting separators such as newline characters (`\n`) between chunks.
                 *         Helps retain logical paragraph and sentence boundaries.
                 */
                TokenTextSplitter textSplitter = new TokenTextSplitter(100, 40, 5, 5000, true);
                List<Document> tokenizedDocuments = textSplitter.apply(documents);

                // Step 4: Enrich documents with metadata
                KeywordMetadataEnricher keywordEnricher = new KeywordMetadataEnricher(openAiChatModel, 5);
                SummaryMetadataEnricher summaryEnricher = new SummaryMetadataEnricher(openAiChatModel,
                        List.of(SummaryType.PREVIOUS, SummaryType.CURRENT, SummaryType.NEXT));

                List<Document> keywordEnrichedDocs = keywordEnricher.apply(tokenizedDocuments);
                List<Document> summaryEnrichedDocs = summaryEnricher.apply(keywordEnrichedDocs);

                // Step 5: Load documents into MongoDB Vector Store
                mongoDBAtlasVectorStore.add(summaryEnrichedDocs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ETL Pipeline Failed";
        }

        return "ETL Pipeline Completed Successfully";
    }



    /**
     * Method to handle user input, perform RAG, and generate a response.
     */
    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/3.3")
    public String generation(String userInput) {

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
