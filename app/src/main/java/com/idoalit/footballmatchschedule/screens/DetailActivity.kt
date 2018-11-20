package com.idoalit.footballmatchschedule.screens

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.gson.Gson
import com.idoalit.footballmatchschedule.R
import com.idoalit.footballmatchschedule.api.ApiRespository
import com.idoalit.footballmatchschedule.models.Event
import com.idoalit.footballmatchschedule.presenters.DetailPresenter
import com.idoalit.footballmatchschedule.views.DetailView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.support.v4.onRefresh
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity(), DetailView {

    companion object {
        const val EVENT_ID_KEY = "EVENT_ID_KEY"
    }

    private var eventId: String = ""
    private lateinit var presenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_detail)

        val request = ApiRespository()
        val gson = Gson()
        presenter = DetailPresenter(this, request, gson)

        val intent = intent
        eventId = intent.getStringExtra(EVENT_ID_KEY)
        presenter.onCreateView(eventId)
        root.onRefresh {
            presenter.onCreateView(eventId)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading() {
        root.isRefreshing = true
    }

    override fun hideLoading() {
        root.isRefreshing = false
    }

    override fun showTeamLogo(strImage: String?, team: String) {
        when (team) {
            "home" -> Picasso.get().load(strImage).into(logo_home)
            "away" -> Picasso.get().load(strImage).into(logo_away)
        }
    }

    override fun showDetailData(event: Event) {

        Log.d("EVENT", event.toString())

        val dateTime = formatDate(event.dateEvent, event.strTime)

        date_event.text = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
            .format(dateTime)

        time_event.text = SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(dateTime)

        str_home_team.text = event.strHomeTeam
        int_home_score.text = event.intHomeScore
        str_home_goal_details.text = event.strHomeGoalDetails?.replace(";", "\n")
        int_home_shots.text = event.intHomeShots
        str_home_lineup_goalkeeper.text = event.strHomeLineupGoalkeeper
        str_home_lineup_defense.text = event.strHomeLineupDefense?.replace(";", "\n")
        presenter.getDetailTeam(event.idHomeTeam, "home")

        str_away_team.text = event.strAwayTeam
        int_away_score.text = event.intAwayScore
        str_away_goal_details.text = event.strAwayGoalDetails?.replace(";", "\n")
        int_away_shots.text = event.intAwayShots
        str_away_lineup_goalkeeper.text = event.strAwayLineupGoalkeeper
        str_away_lineup_defense.text = event.strAwayLineupDefense?.replace(";", "\n")
        presenter.getDetailTeam(event.idAwayTeam, "away")
    }

    fun formatDate(date: String?, time: String?): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = "$date $time"
        return formatter.parse(dateTime)
    }
}
