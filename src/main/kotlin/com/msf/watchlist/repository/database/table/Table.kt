package com.msf.watchlist.repository.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

// table WatchList
object WatchlistTable : IntIdTable(name = "watchlist"){
    val accountId = integer("account_id")
    val stockSymbol = varchar("stock_symbol", 50)
    val isActive = bool("is_active")
}

// table RecentWatchlist
object RecentWatchlistTable : IntIdTable(name = "recent_watchlist") {
    val accountId = integer("account_id")
    val watchlistId = reference("watchlist_id", WatchlistTable).nullable()
}
