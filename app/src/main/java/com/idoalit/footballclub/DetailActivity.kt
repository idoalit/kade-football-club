package com.idoalit.footballclub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_activity.*

class DetailActivity : AppCompatActivity() {

    companion object {
        val ITEM_KEY = "item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        val item : Item = intent.getParcelableExtra(ITEM_KEY)
        name.text = item.name
        item.image.let { Picasso.get().load(it).resize(200, 200).centerInside().into(image) }
        desc.text = item.desc
    }

}
