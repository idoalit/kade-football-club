package com.idoalit.footballclub

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class MainActivity : AppCompatActivity() {

    private val items: MutableList<Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI().setContentView(this)

        initData()

        val list = findViewById<RecyclerView>(MainActivityUI.listId)
        list.adapter = ClubAdapter(items) {
            startActivity(intentFor<DetailActivity>(DetailActivity.ITEM_KEY to it))
        }
    }

    fun initData() {
        val name = resources.getStringArray(R.array.club_name)
        val image = resources.obtainTypedArray(R.array.club_image)
        val desc = resources.getStringArray(R.array.club_desc)
        items.clear()
        for (i in name.indices) {
            items.add(Item(name[i], image.getResourceId(i, 0), desc[i]))
        }

        //Recycle the typed array
        image.recycle()
    }

    class MainActivityUI : AnkoComponent<MainActivity> {
        companion object {
            val listId = 1
        }
        override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                recyclerView {
                    id = listId
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }

    }
}
