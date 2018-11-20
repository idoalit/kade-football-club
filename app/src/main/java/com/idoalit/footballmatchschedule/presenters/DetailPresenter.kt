package com.idoalit.footballmatchschedule.presenters

import com.google.gson.Gson
import com.idoalit.footballmatchschedule.api.ApiRespository
import com.idoalit.footballmatchschedule.api.TheSportDBApi
import com.idoalit.footballmatchschedule.models.EventResponse
import com.idoalit.footballmatchschedule.models.TeamResponse
import com.idoalit.footballmatchschedule.views.DetailView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DetailPresenter(
    private val view: DetailView,
    private val apiRespository: ApiRespository,
    private val gson: Gson
) {

    fun onCreateView(eventId: String?) {
        getDetailEvent(eventId)
    }

    fun getDetailEvent(eventId: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRespository.doRequest(TheSportDBApi.getDetalEvent(eventId)), EventResponse::class.java)

            uiThread {
                view.hideLoading()
                view.showDetailData(data.events[0])
            }
        }
    }

    fun getDetailTeam(teamId: String?, team: String) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRespository.doRequest(TheSportDBApi.getDetailTeam(teamId)), TeamResponse::class.java)

            uiThread {
                view.hideLoading()
                view.showTeamLogo(data.teams[0].strTeamBadge, team)
            }
        }
    }

}