package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
public class Tutorial_1_2_SimplePromptAndSystemPromptAndConfigurations {

    public static final String SYSTEM_PROMPT = "You are a philosopher. After every response, " +
            "add a philosophical line at the end of the response.";

    private final ChatClient chatClient;

    public Tutorial_1_2_SimplePromptAndSystemPromptAndConfigurations(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient
                .builder(openAiChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultSystem(SYSTEM_PROMPT);

        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/1.2")
    public String generation(
            @RequestParam(required = true) String userInput,
            @RequestParam(required = false) Double temperature,
            @RequestParam(required = false) Integer maxTokens,
            @RequestParam(required = false) Double topP,
            @RequestParam(required = false) Double frequencyPenalty,
            @RequestParam(required = false) Double presencePenalty) {

        // Apply default values if parameters are null
        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder();

        if (temperature != null) optionsBuilder.temperature(temperature);
        if (maxTokens != null) optionsBuilder.maxTokens(maxTokens);
        if (topP != null) optionsBuilder.topP(topP);
        if (frequencyPenalty != null) optionsBuilder.frequencyPenalty(frequencyPenalty);
        if (presencePenalty != null) optionsBuilder.presencePenalty(presencePenalty);

        String aiResponse = this.chatClient.prompt()
                .user(userInput)
                .options(optionsBuilder.build()) // Apply dynamic configurations
                .call()
                .content();

        return CommonHelper.surroundMessage(
                getClass(),
                userInput +
                        formatRequestParams(temperature, maxTokens, topP, frequencyPenalty, presencePenalty),
                aiResponse
        );
    }

    public String formatRequestParams(Double temperature, Integer maxTokens,
                                      Double topP, Double frequencyPenalty, Double presencePenalty) {
        return String.format(
                "\n\n\t[ Configurations ]\n\ttemperature=%s\n\tmaxTokens=%s" +
                        "\n\ttopP=%s\n\tfrequencyPenalty=%s\n\tpresencePenalty=%s ]\n",
                temperature,
                maxTokens,
                topP,
                frequencyPenalty,
                presencePenalty
        );
    }

}
