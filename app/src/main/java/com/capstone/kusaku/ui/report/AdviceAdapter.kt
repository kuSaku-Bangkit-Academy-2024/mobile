package com.capstone.kusaku.ui.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kusaku.R

class AdviceAdapter (private val adviceList: List<String>) :
    RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder>() {

    inner class AdviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAdviceContent: TextView = itemView.findViewById(R.id.tv_item_advices)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_advices, parent, false)
        return AdviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdviceViewHolder, position: Int) {
        holder.tvAdviceContent.text = adviceList[position]
    }

    override fun getItemCount(): Int = adviceList.size
}