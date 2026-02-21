package com.goldyonwar.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoldPriceResponse(
    @SerialName("name") val name: String? = null,
    @SerialName("symbol") val symbol: String? = null,
    @SerialName("price") val price: Double,
    @SerialName("updatedAt") val updatedAt: String? = null,
    @SerialName("updatedAtReadable") val updatedAtReadable: String? = null
)