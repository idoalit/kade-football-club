package com.idoalit.footballmatchschedule.views

import com.idoalit.footballmatchschedule.models.Event

interface EventView {
    fun showLoading()
    fun hideLoading()
    fun showEventList(list: List<Event>)
    fun showDetail(eventId: String?)
}