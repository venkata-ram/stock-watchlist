package com.msf.watchlist

import com.msf.watchlist.plugins.configureDatabases
import com.msf.watchlist.plugins.configureException
import com.msf.watchlist.plugins.configureRouting
import com.msf.watchlist.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val env = environment.config.propertyOrNull("ktor.environment")?.getString()
    val port = environment.config.propertyOrNull("ktor.deployment.port")?.getString() ?: "8080"
    println("env $env")
    println("port $port")

    configureSerialization()
    configureDatabases()
    configureRouting()
    configureException()
}
