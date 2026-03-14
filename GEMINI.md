# GEMINI.md - GoldStrike Project Context

## Project Overview
GoldStrike is a high-performance Kotlin-based gold price monitor and Model Context Protocol (MCP) server. It uses Ktor for asynchronous networking and Coroutines for efficient polling. It operates in two primary modes:
1.  **Watcher Bot:** Monitors gold prices and sends Telegram alerts when user-defined price thresholds are crossed.
2.  **MCP Server:** Provides real-time gold price data to AI agents via the Model Context Protocol.

## Main Technologies
- **Kotlin:** 2.3.0 (JVM Toolchain 17)
- **Ktor:** Client (CIO engine) for HTTP requests and content negotiation.
- **MCP SDK:** `io.modelcontextprotocol:kotlin-sdk` for the MCP server implementation.
- **Serialization:** `kotlinx-serialization-json` for API data models.
- **Networking:** `kotlinx-io-core` for MCP transport streams.
- **Deployment:** Docker & Docker Compose for multi-platform support (AMD64 & ARM64).

## Building and Running
The project uses Gradle as its build system.

### Key Commands:
- **Build Distribution:** `./gradlew installDist`
- **Run Watcher Bot:** `./gradlew run`
- **Run MCP Server:** `./gradlew run --args="--mcp"`
- **Docker Build:** `docker build -t gold-watcher-bot .`
- **Docker Run:** `docker run -d --env-file .env gold-watcher-bot`
- **Test:** `./gradlew test` (Currently no tests implemented).

## Development Conventions
- **Project Structure:** Follows standard Gradle conventions (`src/main/kotlin`).
- **Concurrency:** Uses Kotlin Coroutines for non-blocking I/O and polling loops.
- **Error Handling:** Graceful handling of network timeouts and API errors in `GoldPriceApiClient` and `TelegramBotClient`.
- **Environment Variables:** Configuration is handled via environment variables, typically loaded from a `.env` file during local execution or Docker runs.

## Configuration (Environment Variables)
| Variable | Description | Default |
| :--- | :--- | :--- |
| `TELEGRAM_BOT_TOKEN` | Your Telegram Bot token from @BotFather | (Required for Bot) |
| `TELEGRAM_CHAT_ID` | Your Telegram Chat ID for alerts | (Required for Bot) |
| `TARGET_HIGH_PRICE` | Alert if price goes above this value | 5100.0 |
| `TARGET_LOW_PRICE` | Alert if price drops below this value | 5080.0 |
| `CHECK_INTERVAL` | Polling interval in milliseconds | 60000 |

## MCP Capabilities
- **Tools:**
    - `get_gold_price`: Fetches real-time gold price in USD (XAU).
