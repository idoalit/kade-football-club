package com.idoalit.footballmatchschedule.presenters

import com.google.gson.Gson
import com.idoalit.footballmatchschedule.api.ApiRespository
import com.idoalit.footballmatchschedule.api.TheSportDBApi
import com.idoalit.footballmatchschedule.models.EventResponse
import com.idoalit.footballmatchschedule.views.EventView
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
}