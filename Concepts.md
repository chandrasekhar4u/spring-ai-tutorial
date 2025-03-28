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
- OpenAI usage & budget in their online dashboard
- OpenAI API specification
- Terminal logs to see the data flow.

## Prompts

- **User Prompt:**
    - **Definition:** A **User Prompt** is a question or instruction given by the end user.
    - **Example:** “How can I use Spring AI in Java?”
    - The LLM processes this prompt to generate a relevant response.
    - **Use case:** Ask the model for information or specific tasks, like querying a database or generating a summary.



- **System Prompt:**
    - **Definition:** A **System Prompt** is an instruction or context given to the LLM to guide how it should respond to user inputs.
    - **Example:** “You are an expert in Java programming.”
    - This helps set the tone, style, or specific behavior of the LLM when it processes user inputs.
    - **Use case:** Set a role, persona, or behavior for the LLM (e.g., making it more formal, casual, or technical).
 


- **Why Use System Prompts?**
  - System prompts help control how the LLM responds—whether it should be formal, informal, technical, or conversational.
  - They provide additional context that can improve the quality of the response.

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
 




