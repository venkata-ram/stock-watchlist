package com.msf.watchlist.repository

import com.msf.watchlist.repository.database.DatabaseFactory.dbQuery
import com.msf.watchlist.repository.database.table.RecentWatchlistTable
import com.msf.watchlist.repository.database.table.WatchlistTable
import com.msf.watchlist.repository.models.RecentWatchlist
import com.msf.watchlist.repository.models.Watchlist
import com.msf.watchlist.service.WatchlistService
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class WatchlistServiceImpl : WatchlistService {



    override suspend fun insertWatchlist(watchList: Watchlist): Int = dbQuery {
        val accountId = watchList.accountId!!
        val stockSymbol = watchList.stockSymbol!!
        val isActive = watchList.isActive!!
        val watchlistId = WatchlistTable.insertAndGetId {
            it[WatchlistTable.accountId] = accountId
            it[WatchlistTable.stockSymbol] = stockSymbol
            it[WatchlistTable.isActive] = isActive
        }.value

        watchlistId
    }

    override suspend fun insertOrUpdateRecentWatchlist(watchlistId: Int, accountId: Int): Unit = dbQuery{
        // Check if the accountId exists in the RecentWatchlist table
        val existsInRecentWatchlist = RecentWatchlistTable.select { RecentWatchlistTable.accountId eq accountId }.any()

        if (existsInRecentWatchlist) {
            // If the watchlistId exists, update the record
            updateRecentWatchlist(watchlistId,accountId)
        } else {
            // If the watchlistId does not exist, insert a new record
            insertRecentWatchlist(watchlistId,accountId)
        }
    }

    override suspend fun insertRecentWatchlist(watchlistId: Int, accountId: Int) : RecentWatchlist? = dbQuery {
        val insertRecentWatchlist = RecentWatchlistTable.insert {
            it[RecentWatchlistTable.accountId] = accountId
            it[RecentWatchlistTable.watchlistId] = watchlistId
        }
        insertRecentWatchlist.resultedValues?.singleOrNull()?.let(::rowToRecentWatchList)
    }

    override suspend fun updateRecentWatchlist(watchlistId: Int, accountId: Int) = dbQuery {
        val updateRecentWatchlist = RecentWatchlistTable.update({ RecentWatchlistTable.accountId eq accountId }) {
                it[RecentWatchlistTable.watchlistId] = watchlistId
        }
    }

    override suspend fun deleteWatchlist(watchlistId: Int) = dbQuery {
        // Set is_active to false in the Watchlist table
        WatchlistTable.update({ WatchlistTable.id eq watchlistId }) {
            it[WatchlistTable.isActive] = false
        }

        // Check if the watchlistId exists in the RecentWatchlist table
        val existsInRecentWatchlist =
            RecentWatchlistTable.select { RecentWatchlistTable.watchlistId eq watchlistId }.any()

        if (existsInRecentWatchlist) {
            // to get the accountId associated with watchlist deleted
            val accountId = WatchlistTable
                .slice(WatchlistTable.accountId)
                .select { WatchlistTable.id eq watchlistId }
                .single()[WatchlistTable.accountId]

            // to get the last modified watchlistId corresponding to the above accountId
            // returns watchlistId or null
            val lastWatchlistId = WatchlistTable
                .slice(WatchlistTable.id)
                .select { (WatchlistTable.accountId eq accountId) and (WatchlistTable.isActive eq true) }
                .orderBy(WatchlistTable.id to SortOrder.DESC)
                .limit(1).singleOrNull()?.get(WatchlistTable.id)

            // Update the RecentWatchlist table
            RecentWatchlistTable.update({ RecentWatchlistTable.watchlistId eq watchlistId }) {
                it[RecentWatchlistTable.watchlistId] = lastWatchlistId?.value
            }
        }
    }

    override suspend fun getAllWatchlist(): List<Watchlist> = dbQuery {
        val allWatchlist =  WatchlistTable.selectAll().orderBy(WatchlistTable.id,SortOrder.ASC).map { rowToWatchList(it) }
        allWatchlist
    }

    override suspend fun getAllRecentWatchlistTable(): List<RecentWatchlist> = dbQuery {
        val allRecentWatchlist =  RecentWatchlistTable.selectAll().orderBy(RecentWatchlistTable.watchlistId,SortOrder.DESC).map { rowToRecentWatchList(it) }
        allRecentWatchlist
    }

    override suspend fun getAllRecentWatchlist(): List<Watchlist> = dbQuery {
        // get all Watchlist present in RecentWatchlist table
        WatchlistTable
            .innerJoin(
                RecentWatchlistTable,
                { WatchlistTable.id},
                {RecentWatchlistTable.watchlistId})
            .selectAll()
            .orderBy(WatchlistTable.id,SortOrder.DESC)
            .map { rowToWatchList(it) }
    }

    override suspend fun isIdExists(id : Int): Boolean = dbQuery {
        WatchlistTable.select { WatchlistTable.id eq id}.any()
    }

    private fun rowToWatchList(row: ResultRow): Watchlist {
        return Watchlist(
            id = row[WatchlistTable.id].value,
            accountId = row[WatchlistTable.accountId],
            stockSymbol = row[WatchlistTable.stockSymbol],
            isActive = row[WatchlistTable.isActive]
        )
    }

    private fun rowToRecentWatchList(row: ResultRow) = RecentWatchlist(
            watchListId = row[RecentWatchlistTable.watchlistId]?.value,
            accountId = row[RecentWatchlistTable.accountId]
        )

}
