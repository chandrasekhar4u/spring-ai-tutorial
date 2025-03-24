package com.itsallbinary.tutorial.ai.spring_ai_app.common;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class CustomChatModel implements ChatModel {

    private static final Logger LOGGER = Logger.getLogger(CustomChatModel.class.getName());

    @Override
    public ChatResponse call(Prompt prompt) {
        LOGGER.info("Received prompt: " + prompt);

        // Extract user inputs from messages
        List<String> userInputs = prompt.getInstructions().stream()
                .filter(msg -> msg instanceof UserMessage)
                .map(msg -> ((UserMessage) msg).getText())
                .collect(Collectors.toList());

        // Simulated AI response
        String responseText = "🤖 AI says: I received - " + String.join(", ", userInputs);

        LOGGER.info("Generated response: " + responseText);

        // Create Generation objects from AssistantMessage
        List<Generation> generations = List.of(new Generation(new AssistantMessage(responseText)));

        // Using the builder pattern to create a ChatResponse
        return ChatResponse.builder()
                .generations(generations)  // Pass the correct List<Generation>
                .build();
    }

    /*@Override
    public ChatResponse call(String text) {
        LOGGER.info("Received single text input: " + text);

        // Simulated response
        String responseText = "🤖 AI says: You said - " + text;

        LOGGER.info("Generated response: " + responseText);

        // Create Generation objects from AssistantMessage
        List<Generation> generations = List.of(new Generation(new AssistantMessage(responseText)));

        // Using the builder pattern to create a ChatResponse
        return ChatResponse.builder()
                .generations(generations)  // Pass the correct List<Generation>
                .build();
    }

    @Override
    public ChatResponse call(List<String> texts) {
        LOGGER.info("Received batch of texts: " + texts);

        // Simulated batch response
        List<AssistantMessage> responses = texts.stream()
                .map(text -> new AssistantMessage("🤖 AI response: " + text))
                .collect(Collectors.toList());

        LOGGER.info("Generated batch responses");

        // Create Generation objects from AssistantMessage for each response
        List<Generation> generations = responses.stream()
                .map(assistantMessage -> new Generation(assistantMessage))  // Wrap AssistantMessage in Generation
                .collect(Collectors.toList());

        // Using the builder pattern to create a ChatResponse
        return ChatResponse.builder()
                .generations(generations)  // Pass the correct List<Generation>
                .build();
    }*/
}
