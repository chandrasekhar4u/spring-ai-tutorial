package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tutorial_4_0_PromptWithContextAndAgentTool {

    private static final Logger logger = LoggerFactory.getLogger(Tutorial_4_0_PromptWithContextAndAgentTool.class);

    private final ChatClient chatClient;

    public Tutorial_4_0_PromptWithContextAndAgentTool(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);


        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                        , new SimpleLoggerAdvisor()
                )
                /**
                 * Add tool for getting current status
                 */
                .defaultTools(new PlanStatusServiceTool())
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/4")
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

    /**
     * Imitates a Tool / Agent which will call a API/Service to fetch current status.
     */
    @Slf4j
    public static class PlanStatusServiceTool {

        /**
         * Tool that act as AI Agent for getting status.
         * Description is used to explain LLM what this tool does.
         *
         * @param nameOfPlanet - Input to be provided by LLM.
         *
         * @return
         */
        @Tool(description = "Returns current status of a plan of MySpaceCompany, provided planet name as input")
        String getCurrentStatus(String nameOfPlanet) {
            logger.info("MySpaceCompany - Fetching status of plan for planet " + nameOfPlanet);
            String status = "Nothing planned for this planet = " + nameOfPlanet;
            switch (StringUtils.lowerCase(StringUtils.trim(nameOfPlanet))){
                case "jupiter":
                    status = "Implementation is currently at 20%";
                    break;
                case "mars":
                    status = "Implementation is currently at 80%";
                    break;
                case "earth":
                    status = "This is always in ongoing";
                    break;
            }
            logger.info("MySpaceCompany - status =  " + status);
            return status;
        }
    }


}
