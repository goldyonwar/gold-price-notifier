# 🪙 GoldStrike: Relentless Gold Price Monitor

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Ktor](https://img.shields.io/badge/ktor-%23087CFA.svg?style=for-the-badge&logo=ktor&logoColor=white)
![Gemini Extension](https://img.shields.io/badge/Gemini_Extension-gold--strike-blue?logo=google-gemini&logoColor=white)

Stop staring at charts like a peasant. Let the machines do the heavy lifting.

**GoldStrike** is a high-performance, asynchronous Kotlin bot that relentlessly tracks the price of Gold (XAU) and strikes your Telegram DMs the exact second your target price is breached. Now also available as a **Gemini CLI Extension**!

## 🔥 Why It's Badass
- **Relentless Monitoring:** Powered by Kotlin Coroutines to track prices asynchronously without blocking the main thread.
- **Zero-Bullshit Alerts:** Only alerts you when the price crosses your aggressively defined thresholds. No spam, no false alarms.
- **Gemini CLI Extension:** Install it directly into your Gemini CLI for instant gold price queries during your workflow.
- **MCP Integration:** Acts as a **Model Context Protocol (MCP)** server, allowing AI agents to query real-time gold prices directly.
- **Bulletproof Architecture:** Engineered with Ktor CIO client, precise HTTP timeouts, and graceful JVM shutdowns. It doesn't crash; it terminates systematically.

## 🚀 Instant Deployment (Gemini CLI)

The easiest way to use GoldStrike is as a [Gemini CLI extension](https://geminicli.com/extensions/).

```bash
# 1. Install the extension
gemini extensions install <your-github-username>/gold-price-notifier

# 2. Ask Gemini for the gold price
gemini "What is the current gold price?"
```
*Requires Java 17+ installed locally.*

## ⚙️ Configuration (The Arsenal)

If you are running the **Telegram Watcher Bot**, you need to arm it with environment variables. Create a `.env` file in the root directory:

**Intel Gathering (Pre-requisites):**
- **Bot Token:** Talk to [@BotFather](https://t.me/BotFather) on Telegram to create your bot and get the token.
- **Chat ID:** Forward a message from your target chat to [@userinfobot](https://t.me/userinfobot) or use `https://api.telegram.org/bot<YOUR_TOKEN>/getUpdates` to find your precise Chat ID.

```env
# Your Telegram Bot Token from @BotFather
TELEGRAM_BOT_TOKEN=your_bot_token_here

# Your personal Telegram Chat ID
TELEGRAM_CHAT_ID=your_chat_id_here

# The Strike Zones (Alert Triggers)
TARGET_HIGH_PRICE=5100.0
TARGET_LOW_PRICE=5080.0

# Polling frequency in milliseconds (e.g., 60000 = 1 minute)
CHECK_INTERVAL=60000
```

## 🛠 Mode 1: The Watcher Bot (Standard)
This is the default mode that monitors prices and sends Telegram alerts.

### Option A: Docker (Recommended)
```bash
# 1. Build the image
docker build -t gold-watcher-bot .

# 2. Run the container
docker run -d --name my_gold_bot --env-file .env gold-watcher-bot
```

### Option B: Gradle
```bash
./gradlew run
```

---

## 🛠 Mode 2: MCP Server (AI Agent Integration)
Run GoldStrike as a standalone MCP server to expose gold price tracking to your AI tools (Claude, etc.).

```bash
# Run with the --mcp flag
./gradlew run --args="--mcp"
```

**Exposed Tools:**
- `get_gold_price`: Fetches the real-time gold price in USD (XAU).

#### MCP Client Configuration (e.g., Claude Desktop)
```json
{
  "mcpServers": {
    "gold-watcher": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/gold-price-notifier-all.jar",
        "--mcp"
      ]
    }
  }
}
```

## 🛡️ License
This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
