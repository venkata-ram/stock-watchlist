package com.msf.watchlist.repository.database

import com.msf.watchlist.repository.database.table.RecentWatchlistTable
import com.msf.watchlist.repository.database.table.WatchlistTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        // Data from config
        val driverClassName = config.property("ktor.database.driverClassName").getString()
        val jdbcURL = config.property("ktor.database.jdbcURL").getString()
        val defaultDatabase = config.property("ktor.database.database").getString()
        val username = config.property("ktor.database.user").getString()
        val password = config.property("ktor.database.password").getString()
        val maxPoolSize = config.property("ktor.database.maxPoolSize").getString()
        val autoCommit = config.property("ktor.database.autoCommit").getString()

        // Creating instance for database connection
        val connectionPool = createHikariDataSource(
            url = "$jdbcURL/$defaultDatabase?user=$username&password=$password",
            driver = driverClassName,
            maxPoolSize.toInt(),
            autoCommit.toBoolean()
        )

        // Connecting to database and adding schemas
        val database = Database.connect(connectionPool)
        transaction(database) {
            SchemaUtils.create(WatchlistTable, RecentWatchlistTable)
        }
    }

    private fun createHikariDataSource(
        url: String,
        driver: String,
        maxPoolSize: Int,
        autoCommit: Boolean
    ) = HikariDataSource(HikariConfig().apply {
        driverClassName = driver
        jdbcUrl = url
        maximumPoolSize = maxPoolSize
        isAutoCommit = autoCommit
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}