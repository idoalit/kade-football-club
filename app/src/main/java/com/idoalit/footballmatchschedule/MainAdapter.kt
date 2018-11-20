package com.idoalit.footballmatchschedule

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idoalit.footballmatchschedule.models.Event
import kotlinx.android.synthetic.main.item_event_list.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainAdapter(private val list: List<Event>, private val listener: (Event) -> Unit) :
    RecyclerView.Adapter<MainAdapter.EventViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_event_list, p0, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: EventViewHolder, p1: Int) {
        p0.binView(list[p1], listener)
    }

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun binView(event: Event, listener: (Event) -> Unit) {
            itemView.homeTeam.text = event.strHomeTeam
            itemView.homeScore.text = event.intHomeScore
            itemView.awayTeam.text = event.strAwayTeam
            itemView.awayScore.text = event.intAwayScore
            itemView.eventDate.text = event.dateEvent
            itemView.onClick { listener(event) }
        }
    }
}