package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.moderation.ModerationPrompt;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.ai.openai.OpenAiModerationOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_9_0_Moderation {

    private final ChatClient chatClient;

    private OpenAiModerationModel openAiModerationModel;


    public Tutorial_9_0_Moderation(@Qualifier("openAiChatModel") ChatModel openAiChatModel,
                                   OpenAiModerationModel openAiModerationModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);

        this.chatClient = chatClientBuilder
                .build();
        this.openAiModerationModel = openAiModerationModel;
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/9")
    String generation(String userInput) {

        OpenAiModerationOptions moderationOptions = OpenAiModerationOptions.builder()
                .model("text-moderation-latest")
                .build();

        ModerationPrompt moderationPrompt = new ModerationPrompt(userInput, moderationOptions);
        ModerationResponse response = openAiModerationModel.call(moderationPrompt);

        return CommonHelper.surroundMessage(
                getClass(),
                userInput,
                response.toString());

        /*String aIResponse = this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();*/

        /*return CommonHelper.surroundMessage(
                getClass(),
                userInput,
                aIResponse
        );*/
    }
}
