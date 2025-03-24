package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_1_1_SimplePromptAndSystemPrompt {

    public static final String SYSTEM_PROMPT = "You are a philosopher. After every response, " +
            "add a philosophical line in the end of response.";
    private final ChatClient chatClient;


    public Tutorial_1_1_SimplePromptAndSystemPrompt(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient
                .builder(openAiChatModel)
                /**
                 * Add system prompt to guide AI behavior
                 */
                .defaultSystem(SYSTEM_PROMPT);

        this.chatClient = chatClientBuilder
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX + "tutorial/1.1")
    String generation(String userInput) {

        String aIResponse = this.chatClient.prompt()
                /**
                 * Add system prompt to guide AI behavior
                 */
//                .system(SYSTEM_PROMPT)
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
