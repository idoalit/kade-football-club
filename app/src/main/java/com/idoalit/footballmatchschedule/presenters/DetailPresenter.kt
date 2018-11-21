package com.idoalit.footballmatchschedule.presenters

import com.google.gson.Gson
import com.idoalit.footballmatchschedule.api.ApiRespository
import com.idoalit.footballmatchschedule.api.TheSportDBApi
import com.idoalit.footballmatchschedule.models.EventResponse
import com.idoalit.footballmatchschedule.models.TeamResponse
import com.idoalit.footballmatchschedule.views.DetailView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailPresenter(
    private val view: DetailView,
    private val apiRespository: ApiRespository,
    private val gson: Gson
) {

    fun getDetailEvent(eventId: String?) {
        view.showLoading()

        GlobalScope.launch(Dispatchers.Main) {
            val data = gson.fromJson(apiRespository.doRequest(TheSportDBApi.getDetalEvent(eventId)).await(), EventResponse::class.java)
            view.hideLoading()
            view.showDetailData(data.events[0])
        }
    }

    fun getDetailTeam(teamId: String?, team: String) {
        view.showLoading()

        GlobalScope.launch(Dispatchers.Main) {
            val data = gson.fromJson(apiRespository.doRequest(TheSportDBApi.getDetailTeam(teamId)).await(), TeamResponse::class.java)
            view.hideLoading()
            view.showTeamLogo(data.teams[0].strTeamBadge, team)
        }
    }

}