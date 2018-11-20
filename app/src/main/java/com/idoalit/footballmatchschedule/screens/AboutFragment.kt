package com.idoalit.footballmatchschedule.screens


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.textView


class AboutFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return UI {
            linearLayout {
                gravity = Gravity.CENTER

                textView {
                    text = "Football Match Schedule" + "\n" + "Kelas Kotlin Android Developer Expert" + "\n" + "v.0.0.1"
                    this.gravity = Gravity.CENTER
                }
            }
        }.view
    }


}
