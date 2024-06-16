package com.capstone.kusaku.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kusaku.R
import com.capstone.kusaku.data.remote.response.ExpenseItem
import com.capstone.kusaku.utils.DateHelper
import com.capstone.kusaku.utils.RupiahFormatter
import java.util.Locale

class ExpenseHistoryAdapter : RecyclerView.Adapter<ExpenseHistoryAdapter.ViewHolder>() {

    private var expenseList = listOf<ExpenseItem>()

    fun setData(expenses: List<ExpenseItem>) {
        expenseList = expenses
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense_history, parent, false)
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
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(expenseItem: ExpenseItem?) {
            if (expenseItem != null) {
                categoryTextView.text = capitalizeWords(expenseItem.describe)
                amountTextView.text = RupiahFormatter.format(expenseItem.price.toLong())
                dateTextView.text = DateHelper.convertTimestampToDate(expenseItem.timestamp.toLong())
                imageView.setImageResource(getItemIcon(expenseItem.category))
            } else {
                // Tangani kasus null (misalnya, tampilkan placeholder atau pesan kesalahan)
            }
        }
    }

    private fun getItemIcon(category: String): Int {
        when (capitalizeWords(category)) {
            "Food" -> return R.drawable.food_icon
            "Transportation" -> return R.drawable.transportation_icon
            "Apparel" -> return R.drawable.apparel_icon
            "Household" -> return R.drawable.household_icon
            "Social Life" -> return R.drawable.social_life_icon
            "Education" -> return R.drawable.education_icon
            "Entertainment" -> return R.drawable.entertainment_icon
            "Health" -> return R.drawable.health_icon
        }
        return R.drawable.other_icon
    }

    fun capitalizeWords(input: String): String {
        return input.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }
}