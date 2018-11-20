package com.idoalit.footballmatchschedule.views

import com.idoalit.footballmatchschedule.models.Event

interface DetailView {
    fun showLoading()
    fun hideLoading()
    fun showTeamLogo(strImage: String?, team: String)
    fun showDetailData(event: Event)
    fun addToFavorite()
    fun removeFromFavorite()
}