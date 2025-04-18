# Pre Setup Guide for AI Tutorial

In order to run this tutorial, you will need sign-up to below services in order to get ncessary access keys or authentication details.

This document provides detailed instructions for acquiring the required API keys and connection strings, and configuring them in the `application-dev.properties` file so you can run the Spring AI tutorials successfully.

---

## 📄 Template: `application-dev.properties`

Go to file named `application-dev.properties` under `src/main/resources` and paste the following keys into it. You will replace the placeholder values with real ones obtained by following the steps below.

```properties
tutorial.openai.api-key=<put-openai-api-key-here>
tutorial.anthropic.api-key=<put-anthropic-api-key-here>
tutorial.brave.search.api-key=<put-brave-search-api-key-here>
tutorial.mongodb.atlas.connection-string=<put-mongodb-atlas-connection-string-here>
```

---

## 1️⃣ OpenAI API Key (for GPT models)

🔗 **Site**: [https://platform.openai.com/signup](https://platform.openai.com/signup)

### 🔧 Steps:
1. Create or log in to your OpenAI account.
2. Navigate to: [https://platform.openai.com/api-keys](https://platform.openai.com/api-keys)
3. Click **"Create new secret key"**.
4. Copy the generated key.

### ✅ Paste it into:
```properties
tutorial.openai.api-key=<your-openai-api-key>
```

> 💡 Note: You may be required to add a billing method (credit card) depending on the model and usage.

---

## 2️⃣ Anthropic API Key (for Claude models)

🔗 **Site**: [https://console.anthropic.com](https://console.anthropic.com)

### 🔧 Steps:
1. Sign in or create an account at the above link.
2. From the dashboard, go to the **API Keys** tab.
3. Click **"Create Key"**.
4. Copy the generated API key.

### ✅ Paste it into:
```properties
tutorial.anthropic.api-key=<your-anthropic-api-key>
```

> 💡 A billing setup may be required to access Claude models.

---

## 3️⃣ Brave Search API Key (for web search tool)

🔗 **Site**: [https://account.brave.com/api-keys](https://account.brave.com/api-keys)

### 🔧 Steps:
1. Login with your Brave account or create a new one.
2. Navigate to the **API Keys** section.
3. Click **"Create API Key"**.
4. Copy the generated key.

### ✅ Paste it into:
```properties
tutorial.brave.search.api-key=<your-brave-search-api-key>
```

> 🆓 **Brave Search API is currently free** for developer and educational use. No payment method is required for basic access.

---

## 4️⃣ MongoDB Atlas Connection String (for vector store)

🔗 **Site**: [https://www.mongodb.com/cloud/atlas](https://www.mongodb.com/cloud/atlas)

### 🧰 Setup Instructions (Fresh Signup)

1. Go to [https://www.mongodb.com/cloud/atlas](https://www.mongodb.com/cloud/atlas) and **sign up** or **log in**.
2. Choose a **cloud provider** (AWS, Google Cloud, or Azure) and a region.
3. Create a **Shared Cluster (M0)** — this is a free tier.
4. After the cluster is created:
   - Click **"Database Access"** from the left menu.
   - Create a database user with username and password.
5. Go to **Network Access > IP Whitelist**, and click **"Add My Current IP"**.
6. Click **"Databases"** > **Browse Collections**.
7. Click **"Add My Own Data"**:
   - **Database Name**: `spring_ai_tutorial`
   - **Collection Name**: `space_plans` (used in this tutorial)

### 🔗 Get Connection String

1. Go to **Database Deployments** and click **"Connect"** > **"Connect your application"**.
2. Copy the connection string that looks like:

```
mongodb+srv://<username>:<password>@cluster0.mongodb.net/?retryWrites=true&w=majority
```

3. Replace `<username>` and `<password>` with your actual DB user credentials.

### ✅ Paste it into:
```properties
tutorial.mongodb.atlas.connection-string=mongodb+srv://<user>:<pass>@<cluster-url>
```

---

## ⚠️ Security Best Practices

- Never commit your `application-dev.properties` to source control (especially public repos).
- Add this file to your `.gitignore`:
  ```gitignore
  application-dev.properties
  ```
- Use environment variables or secret managers in production environments.

---

## ✅ You're Ready!

After you've populated your `application-dev.properties` file with valid keys and connection strings, you're ready to:

- Run the Spring AI application
- Access OpenAI and Anthropic models
- Use Brave Search tool integration
- Load and search documents in MongoDB Atlas using RAG pipelines

If something isn't working, double-check that:
- Your API keys are correct and active
- Your IP is whitelisted in MongoDB Atlas
- The cluster is running and your URI is correctly formatted

Happy coding! 🚀
