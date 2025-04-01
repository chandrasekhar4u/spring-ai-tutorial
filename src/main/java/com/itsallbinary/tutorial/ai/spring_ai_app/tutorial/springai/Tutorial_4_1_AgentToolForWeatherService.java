package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Tutorial_4_1_AgentToolForWeatherService {

    private static final Logger logger = LoggerFactory.getLogger(Tutorial_4_1_AgentToolForWeatherService.class);

    private final ChatClient chatClient;


    public Tutorial_4_1_AgentToolForWeatherService(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);


        this.chatClient = chatClientBuilder
                /*
                Add advisor with in memory chat for storing context
                 */
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                        /*
                        Add logger to log details
                         */
                        , new SimpleLoggerAdvisor()
                )
                /*
                Add tool for getting current weather
                 */
                .defaultTools(new WeatherServiceTool())
                .build();
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/4.1")
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
     * Imitates a Tool which will call a API/Service to fetch current status.
     */
    @Slf4j
    public static class WeatherServiceTool {

//        private static final String WEATHER_API_URL = "https://api.weather.gov/gridpoints/MTR/88,126/forecast";

        private static final String WEATHER_API_URL = "https://api.weather.gov/gridpoints/";


        private final RestTemplate restTemplate;

        public WeatherServiceTool() {
            this.restTemplate = new RestTemplate();
        }

        @Tool(description = "Returns weather forecast for today or following week days. " )
        public String getWeather(@ToolParam(required = true, description = "Name of city " +
                "from given enum which is closest to the name of the city user has given. ")
                                         ForecastOfficeByCityName closestCityName
                ,@ToolParam(required = true, description = "number of days from today. " +
                "Today is 0, tomorrow is 1 & so on till max number 6.")
                int numberOfDaysFromToday) {
            try {

                logger.info("Fetching weather for " + closestCityName +
                        " numberOfDaysFromToday = " + numberOfDaysFromToday);

                String finalUrl = WEATHER_API_URL + closestCityName.getGridId() + "/"
                        + closestCityName.getGridX() + "," + closestCityName.getGridY()
                        + "/forecast";

                // Call the Weather API
                String response = restTemplate.getForObject(finalUrl, String.class);

                logger.info("Weather response " + response);

                // Parse JSON using Gson
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                JsonObject firstPeriod = jsonResponse.getAsJsonObject("properties")
                        .getAsJsonArray("periods")
                        .get(numberOfDaysFromToday)
                        .getAsJsonObject();

                // Extract relevant details
                String forecast = firstPeriod.get("detailedForecast").getAsString();
                String temperature = firstPeriod.get("temperature").getAsString();
                String unit = firstPeriod.get("temperatureUnit").getAsString();

                // Prepare output
                //String result = String.format("San Francisco Weather: %s°C, %s", temperature, forecast);
                String result = String.format(closestCityName.name() + " Weather: %s°C, %s", temperature, forecast);
                logger.info(result);

                return result;
            } catch (Exception e) {
                logger.error("Error fetching weather data: " + e.getMessage());
                return "Failed to retrieve weather data.";
            }
        }

        public enum ForecastOfficeByCityName {
            SAN_FRANCISCO("MTR", 85, 105),
            LOS_ANGELES("LOX", 152, 44),
            NEW_YORK("OKX", 33, 35),
            CHICAGO("LOT", 73, 72),
            DALLAS("FWD", 90, 90),
            SEATTLE("SEW", 123, 67),
            MIAMI("MFL", 110, 56),
            DENVER("BOU", 62, 60),
            ATLANTA("FFC", 57, 77),
            WASHINGTON_DC("LWX", 96, 70);

            private final String gridId;
            private final int gridX;
            private final int gridY;

            ForecastOfficeByCityName(String gridId, int gridX, int gridY) {
                this.gridId = gridId;
                this.gridX = gridX;
                this.gridY = gridY;
            }

            public String getGridId() {
                return gridId;
            }

            public int getGridX() {
                return gridX;
            }

            public int getGridY() {
                return gridY;
            }
        }



    }


}
