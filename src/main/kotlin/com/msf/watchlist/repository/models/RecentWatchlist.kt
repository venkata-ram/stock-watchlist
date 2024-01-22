package com.msf.watchlist.repository.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RecentWatchlist(
    var accountId : Int?,
    var watchListId : Int? = 0
)
