package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.langchain4j;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_LangChain4j_1_0_SimplePrompt {

    private final ChatLanguageModel chatLanguageModel;

    public Tutorial_LangChain4j_1_0_SimplePrompt(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_LANGCHAIN4J + "tutorial/1")
    public String generation(@RequestParam String userInput) {
        String aiResponse = chatLanguageModel.chat(userInput);

        return CommonHelper.surroundMessage(
                getClass(),
                userInput,
                aiResponse
        );
    }
}
