package com.goldyonwar.network

import com.goldyonwar.model.GoldPriceResponse
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GoldPriceApiClientTest {

    @Test
    fun `fetchGoldPrice should return response when API is successful`() = runTest {
        // Setup mock engine
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"name":"Gold","symbol":"XAU","price":2000.0,"updatedAt":"2024-03-14T12:00:00Z"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val apiClient = GoldPriceApiClient(client)
        val response = apiClient.fetchGoldPrice()

        assertNotNull(response)
        assertEquals(2000.0, response.price)
        assertEquals("XAU", response.symbol)
    }

    @Test
    fun `fetchGoldPrice should return null when API fails`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = "Internal Server Error",
                status = HttpStatusCode.InternalServerError
            )
        }

        val client = HttpClient(mockEngine)

        val apiClient = GoldPriceApiClient(client)
        val response = apiClient.fetchGoldPrice()

        assertEquals(null, response)
    }
}
