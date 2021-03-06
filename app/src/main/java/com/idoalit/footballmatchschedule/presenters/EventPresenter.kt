package com.idoalit.footballmatchschedule.presenters

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.google.gson.Gson
import com.idoalit.footballmatchschedule.api.ApiRespository
import com.idoalit.footballmatchschedule.api.TheSportDBApi
import com.idoalit.footballmatchschedule.database.database
import com.idoalit.footballmatchschedule.models.Event
import com.idoalit.footballmatchschedule.models.EventResponse
import com.idoalit.footballmatchschedule.models.Favorite
import com.idoalit.footballmatchschedule.views.EventView
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EventPresenter(
    private val view: EventView,
    private val apiRespository: ApiRespository,
    private val gson: Gson
) {
    fun getPastEventList(league: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRespository
                .doRequest(TheSportDBApi.getPastEvent(league)),
                EventResponse::class.java
            )

            uiThread {
                view.hideLoading()
                view.showEventList(data.events)
            }
        }
    }

    fun getNextEventList(league: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRespository
                .doRequest(TheSportDBApi.getNextEvent(league)),
                EventResponse::class.java
            )

            uiThread {
                view.hideLoading()
                view.showEventList(data.events)
            }
        }
    }

    fun getFavoriteEvent(ctx: Context?) {
        view.showLoading()
        try {
            ctx?.database?.use {
                val result = select(Favorite.TABLE_FAVORITE)
                val favorite = result.parseList(classParser<Favorite>())
                val events: MutableList<Event> = mutableListOf()
                favorite.forEach {
                    events.add(
                        Event(
                            idEvent = it.eventId,
                            strHomeTeam = it.homeTeam,
                            intHomeScore = it.homeScore,
                            strAwayTeam = it.awayTeam,
                            intAwayScore = it.awayScore,
                            dateEvent = it.eventDate
                        )
                    )
                }
                view.showEventList(events)
            }
        } catch (e: SQLiteConstraintException) {
            view.showMessage(e.localizedMessage)
        }
        view.hideLoading()
    }
}