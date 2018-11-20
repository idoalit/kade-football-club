package com.idoalit.footballmatchschedule.presenters

import com.google.gson.Gson
import com.idoalit.footballmatchschedule.api.ApiRespository
import com.idoalit.footballmatchschedule.api.TheSportDBApi
import com.idoalit.footballmatchschedule.models.Event
import com.idoalit.footballmatchschedule.models.EventResponse
import com.idoalit.footballmatchschedule.models.Team
import com.idoalit.footballmatchschedule.models.TeamResponse
import com.idoalit.footballmatchschedule.views.DetailView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class DetailPresenterTest {
    @Mock
    private lateinit var detailView: DetailView

    @Mock
    private lateinit var apiRespository: ApiRespository

    @Mock
    private lateinit var gson: Gson

    private lateinit var detailPresenter: DetailPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        detailPresenter = DetailPresenter(detailView, apiRespository, gson)
    }

    @Test
    fun getDetailEvent() {
        val eventId = "441613"
        val eventList: MutableList<Event> = mutableListOf()
        val event = Event()
        eventList.add(event)
        val response = EventResponse(eventList)

        GlobalScope.launch {
            `when`(gson.fromJson(apiRespository.doRequest(TheSportDBApi.getDetalEvent(eventId)).await(),
                EventResponse::class.java))
                .thenReturn(response)

            detailPresenter.getDetailEvent(eventId)
            Mockito.verify(detailView).showLoading()
            Mockito.verify(detailView).showDetailData(event)
            Mockito.verify(detailView).hideLoading()
        }
    }

    @Test
    fun getDetailTeam() {
        val teamId = "133604"
        val teamList: MutableList<Team> = mutableListOf()
        val team = Team(strTeam = "Arsenal")
        teamList.add(team)
        val response = TeamResponse(teamList)

        GlobalScope.launch {
            `when`(gson.fromJson(apiRespository.doRequest(TheSportDBApi.getDetailTeam(teamId)).await(),
                TeamResponse::class.java))
                .thenReturn(response)

            detailPresenter.getDetailTeam(teamId, team.strTeam!!)

            Mockito.verify(detailView).showLoading()
            Mockito.verify(detailView).showTeamLogo(team.strTeamLogo, team.strTeam!!)
            Mockito.verify(detailView).hideLoading()
        }
    }
}