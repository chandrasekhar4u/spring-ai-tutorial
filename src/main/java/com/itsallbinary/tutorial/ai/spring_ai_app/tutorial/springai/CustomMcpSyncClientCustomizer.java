package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CustomMcpSyncClientCustomizer implements McpSyncClientCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(CustomMcpSyncClientCustomizer.class);

    @Override
    public void customize(String serverConfigurationName, McpClient.SyncSpec spec) {

        /*// Customize the request configuration
        spec.requestTimeout(Duration.ofSeconds(30));

        // Sets the root URIs that the server connecto this client can access.
        spec.roots(roots);

        // Sets a custom sampling handler for processing message creation requests.
        spec.sampling((CreateMessageRequest messageRequest) -> {
            // Handle sampling
            CreateMessageResult result = ...
            return result;
        });

        // Adds a consumer to be notified when the available tools change, such as tools
        // being added or removed.
        spec.toolsChangeConsumer((List<McpSchema.Tool> tools) -> {
            // Handle tools change
        });

        // Adds a consumer to be notified when the available resources change, such as resources
        // being added or removed.
        spec.resourcesChangeConsumer((List<McpSchema.Resource> resources) -> {
            // Handle resources change
        });

        // Adds a consumer to be notified when the available prompts change, such as prompts
        // being added or removed.
        spec.promptsChangeConsumer((List<McpSchema.Prompt> prompts) -> {
            // Handle prompts change
        });*/

        // Adds a consumer to be notified when logging messages are received from the server.
        spec.loggingConsumer((McpSchema.LoggingMessageNotification log) -> {
            logger.info("MCP Server Log = " + log.data());
        });
    }
}
