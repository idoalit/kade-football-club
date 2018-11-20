package com.idoalit.footballclub

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContext

class ClubAdapter(private val list: List<Item>, private val listener : (Item) -> Unit)
    : RecyclerView.Adapter<ClubAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(ItemUI().createView(AnkoContext.create(p0.context, p0)))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindView(list[p1], listener)
    }

    class ViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
        val image : ImageView = view.findViewById(ItemUI.imageID)
        val name : TextView = view.findViewById(ItemUI.nameID)

        fun bindView(item: Item, listener: (Item) -> Unit) {
            item.image?.let { Picasso.get().load(it).resize(100, 100).centerInside()
                .into(image) }
            name.text = item.name

            view.setOnClickListener {
                listener(item)
            }
        }
    }
}