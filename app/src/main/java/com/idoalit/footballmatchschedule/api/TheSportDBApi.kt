package com.idoalit.footballmatchschedule.api

import com.idoalit.footballmatchschedule.BuildConfig

object TheSportDBApi {

    fun getPastEvent(idLeague: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/eventspastleague.php?id=$idLeague"
    }

    fun getNextEvent(idLeague: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/eventsnextleague.php?id=$idLeague"
    }

    fun getDetalEvent(idEvent: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/lookupevent.php?id=$idEvent"
    }

    fun getDetailTeam(idTeam: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/lookupteam.php?id=$idTeam"
    }

}