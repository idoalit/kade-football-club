package com.idoalit.footballmatchschedule

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.idoalit.footballmatchschedule.screens.AboutFragment
import com.idoalit.footballmatchschedule.screens.EventFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_last -> {
                addFragment(EventFragment.newInstance(EventFragment.LAST_EVENT))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_next -> {
                addFragment(EventFragment.newInstance(EventFragment.NEXT_EVENT))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_about -> {
                addFragment(AboutFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // initial event fragment
         addFragment(EventFragment.newInstance(EventFragment.LAST_EVENT))
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.fragment, fragment, fragment.javaClass.simpleName)
            .commit()
    }
}
