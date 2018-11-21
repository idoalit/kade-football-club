package com.idoalit.footballmatchschedule.screens

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.idoalit.footballmatchschedule.R
import com.idoalit.footballmatchschedule.api.ApiRespository
import com.idoalit.footballmatchschedule.database.database
import com.idoalit.footballmatchschedule.models.Event
import com.idoalit.footballmatchschedule.models.Favorite
import com.idoalit.footballmatchschedule.presenters.DetailPresenter
import com.idoalit.footballmatchschedule.views.DetailView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity(), DetailView {

    companion object {
        const val EVENT_ID_KEY = "EVENT_ID_KEY"
    }

    private var eventId: String = ""
    private lateinit var event: Event
    private lateinit var presenter: DetailPresenter
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_detail)

        val request = ApiRespository()
        val gson = Gson()
        presenter = DetailPresenter(this, request, gson)

        val intent = intent
        eventId = intent.getStringExtra(EVENT_ID_KEY)
        favoriteState()
        presenter.getDetailEvent(eventId)
        root.onRefresh {
            presenter.getDetailEvent(eventId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == android.R.id.home -> {
                finish()
                true
            }
            item?.itemId == R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun addToFavorite() {
        try {
            database.use {
                insert(Favorite.TABLE_FAVORITE,
                    Favorite.EVENT_ID to eventId,
                    Favorite.EVENT_DATE to event.dateEvent,
                    Favorite.HOME_TEAM to event.strHomeTeam,
                    Favorite.HOME_SCORE to event.intHomeScore,
                    Favorite.AWAY_TEAM to event.strAwayTeam,
                    Favorite.AWAY_SCORE to event.intAwayScore
                    )
            }
            root.snackbar(getString(R.string.added_to_favorite)).show()
        } catch (e: SQLiteConstraintException) {
            root.snackbar(e.localizedMessage).show()
        }
    }

    override fun removeFromFavorite() {
        try {
            database.use {
                delete(Favorite.TABLE_FAVORITE, "(${Favorite.EVENT_ID} = {id})", "id" to eventId)
            }
            root.snackbar(getString(R.string.removed_from_favorite)).show()
        } catch (e: SQLiteConstraintException) {
            root.snackbar(e.localizedMessage).show()
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

        this.event = event

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

    private fun formatDate(date: String?, time: String?): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = "$date $time"
        return formatter.parse(dateTime)
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_border)
    }

    private fun favoriteState(){
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                .whereArgs("(EVENT_ID = {id})",
                    "id" to eventId)
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }
}
