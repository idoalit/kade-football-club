package com.idoalit.footballmatchschedule

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.idoalit.footballmatchschedule.R.id.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.test.espresso.matcher.ViewMatchers.*
import com.idoalit.footballmatchschedule.database.database
import com.idoalit.footballmatchschedule.models.Favorite
import org.hamcrest.Matchers.allOf
import org.jetbrains.anko.db.delete


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var mIdlingResource: CountingIdlingResource

    @Before
    fun setUp() {
        mIdlingResource = activityRule.activity.getIdlingResource()
        IdlingRegistry.getInstance().register(mIdlingResource)
    }

    @Test
    fun testActivityBehavior() {
        // Memastikan bahwa aplikasi terbuka dengan menampilkan BottomNavigation
        onView(withId(navigation)).check(matches(isDisplayed()))
        // Memastikan bahwa recyclerView pada fragment juga sudah ditampilkan
        onView(withId(recyclerView)).check(matches(isDisplayed()))
        // Memastikan bahwa data event sudah ditampilkan pada recyclerview dengan ekpektasi sejumlah 15
        // sesuai dengan request ke server
        onView(withId(recyclerView)).check(RecyclerViewItemAssertion(15))
        // Scroll recyclerview ke posisi terakhir
        onView(withId(recyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(14))
        // Klik pada item terakhir
        onView(withId(recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(14, click())
        )
        // Memastikan detail activity sudah ditampilkan
        onView(withId(action_bar)).check(matches(isDisplayed()))
        onView(withText(R.string.app_name)).check(matches(withParent(withId(action_bar))))
        // Menambahkan data ke favorite
        onView(allOf(withId(add_to_favorite), isDisplayed())).perform(click())
        // Memastikan data sudah masuk
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.added_to_favorite)))
            .check(matches(isDisplayed()))
        // Kembali ke main activity
        onView(allOf(withContentDescription("Navigate up"), isDisplayed())).perform(click())
    }

    @Test
    fun testNavigation() {
        // Memastikan bahwa aplikasi terbuka dengan menampilkan BottomNavigation
        onView(withId(navigation)).check(matches(isDisplayed()))
        // Fragment yang tampil saat ini adalah Last match
        onView(allOf(withId(event_title), isDisplayed())).check(matches(withText(R.string.last_match)))
        // ke halaman next match
        onView(allOf(withId(navigation_next), isDisplayed())).perform(click())
        onView(allOf(withId(event_title), isDisplayed())).check(matches(withText(R.string.next_match)))
        // ke halaman next favorite
        onView(allOf(withId(navigation_favorite), isDisplayed())).perform(click())
        onView(allOf(withId(event_title), isDisplayed())).check(matches(withText(R.string.favorite_match)))
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(mIdlingResource)
        activityRule.activity.database.use {
            val empty = ""
            delete(Favorite.TABLE_FAVORITE, "(${Favorite.EVENT_ID} != {id})", "id" to empty)
        }
    }
}