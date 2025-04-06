
# Setup

## Completed Pre-steps (Only FYI)
- Acquired API keys for OpenAI & Claude
- Added balance to account to make API calls
- Created spring boot project with spring-ai-openai dependency
- As per the model we want to use, added specific dependencies like spring-ai-anthropic-spring-boot-starter & spring-ai-openai-spring-boot-starter
- Added API keys in properties file.
- Sprig auto discovers & create ChatModel beans for configured models.

## Setup
- IDE
    - Insall a IDE that supports Java, Gradle & Spring Boot
    - IntelliJ Community Edition (Free)
        - Windows: https://www.jetbrains.com/idea/download/?section=windows
        - macOS: https://www.jetbrains.com/idea/download/?section=mac
- Setup tutorial code
    - If you have Git installed in your machine Clone this GitHub repository
    - If you don't have Git installed, then get code using download ZIP option in GitHub
    - Go to File > New > Project from Existing Sources > select the location where tutorial code has been cloned or downloaded.
    - Select Gradle as a build tool option & then import project.
- Install JDK 17 or latest
    - Getting JDK In IntelliJ:
        - Go to File > Project Structure > Platform Settings > SDKs > Click + icon > Download JDK
        - Set Version = 17
        - Set Vendor = JetBrains Runtime (17.0.14)   ---> Version might differ, but take 17 or latest.
        - Location = Keep default
        - Download
    - Setup Project SDK
        - Go to File > Project Structure > Project
        - Set SDK = Newly downloaded JDK
    - Setup Gradle
        - Go to File > Settings > Build, Execution, Deployment > Gradle
        - Set Gradle JVM = Project SDK ---> This should show newly downloaded JDK.
- Install node.js latest (Needed for MCP tutorial) https://nodejs.org/en/download/
- Get additional files:
    - Add application-dev.properties to "src/main/resources" & make sure it has all API keys
    - Add mcp-servers-config.json to "src/main/resources" & change "command" to the newly installed nodejs directory's npx executable. npx executable is generally in same directory as npm executable.

```terminal
C:\Users\ravik>npm -v
11.2.0

C:\Users\ravik>npx -v
11.2.0
```

## Libraries
Libraries like **LangChain4j** and **Spring AI** simplify the development of AI-powered applications by providing tools to interact with LLMs (Large Language Models). They offer frameworks to integrate tasks like prompt engineering, function calling, and context management, making it easier to build complex AI workflows without having to manage low-level details.

### Key Benefits:
- **Ease of use**: Simplifies interacting with LLMs.
- **Context management**: Helps with maintaining conversation history or context.
- **Function calling**: Allows models to call external APIs and services.
- **Standardization**: Provides consistent API calls across different platforms.
- **Extensibility**: Facilitates adding custom tools and modules.

### References:
https://docs.spring.io/spring-ai/reference/index.html  
https://docs.langchain4j.dev/intro  

> [!NOTE]
> [Explain Basic concepts](./Concepts.md#basics)

-----
-----

# Tutorials

## Tutorial_1_0_SimplePrompt
> [!NOTE]
> [Explain concepts](./Concepts.md#prompts)
### Highlights
- Simple single prompt input & single output
- No memory
- LLM Request = user input

### Sequence Diagram
```mermaid
sequenceDiagram
    participant User
    participant SpringApp as Spring Application
    participant LLM

    User->>SpringApp: Send Prompt
    SpringApp->>LLM: Pass Prompt
    LLM->>User: Generate Response
```
### Test
#### Spring
[http://localhost:8080/ai/spring/tutorial/1?userInput=which planet is biggest in solar system](http://localhost:8080/ai/spring/tutorial/1?userInput=which%20planet%20is%20biggest%20in%20solar%20system)\
[http://localhost:8080/ai/spring/tutorial/1?userInput=how many moons does it have](http://localhost:8080/ai/spring/tutorial/1?userInput=how%20many%20moons%20does%20it%20have) --> Does not remeber past conversation.  
#### Langchain4j
[http://localhost:8080/ai/langchain4j/tutorial/1?userInput=which planet is biggest in solar system](http://localhost:8080/ai/langchain4j/tutorial/1?userInput=which%20planet%20is%20biggest%20in%20solar%20system)
[http://localhost:8080/ai/langchain4j/tutorial/1?userInput=how many moons does it have](http://localhost:8080/ai/langchain4j/tutorial/1?userInput=how%20many%20moons%20does%20it%20have) --> Does not remeber past conversation.  


### Try on your own
  - Try modifying code to use Anthropic Model. https://docs.spring.io/spring-ai/reference/api/chat/anthropic-chat.html#_sample_controller
  - Try different prompts

## Tutorial_1_1_SimplePromptAndSystemPrompt

### Highlights
- Add system instructions either at client level or at each prompt.
- LLM Request = user input + system prompt

### Sequence Diagram
```mermaid
sequenceDiagram
    participant User
    participant SpringApp as Spring Application
    participant LLM

    User->>SpringApp: Send Prompt
    SpringApp->>SpringApp: Add System Prompt & User Prompt
    SpringApp->>LLM: Pass Combined Prompt
    LLM->>User: Generate Response
```

### Test
[http://localhost:8080/ai/spring/tutorial/1.1?userInput=which planet is biggest in solar system](http://localhost:8080/ai/spring/tutorial/1.1?userInput=which%20planet%20is%20biggest%20in%20solar%20system)


### Try on your own
  - Change system instructions to specify format of output you want like json or yaml or anything that you wish.
  - Test chain of command. Give some restrictive system instructions & try giving conflicting prompt. For ex: Give system instructions to answer only about java but ask non java prompts. Test & make system instructions robust.
  - Langchain4j - Try adding system instructions
  
## Tutorial_1_2_SimplePromptAndSystemPromptAndConfigurations

### Highlights
- Control different configurations of LLM to control generation.
- LLM Request = user input + system prompt + configurations

### Test
[http://localhost:8080/ai/spring/tutorial/1.2?userInput=what is time&temperature=0.1](http://localhost:8080/ai/spring/tutorial/1.2?userInput=what%20is%20time&temperature=0.1)\
[http://localhost:8080/ai/spring/tutorial/1.2?userInput=what is time&temperature=0.8](http://localhost:8080/ai/spring/tutorial/1.2?userInput=what%20is%20time&temperature=0.8)\
[http://localhost:8080/ai/spring/tutorial/1.2?userInput=what is time&topP=0.9](http://localhost:8080/ai/spring/tutorial/1.2?userInput=what%20is%20time&topP=0.9)\
[http://localhost:8080/ai/spring/tutorial/1.2?userInput=what is time&topP=0.1](http://localhost:8080/ai/spring/tutorial/1.2?userInput=what%20is%20time&topP=0.1)


### Try on your own
  - Try changing values of configurations
  - Add more code for other configurations & try out how they control genration.
  

## Tutorial_2_PromptWithContext

### Highlights
- Add memory so that previous context can be retained & passed to LLM with every prompt.
- LLM Request = user input + Prior questions & answers


### Sequence Diagram
```mermaid
sequenceDiagram
    participant User
    participant SpringApp as Spring Application
    participant Memory
    participant LLM

    User->>SpringApp: Send Prompt
    SpringApp->>LLM: Pass Prompt
    LLM->>SpringApp: Generate Response
    SpringApp->>Memory: Store Prompt & Response
    
    User->>SpringApp: Send New Prompt
    SpringApp->>Memory: Retrieve Stored Memory
    SpringApp->>LLM: Combine Stored Memory + New Prompt
    LLM->>User: Generate Response
```

### Test
[http://localhost:8080/ai/spring/tutorial/2?userInput=which planet is biggest in solar system](http://localhost:8080/ai/spring/tutorial/2?userInput=which%20planet%20is%20biggest%20in%20solar%20system)\
[http://localhost:8080/ai/spring/tutorial/2?userInput=how many moons does it have](http://localhost:8080/ai/spring/tutorial/2?userInput=how%20many%20moons%20does%20it%20have)\
[http://localhost:8080/ai/spring/tutorial/2?userInput=name all moons](http://localhost:8080/ai/spring/tutorial/2?userInput=name%20all%20moons)\
[http://localhost:8080/ai/langchain4j/tutorial/2?userInput=which planet is biggest in solar system](http://localhost:8080/ai/langchain4j/tutorial/2?userInput=which%20planet%20is%20biggest%20in%20solar%20system)\
[http://localhost:8080/ai/langchain4j/tutorial/2?userInput=how many moons does it have](http://localhost:8080/ai/langchain4j/tutorial/2?userInput=how%20many%20moons%20does%20it%20have)\
[http://localhost:8080/ai/langchain4j/tutorial/2?userInput=name all moons](http://localhost:8080/ai/langchain4j/tutorial/2?userInput=name%20all%20moons)\


### Try on your own
  - Make memory separate my user (Hint: conversation id. Lookup spring documentation for chat_memory_conversation_id)
  
## Tutorial_3_0_PromptWithContextAndRag

### Highlights
- Add RAG so that it can use knowledge internal to organization.
- Similarity matched vector data will be sent with the prompt.
- LLM Request = user input + Prior questions & answers + retrieved data from vector database + default advise

### Sequence Diagram
```mermaid
sequenceDiagram
    participant SpringApp as Spring Application
    participant SimpleVectorStore as SimpleVectorStore (In-memory)
    
    SpringApp->>SpringApp: Convert Internal Knowledge to Chunks
    SpringApp->>SpringApp: Generate Embeddings for Chunks (Custom Logic)
    SpringApp->>SimpleVectorStore: Store Embeddings in SimpleVectorStore
```

```mermaid
sequenceDiagram
    participant User
    participant SpringApp as Spring Application
    participant SimpleVectorStore as SimpleVectorStore (In-memory)
    participant LLM    

    User->>SpringApp: Send Prompt
    SpringApp->>SpringApp: Convert User Prompt to Embedding (Custom Logic)
    SpringApp->>SimpleVectorStore: Retrieve Relevant Data
    SpringApp->>SpringApp: Augment Retrieved Data
    SpringApp->>LLM: Combine Data with Prompt
    LLM->>User: Generate Response
```

### Test
[http://localhost:8080/ai/spring/tutorial/3?userInput=what is the plan for jupiter planet](http://localhost:8080/ai/spring/tutorial/3?userInput=what%20is%20the%20plan%20for%20jupiter%20planet)

[http://localhost:8080/ai/langchain4j/tutorial/3?userInput=any plans for jupiter planet](http://localhost:8080/ai/langchain4j/tutorial/3?userInput=any%20plans%20for%20jupiter%20planet)\
[http://localhost:8080/ai/langchain4j/tutorial/3?userInput=which planet is biggest in solar system](http://localhost:8080/ai/langchain4j/tutorial/3?userInput=which%20planet%20is%20biggest%20in%20solar%20system)\


### Try on your own
  -
  

## Tutorial_3_1_PromptWithContextAndRagWIthCustomAdvise

### Highlights
- Custom advise about how to use RAG data
- LLM Request = user input + Prior questions & answers + retrieved data from vector database + custom advise


## Tutorial_3_2_PromptWithContextAndRagWithEmbeddingModel

### Highlights
- Use OpenAI embedding model to generate embeddings
- Try
  - Use embedding model to even generate embeddings of use prompt & then search for similarity.

### Sequence Diagram
```mermaid
sequenceDiagram
    participant SpringApp as Spring Application
    participant VectorDB as Vector Database
    participant EmbeddingModel as Embedding Model

    SpringApp->>SpringApp: Convert Internal Knowledge to Chunks
    SpringApp->>EmbeddingModel: Generate Embeddings for Chunks
    SpringApp->>VectorDB: Store Embeddings in Vector Database

```
```mermaid
sequenceDiagram
    participant User
    participant SpringApp as Spring Application
    participant LLM
    participant VectorDB as Vector Database
    participant EmbeddingModel as Embedding Model

    User->>SpringApp: Send Prompt
    SpringApp->>EmbeddingModel: Convert User Prompt to Embedding
    SpringApp->>VectorDB: Retrieve Relevant Data
    SpringApp->>SpringApp: Augment Retrieved Data
    SpringApp->>LLM: Combine Data with Prompt
    LLM->>User: Generate Response
```

### Test
[http://localhost:8080/ai/spring/tutorial/3.2?userInput=any plans for planet jupiter](http://localhost:8080/ai/spring/tutorial/3.2?userInput=any%20plans%20for%20planet%20jupiter)   


## Tutorial_4_0_PromptWithContextAndAgentTool (Agentic AI)

### Highlights
- Add 'Tools' so that actions can be performed.
- LLM can't perform action but it can decide & instruct back to execute action along with inputs for action.
- Spring generates schema for input to the tool
- CALL 1 LLM Request = user input + Prior questions & answers + retrieved data from vector database + default advise + tools, their description & input/output structure
- CALL 1 RESPONSE = Instruction to execute tool & input JSON for tool. (THIS RESPONSE IS NOT RETURNED TO USER)
- TOOL - Execute tool, get response & call LLM again
- CALL 2 LLM Request = user input + Prior questions & answers + retrieved data from vector database + default advise + Tools response + tools, their description & input/output structure
- CALL 2 RESPONSE = final response which can be give back to user

### Sequence Diagram
```mermaid
sequenceDiagram
    participant User
    participant SpringApp as Spring Application
    participant PlanStatusServiceTool as PlanStatusServiceTool (@Tool)
    participant MySpaceCompany as MySpaceCompany API
    participant LLM    
    

    User->>SpringApp: Send Prompt (Request Plan Status)
    SpringApp->>LLM: Send Request with Tool Definition (PlanStatusServiceTool)
    LLM->>SpringApp: Send Tool Name & Input Parameters (Fetch Plan Status)
    SpringApp->>PlanStatusServiceTool: Execute Tool with Input Parameters
    PlanStatusServiceTool->>MySpaceCompany: Fetch Plan Status (Satellite to Planets)
    MySpaceCompany->>PlanStatusServiceTool: Return Plan Status Data
    PlanStatusServiceTool->>SpringApp: Return Plan Status Data
    SpringApp->>LLM: Send Tool Call Result (Plan Status Data)
    LLM->>SpringApp: Generate Final Response with Tool Result
    SpringApp->>User: Provide Response with Plan Status Information
```

### Test
[http://localhost:8080/ai/spring/tutorial/4?userInput=which planet is biggest in solar system](http://localhost:8080/ai/spring/tutorial/4?userInput=which%20planet%20is%20biggest%20in%20solar%20system)\
[http://localhost:8080/ai/spring/tutorial/4?userInput=any plans for this planet](http://localhost:8080/ai/spring/tutorial/4?userInput=any%20plans%20for%20this%20planet)\


[http://localhost:8080/ai/langchain4j/tutorial/4?userInput=which planet is biggest in solar system](http://localhost:8080/ai/langchain4j/tutorial/4?userInput=which%20planet%20is%20biggest%20in%20solar%20system)\
[http://localhost:8080/ai/langchain4j/tutorial/4?userInput=any plans for this planet](http://localhost:8080/ai/langchain4j/tutorial/4?userInput=any%20plans%20for%20this%20planet)\


### Try on your own
  -

## Tutorial_4_1_AgentToolForWeatherService

### Highlights
- Add 'Tools' for weather service with little bit complex input schema

### Test
[http://localhost:8080/ai/spring/tutorial/4.1?userInput=What is the weather in san francisco](http://localhost:8080/ai/spring/tutorial/4.1?userInput=What%20is%20the%20weather%20in%20san%20francisco)\
[http://localhost:8080/ai/spring/tutorial/4.1?userInput=What is the weather in la](http://localhost:8080/ai/spring/tutorial/4.1?userInput=What%20is%20the%20weather%20in%20la)\
[http://localhost:8080/ai/spring/tutorial/4.1?userInput=What is the weather in la tomorrow](http://localhost:8080/ai/spring/tutorial/4.1?userInput=What%20is%20the%20weather%20in%20la%20tomorrow)\
[http://localhost:8080/ai/spring/tutorial/4.1?userInput=What is the weather in la day after tomorrow](http://localhost:8080/ai/spring/tutorial/4.1?userInput=What%20is%20the%20weather%20in%20la%20day%20after%20tomorrow)\
[http://localhost:8080/ai/spring/tutorial/4.1?userInput=What is the weather in la 8 days from now](http://localhost:8080/ai/spring/tutorial/4.1?userInput=What%20is%20the%20weather%20in%20la%208%20days%20from%20now)


## Tutorial_5_0_AgenticRoutingWorkflow

### Highlights
- The Routing pattern implements intelligent task distribution, enabling specialized handling for different types of input.

### Test
[http://localhost:8080/ai/spring/tutorial/5?userInput=write java code for bubble sort](http://localhost:8080/ai/spring/tutorial/5?userInput=write%20java%20code%20for%20bubble%20sort)   ----> Uses route of CLAUDE LLM    
[http://localhost:8080/ai/spring/tutorial/5?userInput=does jupiter plant have rings](http://localhost:8080/ai/spring/tutorial/5?userInput=does%20jupiter%20plant%20have%20rings)  ----> Uses route of OPENAI LLM    


## Tutorial_6_0_AgenticEvaluatorOptimizer

### Highlights
- The Evaluator-Optimizer pattern implements a dual-LLM process where one model generates responses while another provides evaluation and feedback in an iterative loop, similar to a human writer's refinement process
- Use multiple models to review & optimize inputs

### Test

## Tutorial_7_0_Observability.java

### Highlights
- Add logger to log request responses
- Inspect (or log) usage like tokens etc.

### Test

## Tutorial_8_0_CustomModel_PromptWithContext.java

### Highlights
- For private or in-house models, create custom model implementation with defined APIs
- Spring AI will provide all the functionalities like m=context memory, RAG integration tc.

### Test

## Tutorial_9_0_Moderation

### Highlights

### Test

## Tutorial_10_0_ModelContextProtocol

### Highlights
- Integrate with Brave Web Search API using MCP Server
- Brave Web Search MCP Server - https://github.com/modelcontextprotocol/servers/tree/main/src/brave-search
- Brave Web Search API key - https://api-dashboard.search.brave.com/app/keys
- Spring MCP Client - https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html

```mermaid
flowchart LR
    
    subgraph Spring_Application["Spring Application (MCP Host)"]
        Chat_Client["Spring AI (Chat Client)"] 
        MCP_Client["Spring AI (MCP Client)"]        
        MCP_Server["Brave Search (MCP Server)"]        
    end
    Chat_Client -->|Prompt| LLM
    Chat_Client -->|Tools Callback Provider| MCP_Client
    MCP_Client -->|Communicates with| MCP_Server
    MCP_Server -->|Integrates with| E["Brave Search API"]


```

### Test
[http://localhost:8080/ai/spring/tutorial/1?userInput=what is the latest version of java](http://localhost:8080/ai/spring/tutorial/1?userInput=what%20is%20the%20latest%20version%20of%20java)  --> Without MCP

[http://localhost:8080/ai/spring/tutorial/10?userInput=what is the latest version of java](http://localhost:8080/ai/spring/tutorial/10?userInput=what%20is%20the%20latest%20version%20of%20java) --> With MCP

### Try on your own
- Try to integrate any other MCP server. Reference - https://modelcontextprotocol.io/examples  https://github.com/modelcontextprotocol/servers

# References
https://docs.spring.io/spring-ai/reference/api/chatclient.html
https://spring.io/blog/2025/01/21/spring-ai-agentic-patterns
https://docs.langchain4j.dev/get-started
