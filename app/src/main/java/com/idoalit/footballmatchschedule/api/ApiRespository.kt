package com.idoalit.footballmatchschedule.api

import android.util.Log
import java.net.URL

class ApiRespository {

    fun doRequest(url: String) :String {
        Log.d("URL REQUEST", url)
        return URL(url).readText()
    }
}