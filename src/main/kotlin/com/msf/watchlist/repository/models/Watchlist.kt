package com.msf.watchlist.repository.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Watchlist(
    var id : Int? = 0,
    var accountId : Int?,
    var stockSymbol : String?,
    var isActive : Boolean?
)
