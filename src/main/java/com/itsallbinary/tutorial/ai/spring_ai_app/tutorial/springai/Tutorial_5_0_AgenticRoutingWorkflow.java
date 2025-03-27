package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_5_0_AgenticRoutingWorkflow {

    private final ChatClient chatClient;

    private final ChatClient chatClientAnthropic;

    private static final Logger logger = LoggerFactory.getLogger(Tutorial_4_0_PromptWithContextRagAndTools.class);

    public Tutorial_5_0_AgenticRoutingWorkflow(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel,
            @Qualifier("anthropicChatModel") ChatModel anthropicChatModel
            ) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

        this.chatClient = chatClientBuilder
                .build();

        ChatClient.Builder anthropicChatModelBuilder = ChatClient.builder(anthropicChatModel);

        this.chatClientAnthropic = anthropicChatModelBuilder
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/5")
    String generation(String userInput) {

        String route = this.chatClient.prompt()
                .system("if user input is about coding related then output CLAUDE else output OPENAI. " +
                        "Oly output one of these words CLAUDE or OPENAI. Don't return anything else.")
                .user(userInput)
                .call()
                .content();

        logger.info("Selected route = " + route);

        String finalResponse = "NA";

        switch (StringUtils.trim(route)){
            case "CLAUDE":
                finalResponse = this.chatClientAnthropic.prompt()
                        .user(userInput)
                        .call()
                        .content();
                logger.info("Response from Claude route = " + finalResponse);
                break;
            case "OPENAI":
                finalResponse = this.chatClient.prompt()
                        .user(userInput)
                        .call()
                        .content();
                logger.info("Response from OpenAI route = " + finalResponse);
                break;
        }

        return CommonHelper.surroundMessage(
                getClass(),
                userInput,
                finalResponse
        );
    }

}
