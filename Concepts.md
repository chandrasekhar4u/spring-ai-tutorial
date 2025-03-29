# Concepts


## LLM Model Facts
- No knowledge other than public data used to train it
- Knowledge till the time of training, not latest 
- No memory
- No real world connection or realtime data
- Can't perform action
- Non-deterministic & Less predictable

## LLM considerations
- Cost by tokens in input & output. Optimize prompts. Restrict inputs.
- Not inherently secure. Watch what flows to LLM.

## To show
- OpenAI usage & budget in their online dashboard - https://platform.openai.com/settings/organization/usage
- OpenAI API specification - https://platform.openai.com/docs/api-reference/introduction
- Terminal logs to see the data flow.
  

> [!NOTE]
> Explain pre-steps in ReadMe. Also verify local setup.
> [Pre Steps](./ReadMe.md#tutorial_1_0_simpleprompt)

## Prompts

- **User Prompt:**
    - **Definition:** A **User Prompt** is a question or instruction given by the end user.
    - **Example:** “How can I use Spring AI in Java?”
    - The LLM processes this prompt to generate a relevant response.
    - **Use case:** Ask the model for information or specific tasks, like querying a database or generating a summary.

> [!NOTE]
> Go to tutorial 1


- **Model Spec & Chain of commands:**
    - Reference https://openai.com/index/introducing-the-model-spec/
    - Reference https://cdn.openai.com/spec/model-spec-2024-05-08.html#follow-the-chain-of-command
    - Ordering of priorities - Platform (LLM Model) > Developer (System prompt) > User (User prompt) > Tool

- **System Prompt / Developer prompt:**
    - **Definition:** A **System Prompt** is an instruction or context given to the LLM to guide how it should respond to user inputs.
    - Reference: https://platform.openai.com/docs/guides/prompt-engineering
    - **Example:** “You are an expert in Java programming.”
    - This helps set the tone, style, or specific behavior of the LLM when it processes user inputs.
    - **Use case:** Set a role, persona, or behavior for the LLM (e.g., making it more formal, casual, or technical).

- **Why Use System Prompts?**
  - System prompts help control how the LLM responds—whether it should be formal, informal, technical, or conversational.
  - They provide additional context that can improve the quality of the response.

> [!NOTE]
> Go to tutorial 1.1

# Common LLM API Configuration Fields (OpenAI, Claude, Gemini, LLaMA, etc.)

When calling an **LLM (Large Language Model) API**, you send a request with configuration fields that control the model’s response. These fields are **mostly shared** across models like OpenAI (GPT), Anthropic Claude, Google Gemini, and Meta LLaMA.

---

## Common Configuration Fields

### 1. Temperature
- Controls randomness.
- **Lower values (e.g., 0.2)** → More predictable, factual responses.
- **Higher values (e.g., 0.8)** → More creative and varied responses.

### 2. Top-P (Nucleus Sampling)
- Controls response diversity by setting a probability threshold.
- **Low values (e.g., 0.3)** → Model picks from the most likely words only.
- **High values (e.g., 0.9)** → More diverse choices, increasing creativity.
- Works similarly to **temperature** but in a different way (usually adjust only one).

### 3. Max Tokens
- Sets the limit for how long the response can be.
- **Higher values** → Longer responses (up to the model’s max limit).
- **Lower values** → Shorter, more concise answers.

### 4. Frequency Penalty
- Discourages repetition of words/phrases.
- **Higher values** → Less repetition, more varied language.
- **Lower values** → May repeat phrases more often.

### 5. Presence Penalty
- Encourages new topics by discouraging words already used.
- **Higher values** → Model is more likely to introduce new ideas.
- **Lower values** → Sticks to the main topic more.

### 6. Top-K (Used in LLaMA, Gemini, Claude, etc.)
- Limits how many top word choices the model considers at each step.
- **Top-K = 40** means the model picks the next word from the **top 40 most likely words**.
- **Lower values (e.g., 5)** → More deterministic.
- **Higher values (e.g., 100)** → More creative but riskier outputs.

### 7. Stop Sequences
- Defines words/phrases that will make the response stop early.
- Useful for controlling response length or ensuring structured output.

---

## Less Common / Model-Specific Fields

### 1. Logit Bias (*OpenAI, Claude, Gemini*)
- Allows boosting or suppressing specific words/tokens.
- Example: You can bias the model to prefer `"yes"` over `"no"`.

### 2. Repetition Penalty (*Google Gemini, LLaMA*)
- Similar to **frequency penalty** but applied differently.
- Reduces the likelihood of repeating words or phrases.
- **Higher values** → Avoids repetition.

### 3. Seed (*Claude, OpenAI, Gemini, LLaMA*)
- Fixes randomness so that the same input always gives the same response.
- Useful for reproducible results in testing environments.

### 4. Streaming (*OpenAI, Claude, Gemini*)
- If enabled, the model sends partial responses as it generates them (instead of waiting for the full response).
- Used for chat apps to display text in real-time.

### 5. Response Format (*OpenAI, Gemini*)
- Lets you control whether the response is returned as plain text or structured (like JSON).
- OpenAI supports `"response_format": "json"`, ensuring the output follows JSON rules.

### 6. Tool Calling (Function Calling) (*OpenAI, Gemini, Claude*)
- Allows the model to call external tools/functions.
- **OpenAI:** Function calling (`"tools"`)
- **Anthropic Claude:** Tool Use
- **Gemini:** Function calling
- Useful for integrating with APIs, calculators, or databases.

### 7. Safety Settings (*Google Gemini, Claude, OpenAI*)
- Allows setting strictness levels for filtering harmful or inappropriate content.
- Example: `"safety_settings": { "harassment": "high" }` in Gemini.

### 8. Context Window (varies by model)
- Not a request field but an important limitation.
- Determines how much past conversation the model remembers.
  - **Claude 3 Opus:** ~200K tokens
  - **GPT-4 Turbo:** ~128K tokens
  - **Gemini 1.5 Pro:** ~1M tokens

### 9. Temperature Decay (*LLaMA, some custom APIs*)
- Gradually lowers the temperature as the model generates output.
- Helps keep responses structured while allowing creativity at the start.

### 10. Penalty Alpha (*Anthropic Claude*)
- Adjusts how much the model explores new topics vs. staying focused.
- **Higher values** = More exploratory responses.



## Prompt Caching

### **Why Use Prompt Caching?**
- **Faster Responses**:  
  Reusing previously cached responses reduces the need to recompute answers, leading to faster response times.  
- **Reduce Redundancy**:  
  Avoids repeating the same calculations for identical or similar inputs.  
- **Cost-Efficient**:  
  Saves processing power and API usage by reusing cached results instead of recalculating them each time.

> [!TIP]
> To realize caching benefits, place static content like instructions and examples at the beginning of your prompt, and put variable content, such as user-specific information, at the end. https://platform.openai.com/docs/guides/prompt-caching#structuring-prompts

- For Spring & Langchain4j feature not yet implemented
- https://github.com/spring-projects/spring-ai/issues/1403
- https://github.com/langchain4j/langchain4j/issues/1591

## Context Memory

- **What is Context Memory?**
  - **Definition:** Context memory allows the model to remember previous interactions or inputs during a session.
  - **Use case:** Helps create a more natural flow in conversations by remembering the context from earlier prompts.
  - **Example:** If you ask the LLM, “Tell me about Java,” and then ask, “What about Spring Boot?” it will remember that the previous question was about Java.

- **Why Use Context Memory?**
  - Allows the model to maintain continuity across multiple user inputs.
  - Helps the model provide more relevant and accurate responses based on previous context.
  - Essential for building chatbots, assistants, or any multi-step workflows.


- **Key Concepts:**
  - **Contextual Memory:** The LLM can recall past interactions to keep the conversation coherent.
  - **State Management:** Manage the flow of data (user inputs, LLM responses) across multiple interactions.
  - **Session-based Memory:** Typically used in sessions where multiple inputs from the same user are processed.

- **Tips for Effective Use:**
  - Keep track of **relevant information**: Only store context that is useful for the task.
  - **Limit the context size**: LLMs may have a token limit, so manage how much past context you store and send with each prompt.
  - Consider **expiration** of context: Older context may become irrelevant over time, so decide when to discard or refresh memory.

> [!NOTE]
> Go to tutorial 2

## RAG

- **What is RAG?**
  - **Definition:** RAG (Retrieval-Augmented Generation) combines retrieving relevant information from an external source and using it to generate a response.
  - **Use case:** Instead of relying solely on the model's pre-existing knowledge, RAG fetches real-time or specific data from an organization's internal data sources (like databases, knowledge bases, or document repositories) to generate more accurate, up-to-date, and context-specific responses.

**How RAG Works:**
- **Step 1: Retrieve** relevant information from an external source using a query.
- **Step 2: Augment** the retrieved information by refining or adding context to it. This step may involve preprocessing the data, filtering irrelevant details, or summarizing large data before passing it to the generative model.
- **Step 3: Generate** a response by combining the augmented information with the user's prompt.
- This process ensures that the model provides responses based on the most relevant, up-to-date, and specific data available.

- **Why Use RAG?**
  - **Accuracy:** RAG helps improve response quality by using external data sources.
  - **Dynamic Knowledge:** It provides real-time access to new data beyond the model's training cut-off.
  - **Custom Knowledge:** Use your own documents, knowledge bases, or databases to augment LLM responses.

- **What to Watch For:**
  - **Data Privacy & Security:** Ensure that sensitive or confidential internal data is handled securely when using RAG.
  - **Relevance of Retrieved Data:** The quality of the response depends on the relevance of the data retrieved. Make sure the retrieval mechanism is well-tuned.
  - **Token Limitations:** Large data sets might exceed the model's token limit. Make sure to properly manage the amount of data you send to the model.
  - **Data Freshness:** Regularly update internal data sources to avoid relying on outdated or irrelevant information for generating responses.

> [!NOTE]
> Go to tutorial 3

## **What is LLM Moderation?**

- **Definition**:  
  **LLM Moderation** involves using Large Language Models (LLMs) to **flag** and **score** harmful, inappropriate, or offensive content in user inputs and AI-generated outputs, rather than directly filtering or blocking content.

### **Why is LLM Moderation Important?**
- **Flag Harmful Content**:  
  Flags content that may be inappropriate, offensive, or harmful, allowing further review or action to be taken.
  
- **Ensure Safe User Interaction**:  
  Provides an additional layer of protection by identifying unsafe or damaging content before it reaches the user.
  
- **Compliance with Regulations**:  
  Helps meet legal and ethical standards regarding user-generated content.

Reference: https://platform.openai.com/docs/guides/moderation

## Agentic AI

## **Workflows vs Agents in AI**

### **Workflows** (Prescriptive Systems)
- **What is it?**  
  A **workflow** is a system where **LLMs** and tools are orchestrated through **predefined code paths**. The AI follows a set sequence of steps designed by developers.
- **How it works**:  
  - The system executes specific, predefined tasks in a fixed order.
  - For example, in a **workflow**, the AI might first process the input, then call an external API to fetch data, and finally return the response, all in a defined sequence.
- **Key Benefits**:
  - **Predictable**: The steps are defined in advance, ensuring a consistent output.
  - **Reliable**: Works well for well-defined tasks that don’t change often.
  - **Ideal for Enterprises**: Provides **stability** and **maintainability**, which are crucial for business applications.

### **Agents** (Autonomous Systems)
- **What is it?**  
  An **AI agent** is a system where **LLMs** dynamically direct their own processes and decide when to use tools based on the user’s input.
- **How it works**:  
  - The **agent** operates autonomously, meaning it makes decisions about how to approach the task and which tools to use based on real-time data and context.
  - For example, the AI agent might decide in the moment to call a tool (e.g., an API or a database) based on the specific details of the user's query.
- **Key Benefits**:
  - **Flexible**: The system adapts to different situations and changes its behavior as needed.
  - **Autonomous**: Agents can make decisions without predefined steps.
  - **Ideal for Complex Problems**: Useful for tasks that are unclear or require adaptive problem-solving.

### **Main Difference**
- **Workflows** are **orchestrated through predefined code paths**, ensuring a fixed and predictable sequence of steps, which is great for well-defined tasks.
- **Agents** are more **dynamic** and **autonomous**, where the system itself decides how to approach the task and when to call tools based on the context.

### **Key Insight**
- While **agents** offer autonomy and flexibility, **workflows** are often preferred in environments where **predictability**, **reliability**, and **maintainability** are crucial, especially in enterprise applications.


### **Key Concepts in Tool Usage**:
- **Tool Definition**: You must define the tool’s capabilities (e.g., data-fetching, calculations) and input/output schemas.
- **Model-Controlled Execution**: The **LLM** decides when and how to invoke tools based on user prompts, providing dynamic and context-aware responses.
- **Tool Invocation**: The tool's input parameters are passed from the **LLM** to the **Spring Application**, which executes the tool and processes the results.
- **Response Generation**: The tool's results are sent back to the **LLM**, which integrates them into the final output provided to the user.

### **Advantages of Using Tools in Spring AI**:
- **Dynamic Interactions**: Tools enable the **LLM** to access and integrate real-world data (e.g., API calls, database queries) in real-time.
- **Modular Approach**: Spring AI allows you to build reusable and extendable tools that can be integrated into various use cases.
- **Enhanced AI Capabilities**: Tools provide additional functionality (such as data retrieval, calculations, or external service calls) to augment the LLM’s intelligence.




