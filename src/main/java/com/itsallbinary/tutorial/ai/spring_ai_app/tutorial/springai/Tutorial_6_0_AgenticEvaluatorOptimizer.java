package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_6_0_AgenticEvaluatorOptimizer {

    private final ChatClient chatClient;

    private final ChatClient chatClientAnthropic;

    private static final Logger logger = LoggerFactory.getLogger(Tutorial_6_0_AgenticEvaluatorOptimizer.class);

    public Tutorial_6_0_AgenticEvaluatorOptimizer(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel,
            @Qualifier("anthropicChatModel") ChatModel anthropicChatModel
            ) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

        this.chatClient = chatClientBuilder
                .build();

        ChatClient.Builder anthropicChatModelBuilder = ChatClient.builder(anthropicChatModel)/*
                Add advisor with in memory chat for storing context
                 */
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                );

        this.chatClientAnthropic = anthropicChatModelBuilder
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/6")
    String generation(String userInput) {

        String finalOutputToUi = "";

        String responsePass1 = this.chatClientAnthropic.prompt()
                .user(userInput)
                .call()
                .content();
        logger.info("responsePass1 from Claude route = " + responsePass1);

        finalOutputToUi = finalOutputToUi + CommonHelper.surroundMessageSection("PASS 1 OUTPUT", responsePass1);

        String reviewResponse = this.chatClient.prompt()
                .system("You are a reviewer. User prompt contains user's input which tells what user wants. " +
                        "It also has output generated which you should review & see if it completely answers users ask. Also check if it it the best output possible." +
                        "In response provide clear instructions about what should be improved. Only give text instructions in output.")
                .user("User asked for this ' "+ userInput + " '.  We have created answer or ouput which you can find below between ---- pattern\n"
                        + "----\n" + responsePass1 + "\n----\n")
                .call()
                .content();

        logger.info("reviewResponse = " + reviewResponse);

        finalOutputToUi = finalOutputToUi + CommonHelper.surroundMessageSection("PASS 1 REVIEW COMMENTS", reviewResponse);

        String responsePass2 = this.chatClientAnthropic.prompt()
                .system("You had already generated response for our input. We reviewed it & now we are giving instructions about how to improve the response. " +
                        "Please improve response & return updated answer.")
                .user(reviewResponse)
                .call()
                .content();
        logger.info("responsePass2 from Claude route = " + responsePass2);

        finalOutputToUi = finalOutputToUi + CommonHelper.surroundMessageSection("PASS 2 OUTPUT", responsePass2);

        return CommonHelper.surroundMessage(
                getClass(),
                userInput,
                finalOutputToUi
        );
    }

}
