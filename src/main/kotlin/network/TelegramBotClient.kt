package com.goldyonwar.network

import com.goldyonwar.model.TelegramMessageRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.slf4j.LoggerFactory

class TelegramBotClient(private val client: HttpClient) {

    private val logger = LoggerFactory.getLogger(TelegramBotClient::class.java)

    private val botToken = System.getenv("TELEGRAM_BOT_TOKEN")
        ?: throw IllegalArgumentException("TELEGRAM_BOT_TOKEN is missing")
    private val chatId = System.getenv("TELEGRAM_CHAT_ID")
        ?: throw IllegalArgumentException("TELEGRAM_CHAT_ID is missing")
    private val sendMessageUrl = "${BASE_URL}/bot$botToken/sendMessage"

    /**
     * Sends message to Telegram; handles exceptions
     */
    suspend fun sendMessage(message: String) {
        try {
            val requestBody = TelegramMessageRequest(
                chatId = chatId,
                text = message
            )

            client.post(sendMessageUrl) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            
            logger.info("Message sent to Telegram successfully.")
        } catch (e: Exception) {
            logger.error("Error sending message to Telegram: {}", e.message)
        }
    }

    companion object {
        private const val BASE_URL = "https://api.telegram.org"
    }

}