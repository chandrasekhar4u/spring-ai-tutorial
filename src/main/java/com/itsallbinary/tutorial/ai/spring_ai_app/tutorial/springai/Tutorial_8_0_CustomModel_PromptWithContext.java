package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import com.itsallbinary.tutorial.ai.spring_ai_app.common.CustomChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_8_0_CustomModel_PromptWithContext {

    private final ChatClient chatClient;


    public Tutorial_8_0_CustomModel_PromptWithContext() {
        ChatModel customChatModel = new CustomChatModel();
        ChatClient.Builder chatClientBuilder = ChatClient.builder(customChatModel);

        this.chatClient = chatClientBuilder
                /*
                Add advisor with in memory chat for storing context
                 */
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                        , new SimpleLoggerAdvisor()
                )
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/8")
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
