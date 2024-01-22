package com.msf.watchlist.controller.api

import com.msf.watchlist.repository.WatchlistServiceImpl
import com.msf.watchlist.repository.models.Watchlist
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.watchlistRouting(){
    val watchlistService = WatchlistServiceImpl()

    route("/watchlist"){
        get {
            val listOfWatchlist = watchlistService.getAllWatchlist()
            call.respond(listOfWatchlist)
        }
        get("/recent"){
            val listOfRecentWatchlist = watchlistService.getAllRecentWatchlist()
            call.respond(listOfRecentWatchlist)
        }
        get("/recent_table") {
            val listOfRecentWatchlistTable = watchlistService.getAllRecentWatchlistTable()
            call.respond(listOfRecentWatchlistTable)
        }
        post("/insert") {
            try {
                val watchlist = call.receive<Watchlist>()
                val isActive = watchlist.isActive
                val accountId = watchlist.accountId
                val stockSymbol = watchlist.stockSymbol
                if(isActive == null || accountId == null || stockSymbol.isNullOrBlank()){
                    throw IllegalArgumentException("Missing fields in request")
                }

                val watchlistId = watchlistService.insertWatchlist(watchlist)
                if(isActive) {
                    watchlistService.insertOrUpdateRecentWatchlist(watchlistId, accountId)
                }
                call.respondText("Success,inserted watchlist Id $watchlistId", status = HttpStatusCode.Created)
            }catch (e : ContentTransformationException){
                call.respond(status = HttpStatusCode.BadRequest,"invalid request")
            }


        }
        delete("/delete/{id}") {
            val watchListId = call.parameters.getOrFail("id").toInt()
            if (!watchlistService.isIdExists(watchListId)){
                throw NotFoundException("id not found")
            }else {
                watchlistService.deleteWatchlist(watchListId)
                call.respondText("Deleted", status = HttpStatusCode.OK)
            }
        }
    }
}