package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import com.itsallbinary.tutorial.ai.spring_ai_app.experiments.InMemoryEmbeddingModel;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class Tutorial_7_0_Observability {

    private static final Logger logger = LoggerFactory.getLogger(Tutorial_4_0_PromptWithContextRagAndTools.class);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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


    public Tutorial_7_0_Observability(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

        /**
         * In memory Vector database
         */
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(new InMemoryEmbeddingModel()).build();
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
                        /*
                        Add logger to log details
                         */
                        , new SimpleLoggerAdvisor()
                )
                /*
                Add tool for getting current status
                 */
                .defaultTools(new Tutorial_4_0_PromptWithContextRagAndTools.PlanStatusServiceTool())
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX + "tutorial/7")
    String generation(String userInput) {

        String finalOutputToUi = "";

        ChatClient.ChatClientRequestSpec requestSpec = this.chatClient.prompt()
                .user(userInput);

        finalOutputToUi = finalOutputToUi + CommonHelper.surroundMessageSection("requestSpec", prettyPrint(requestSpec));

        ChatClient.CallResponseSpec responseSpec = requestSpec
                .call();

        ChatResponse chatResponse = responseSpec.chatResponse();
        finalOutputToUi = finalOutputToUi + CommonHelper.surroundMessageSection("chatResponse", prettyPrint(chatResponse));

        Usage usage = chatResponse.getMetadata().getUsage();
        finalOutputToUi = finalOutputToUi + CommonHelper.surroundMessageSection("usage", prettyPrint(usage));


        String aIResponse = responseSpec
                .content();

        finalOutputToUi = finalOutputToUi + CommonHelper.surroundMessageSection("aIResponse", aIResponse);

        return CommonHelper.surroundMessage(
                getClass(),
                userInput,
                finalOutputToUi
        );
    }

    private static String prettyPrint(Object spec) {
        try{
        return GSON.toJson(GSON.fromJson(ToStringBuilder.reflectionToString(spec, ToStringStyle.JSON_STYLE), Object.class));
        } catch(Exception e){
            e.printStackTrace();
            return ToStringBuilder.reflectionToString(spec, ToStringStyle.JSON_STYLE);
        }
    }

    /**
     * Imitates a Tool which will call a API/Service to fetch current status.
     */
    @Slf4j
    public static class PlanStatusServiceTool {

        @Tool(description = "Returns current status of a plan of MySpaceCompany, provided planet name as input")
        String getCurrentStatus(String nameOfPlanet) {
            logger.info("MySpaceCompany - Fetching status of plan for planet " + nameOfPlanet);
            String status = "Nothing planned for this planet = " + nameOfPlanet;
            switch (StringUtils.lowerCase(StringUtils.trim(nameOfPlanet))){
                case "jupiter":
                    status = "Implementation is currently at 20%";
                    break;
                case "mars":
                    status = "Implementation is currently at 80%";
                    break;
                case "earth":
                    status = "This is always in ongoing";
                    break;
            }
            logger.info("MySpaceCompany - status =  " + status);
            return status;
        }
    }


}
