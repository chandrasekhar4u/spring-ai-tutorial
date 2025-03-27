package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.langchain4j;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ChatService {

    @SystemMessage("You are an AI assistant. Keep track of conversation history.")
    String chat(@MemoryId String sessionId, @UserMessage String message);
}
