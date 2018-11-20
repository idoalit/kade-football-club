package com.idoalit.footballclub

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import org.jetbrains.anko.*

class ItemUI : AnkoComponent<ViewGroup> {

    companion object {
        val imageID = 1
        val nameID = 2
    }

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            padding = dip(16)
            orientation = LinearLayout.HORIZONTAL

            imageView {
                id = imageID
            }.lparams {
                width = dip(50)
                height = dip(50)
            }

            textView {
                id = nameID
            }.lparams {
                width = wrapContent
                height = wrapContent
                margin = dip(10)
                gravity = Gravity.CENTER_VERTICAL
            }

        }
    }
}