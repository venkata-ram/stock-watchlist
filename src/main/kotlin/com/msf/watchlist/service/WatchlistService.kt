package com.msf.watchlist.service

import com.msf.watchlist.repository.models.RecentWatchlist
import com.msf.watchlist.repository.models.Watchlist


interface WatchlistService {
    suspend fun insertWatchlist(watchList : Watchlist) : Int
    suspend fun insertRecentWatchlist(watchlistId : Int,accountId : Int) : RecentWatchlist?
    suspend fun updateRecentWatchlist(watchlistId : Int,accountId : Int)
    suspend fun insertOrUpdateRecentWatchlist(watchlistId : Int,accountId : Int)
    suspend fun deleteWatchlist(watchlistId : Int)
    suspend fun getAllWatchlist() : List<Watchlist>
    suspend fun getAllRecentWatchlist() : List<Watchlist>
    suspend fun getAllRecentWatchlistTable() : List<RecentWatchlist>
    suspend fun isIdExists(id : Int) : Boolean
}