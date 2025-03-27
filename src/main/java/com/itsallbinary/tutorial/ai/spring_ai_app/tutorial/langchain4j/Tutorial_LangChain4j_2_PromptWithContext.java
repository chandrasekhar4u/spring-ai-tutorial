package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.langchain4j;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import dev.langchain4j.memory.ChatMemory;
//import dev.langchain4j.memory.InMemoryChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_LangChain4j_2_PromptWithContext {

    private final ChatService chatService;

    public Tutorial_LangChain4j_2_PromptWithContext(ChatLanguageModel chatLanguageModel) {
        /**
         * ChatMemory - singleton, works for singleton use but not good for shared with multiple users
         */
//        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(5); // Stores context
        this.chatService = AiServices.builder(ChatService.class)
                .chatLanguageModel(chatLanguageModel)
//                .chatMemory(chatMemory) // Attach memory
                /**
                 * chatMemoryProvider - Good for use cases of memory per user using memory id.
                 */
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_LANGCHAIN4J + "tutorial/2")
    public String generation(@RequestParam String userInput) {
        String aiResponse = chatService.chat("session-1", userInput);

        return CommonHelper.surroundMessage(
                getClass(),
                userInput,
                aiResponse
        );
    }
}
