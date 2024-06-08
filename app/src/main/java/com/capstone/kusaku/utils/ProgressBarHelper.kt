package com.capstone.kusaku.utils

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kusaku.R

class ProgressBarHelper(private val activity: AppCompatActivity) {

    private var progressBarView: View? = null

    fun show() {
        if (progressBarView == null) {
            val inflater = LayoutInflater.from(activity)
            progressBarView = inflater.inflate(R.layout.progress_bar, activity.findViewById(android.R.id.content), false)
            activity.addContentView(progressBarView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
            )
        }
        progressBarView?.visibility = View.VISIBLE
    }

    fun hide() {
        progressBarView?.visibility = View.GONE
    }
}