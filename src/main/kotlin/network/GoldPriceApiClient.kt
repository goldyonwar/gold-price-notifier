package com.goldyonwar.network

import com.goldyonwar.model.GoldPriceResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class GoldPriceApiClient(private val client: HttpClient) {

    suspend fun fetchGoldPrice(): GoldPriceResponse? {
        return try {
            client.get(API_URL).body<GoldPriceResponse>()
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        private const val API_URL = "https://api.gold-api.com/price/XAU"
    }

}