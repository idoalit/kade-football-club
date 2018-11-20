package com.idoalit.footballmatchschedule.presenters

import com.google.gson.Gson
import com.idoalit.footballmatchschedule.api.ApiRespository
import com.idoalit.footballmatchschedule.api.TheSportDBApi
import com.idoalit.footballmatchschedule.models.Event
import com.idoalit.footballmatchschedule.models.EventResponse
import com.idoalit.footballmatchschedule.views.EventView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class EventPresenterTest {
    @Mock
    private lateinit var eventView: EventView

    @Mock
    private lateinit var apiRepository: ApiRespository

    @Mock
    private lateinit var gson: Gson

    private lateinit var eventPresenter: EventPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        eventPresenter = EventPresenter(eventView, apiRepository, gson)
    }

    @Test
    fun getPastEventList() {
        val eventList: MutableList<Event> = mutableListOf()
        val response = EventResponse(eventList)
        val league = "4328"

        GlobalScope.launch {
            `when`(gson.fromJson(apiRepository.doRequest(TheSportDBApi.getPastEvent(league)).await(),
                EventResponse::class.java))
                .thenReturn(response)

            eventPresenter.getPastEventList(league)

            Mockito.verify(eventView).showLoading()
            Mockito.verify(eventView).showEventList(eventList)
            Mockito.verify(eventView).hideLoading()
        }
    }

    @Test
    fun getNextEventList() {
        val eventList: MutableList<Event> = mutableListOf()
        val response = EventResponse(eventList)
        val league = "4328"


        GlobalScope.launch {
            `when`(gson.fromJson(apiRepository.doRequest(TheSportDBApi.getNextEvent(league)).await(),
                EventResponse::class.java))
                .thenReturn(response)

            eventPresenter.getPastEventList(league)

            Mockito.verify(eventView).showLoading()
            Mockito.verify(eventView).showEventList(eventList)
            Mockito.verify(eventView).hideLoading()
        }
    }
}