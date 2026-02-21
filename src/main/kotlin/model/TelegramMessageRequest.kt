package com.goldyonwar.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramMessageRequest(
    @SerialName("chat_id") val chatId: String,
    val text: String
)