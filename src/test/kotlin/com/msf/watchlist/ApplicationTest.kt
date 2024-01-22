package com.msf.watchlist


import com.msf.watchlist.repository.models.Watchlist
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testPostWatchlistPositive() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/watchlist/insert") {
            contentType(ContentType.Application.Json)
            // proper input
            setBody(Watchlist(accountId = 1000, stockSymbol = "AAPL", isActive = true))
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testPostWatchlistNegative() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/watchlist/insert") {
            contentType(ContentType.Application.Json)
            // null values in request
            setBody(Watchlist(accountId = null, stockSymbol = "", isActive = null))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testDeleteWatchlistPositive() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        // with watchlist id as 1
        val response = client.delete("/watchlist/delete/1")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testDeleteWatchlistNegative() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // with watchlist id as 55
        val response = client.delete("/watchlist/delete/55")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

}
