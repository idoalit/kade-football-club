package com.idoalit.footballmatchschedule

import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

class RecyclerViewItemAssertion(val expectedCount: Int): ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        val recyclerView: RecyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter!!.itemCount, `is`(expectedCount))
    }
}