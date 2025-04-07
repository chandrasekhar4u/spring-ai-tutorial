package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_1_0_SimplePrompt {

    private final ChatClient chatClient;


    public Tutorial_1_0_SimplePrompt(@Qualifier("openAiChatModel") ChatModel openAiChatModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/1")
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
