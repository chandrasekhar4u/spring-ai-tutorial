package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_5_0_AgenticRoutingWorkflow {

    private final ChatClient routerClient;

    private final ChatClient chatClient;

    private final ChatClient chatClientAnthropic;

    private static final Logger logger = LoggerFactory.getLogger(Tutorial_5_0_AgenticRoutingWorkflow.class);

    public Tutorial_5_0_AgenticRoutingWorkflow(
            @Value("${tutorial.openai.api-key}") String openAiApiKey,
            @Qualifier("openAiChatModel") ChatModel openAiChatModel,
            @Qualifier("anthropicChatModel") ChatModel anthropicChatModel
            ) {

        /**
         * Create a router using cheap OpenAI model i.e. gpt-4o-mini
         */
        ChatModel routerModel = OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .apiKey(openAiApiKey)
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .temperature(0.7)
                        .build()).build();

        ChatClient.Builder routerClientBuilder = ChatClient.builder(routerModel);
        this.routerClient = routerClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();


        /**
         * Regular OpenAI model client for non-coding questions
         */
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

        /**
         * Regular Anthropic Claude model for coding related questions.
         */
        ChatClient.Builder anthropicChatModelBuilder = ChatClient.builder(anthropicChatModel);

        this.chatClientAnthropic = anthropicChatModelBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/5")
    String generation(String userInput) {

        /**
         * First invoke router to decide which LLM to use to answer user's question.
         */
        String route = this.routerClient.prompt()
                .system("if user input is about coding related then output CLAUDE else output OPENAI. " +
                        "Only output one of these words CLAUDE or OPENAI. Don't return anything else.")
                .user(userInput)
                .call()
                .content();

        logger.info("Selected route = " + route);

        String finalResponse = "NA";

        /**
         * Based on router decision route to appropriate LLM.
         */
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
