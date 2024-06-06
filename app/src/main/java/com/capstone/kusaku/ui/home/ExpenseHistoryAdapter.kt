package com.capstone.kusaku.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kusaku.R

class ExpenseHistoryAdapter : RecyclerView.Adapter<ExpenseHistoryAdapter.ViewHolder>() {

    private var expenseList = listOf<ExpenseItem>()

    fun setData(expenses: List<ExpenseItem>) {
        expenseList = expenses
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expenseItem = expenseList[position]
        holder.bind(expenseItem)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTextView: TextView = itemView.findViewById(R.id.tv_category)
        val amountTextView: TextView = itemView.findViewById(R.id.tv_amount)
        val dateTextView: TextView = itemView.findViewById(R.id.tv_date)

        fun bind(expenseItem: ExpenseItem?) {
            if (expenseItem != null) {
                categoryTextView.text = expenseItem.category
                amountTextView.text = expenseItem.amount
                dateTextView.text = expenseItem.date
            } else {
                // Tangani kasus null (misalnya, tampilkan placeholder atau pesan kesalahan)
            }
        }
    }
}