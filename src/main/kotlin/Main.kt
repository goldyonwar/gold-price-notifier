package com.goldyonwar

import com.goldyonwar.network.GoldPriceApiClient
import com.goldyonwar.network.TelegramBotClient
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.serialization.kotlinx.json.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import io.modelcontextprotocol.kotlin.sdk.types.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.coroutines.awaitCancellation
import kotlinx.io.asSource
import kotlinx.io.asSink
import kotlinx.io.buffered
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("GoldWatcher")

fun main(args: Array<String>) = runBlocking {
    if (args.contains("--mcp")) {
        runMcpServer()
    } else {
        runWatcherBot()
    }
}

private fun createHttpClient(): HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = false
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10_000
        connectTimeoutMillis = 5_000
        socketTimeoutMillis = 10_000
    }
}

suspend fun runMcpServer() {
    createHttpClient().use { client ->
        val apiClient = GoldPriceApiClient(client)

        val server = Server(
            serverInfo = Implementation(name = "GoldWatcherMCP", version = "1.0.0"),
            options = ServerOptions(
                capabilities = ServerCapabilities(
                    tools = ServerCapabilities.Tools(listChanged = false)
                )
            )
        )

        server.addTool(
            name = "get_gold_price",
            description = "Get the real-time gold price in USD (XAU).",
            inputSchema = ToolSchema()
        ) { _ ->
            val goldData = apiClient.fetchGoldPrice()
            val resultText = goldData?.let { gData->
                "The current gold price is $${gData.price} per ounce (Updated: ${gData.updatedAt})."
            } ?: "Sorry, unable to fetch the gold price at the moment. Please try again later."

            CallToolResult(
                content = listOf(TextContent(text = resultText))
            )
        }

        val transport = StdioServerTransport(System.`in`.asSource().buffered(), System.out.asSink().buffered())
        server.createSession(transport)
        awaitCancellation()
    }
}

suspend fun runWatcherBot() {
    logger.info("Starting to check gold price...")
    
    val targetHighPrice = System.getenv("TARGET_HIGH_PRICE")?.toDoubleOrNull() ?: 5100.0
    val targetLowPrice = System.getenv("TARGET_LOW_PRICE")?.toDoubleOrNull() ?: 5080.0
    val interval = System.getenv("CHECK_INTERVAL")?.toLongOrNull() ?: 60_000L

    createHttpClient().use { client ->
        val apiClient = GoldPriceApiClient(client)
        val telegramClient = TelegramBotClient(client)

        var previousPrice: Double? = null

        while (true) {
            val goldData = apiClient.fetchGoldPrice()

            if (goldData != null) {
                val currentPrice = goldData.price
                
                if (currentPrice != previousPrice) {
                    if (currentPrice >= targetHighPrice || currentPrice <= targetLowPrice) {
                        val status = if (currentPrice >= targetHighPrice) "📈 Price has exceeded the target" else "📉 Price has dropped below the target"
                        val message = """
                            $status
                            Type: ${goldData.name} (${goldData.symbol})
                            Current Price: $${currentPrice}
                            Time: ${goldData.updatedAt}
                        """.trimIndent()

                        telegramClient.sendMessage(message)
                        logger.info("Target reached: {}. Sent to Telegram.", currentPrice)
                    } else {
                        logger.info("Price changed: {} (Not reached target).", currentPrice)
                    }
                    previousPrice = currentPrice
                } else {
                    logger.info("No price change (Current: {}).", currentPrice)
                }
            } else {
                logger.warn("⚠️ Difficulty retrieving gold price.")
            }

            logger.info("Waiting {} minute(s) before checking again...", interval / 60_000)
            delay(interval)
        }
    }
}