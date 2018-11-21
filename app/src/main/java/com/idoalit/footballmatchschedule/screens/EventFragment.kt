package com.idoalit.footballmatchschedule.screens


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import com.idoalit.footballmatchschedule.MainAdapter

import com.idoalit.footballmatchschedule.R
import com.idoalit.footballmatchschedule.api.ApiRepository
import com.idoalit.footballmatchschedule.models.Event
import com.idoalit.footballmatchschedule.presenters.EventPresenter
import com.idoalit.footballmatchschedule.views.EventView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import com.idoalit.footballmatchschedule.MainActivity

class EventFragment : Fragment(), EventView {

    private var eventList: MutableList<Event> = mutableListOf()
    private var league: String = "4328"
    private var event: String? = ""
    private lateinit var adapter: MainAdapter
    private lateinit var presenter: EventPresenter
    private lateinit var listEvent: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var eventTitle: TextView
    private lateinit var countingIdlingResource: CountingIdlingResource

    override fun showDetail(eventId: String?) {
        startActivity<DetailActivity>(DetailActivity.EVENT_ID_KEY to "$eventId")
    }

    override fun showLoading() {
        swipeLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeLayout.isRefreshing = false
    }

    override fun showMessage(message: String) {
        Snackbar.make(swipeLayout, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showEventList(list: List<Event>) {
        eventList.clear()
        eventList.addAll(list)
        adapter.notifyDataSetChanged()
        countingIdlingResource.decrement()
    }

    @SuppressLint("VisibleForTests")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        event = arguments?.getString(EVENT_KEY)

        countingIdlingResource = (activity as MainActivity).getIdlingResource()

        listEvent = view.findViewById(R.id.recyclerView)
        swipeLayout = view.findViewById(R.id.swipeLayout)
        eventTitle = view.findViewById(R.id.event_title)

        listEvent.layoutManager = LinearLayoutManager(context)
        listEvent.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        adapter = MainAdapter(eventList) {
            // start detail activity
            showDetail(it.idEvent)
        }
        listEvent.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = EventPresenter(this, request, gson)

        getEvent(event)
        swipeLayout.onRefresh {
            getEvent(event)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        getEvent(event)
    }

    private fun getEvent(event: String?) {
        countingIdlingResource.increment()
        when (event) {
            LAST_EVENT -> {
                eventTitle.text = getString(R.string.last_match)
                presenter.getPastEventList(league)
            }
            NEXT_EVENT -> {
                eventTitle.text = getString(R.string.next_match)
                presenter.getNextEventList(league)
            }
            FAVORITE_EVENT -> {
                eventTitle.text = getString(R.string.favorite_match)
                presenter.getFavoriteEvent(context)
            }
        }
    }

    companion object {

        private const val EVENT_KEY = "EVENT_KEY"
        const val LAST_EVENT = "LAST_EVENT"
        const val NEXT_EVENT = "NEXT_EVENT"
        const val FAVORITE_EVENT = "FAVORITE_EVENT"

        fun newInstance(event: String?) : EventFragment {
            val fragment = EventFragment()
            val bundle = Bundle()
            bundle.putString(EVENT_KEY, event)
            fragment.arguments = bundle
            return fragment
        }
    }
}
