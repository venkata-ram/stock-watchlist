package com.msf.watchlist.plugins

import com.msf.watchlist.controller.api.watchlistRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        watchlistRouting()
    }
}
