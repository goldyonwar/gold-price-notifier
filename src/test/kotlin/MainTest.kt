package com.goldyonwar

import com.goldyonwar.model.GoldPriceResponse
import com.goldyonwar.network.GoldPriceApiClient
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class MainTest {

    @Test
    fun `handleGetGoldPrice should return success message when API succeeds`() = runTest {
        val apiClient = mockk<GoldPriceApiClient>()
        coEvery { apiClient.fetchGoldPrice() } returns GoldPriceResponse(
            name = "Gold",
            symbol = "XAU",
            price = 2000.0,
            updatedAt = "2024-03-14"
        )

        val result = handleGetGoldPrice(apiClient)
        val text = (result.content[0] as TextContent).text
        
        assertTrue(text.contains("$2000.0"))
        assertTrue(text.contains("The current gold price is"))
    }

    @Test
    fun `handleGetGoldPrice should return error message when API fails`() = runTest {
        val apiClient = mockk<GoldPriceApiClient>()
        coEvery { apiClient.fetchGoldPrice() } returns null

        val result = handleGetGoldPrice(apiClient)
        val text = (result.content[0] as TextContent).text
        
        assertTrue(text.contains("unable to fetch the gold price"))
    }
}
