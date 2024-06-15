package com.capstone.kusaku.ui.report

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kusaku.R
import com.capstone.kusaku.databinding.FragmentReportBinding
import com.capstone.kusaku.ui.home.ExpenseHistoryAdapter
import com.capstone.kusaku.ui.home.ExpenseItem
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.text.NumberFormat
import java.util.Locale

class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var adviceAdapter: AdviceAdapter
    private lateinit var rvAdvices: RecyclerView
    private lateinit var expenseHistoryAdapter: ExpenseHistoryAdapter
    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val adviceList = getAdviceList()
        rvAdvices = binding.rvAdvices
        adviceAdapter = AdviceAdapter(adviceList)
        rvAdvices.layoutManager = LinearLayoutManager(requireContext())
        rvAdvices.adapter = adviceAdapter
        expenseHistoryAdapter = ExpenseHistoryAdapter()

        // Set data palsu ke adapter
        val reportExpenseList = getReportExpenseData()
//        expenseHistoryAdapter.setData(reportExpenseList)

        pieChart = binding.pieChartViewReport
        initPieChart()
        showPieChart(reportExpenseList)

        return root
    }

    private fun getAdviceList(): List<String> {
        return listOf(
            "Advice 1",
            "Advice 2",
            "Advice 3"
        )
    }

    private fun getReportExpenseData(): List<ExpenseItem> {
        return listOf(
            ExpenseItem("Food", "25.000", "2024-06-10"),
            ExpenseItem("Transportation", "30.000", "2024-06-11"),
            ExpenseItem("Shopping", "250.000", "2024-06-12"),
            ExpenseItem("Education", "500.000", "2024-06-14"),
            ExpenseItem("Sport", "100.000", "2024-06-15"),
            ExpenseItem("Internet", "200.000", "2024-06-16"),
        )
    }

    private fun calculateTotalExpense(expenseList: List<ExpenseItem>): Float {
        var totalExpense = 0f
        for (expenseItem in expenseList) {
            val amount = expenseItem.amount.replace(".", "").toFloatOrNull() ?: 0f
            totalExpense += amount
        }
        return totalExpense
    }

    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = true
        pieChart.dragDecelerationFrictionCoef = 0.9f
        pieChart.rotationAngle = 0f
        pieChart.isHighlightPerTapEnabled = true
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)
        pieChart.isDrawHoleEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.isEnabled = false
    }

    private fun showPieChart(expenseList: List<ExpenseItem>) {
        val pieEntries = ArrayList<PieEntry>()
        val typeAmountMap = mutableMapOf<String, Float>()
        for (expenseItem in expenseList) {
            val amount = expenseItem.amount.replace(".", "").toFloatOrNull() ?: 0f
            if (typeAmountMap.containsKey(expenseItem.category)) {
                typeAmountMap[expenseItem.category] = typeAmountMap[expenseItem.category]!! + amount
            } else {
                typeAmountMap[expenseItem.category] = amount
            }
        }

        val totalExpenseTextView: TextView = binding.tvTotalExpense
        val totalExpense = calculateTotalExpense(expenseList)
        val formattedTotalExpense = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(totalExpense)
        totalExpenseTextView.text = formattedTotalExpense

        for (entry in typeAmountMap.entries) {
            pieEntries.add(PieEntry(entry.value, entry.key))
        }

        val colors = getChartColors()
        val pieDataSet = PieDataSet(pieEntries, "type")
        pieDataSet.colors = colors
        pieDataSet.valueTextSize = 12f
        pieDataSet.setDrawValues(true)

        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setDrawValues(true)

        pieChart.data = pieData
        pieChart.invalidate()

        createCustomLegend(typeAmountMap, colors)
    }

    private fun createCustomLegend(typeAmountMap: Map<String, Float>, colors: List<Int>) {
        val legendLayout = binding.legendLayoutReport
        legendLayout.removeAllViews()

        val sortedEntries = typeAmountMap.entries.sortedByDescending { it.value }

        for ((index, entry) in sortedEntries.withIndex()) {
            val legendItem = layoutInflater.inflate(R.layout.legend_item, legendLayout, false)
            legendItem.findViewById<View>(R.id.legend_color).setBackgroundColor(colors[index])
            val legendLabel = legendItem.findViewById<TextView>(R.id.legend_label)
            legendLabel.text = entry.key
            legendLabel.setTextColor(Color.WHITE)
            legendLayout.addView(legendItem)
        }
    }

    private fun getChartColors(): ArrayList<Int> {
        val colors = ArrayList<Int>()
        colors.add(Color.rgb(255, 102, 102)) // Merah muda
        colors.add(Color.rgb(255, 204, 102)) // Kuning muda
        colors.add(Color.rgb(153, 204, 255)) // Biru muda
        colors.add(Color.rgb(255, 153, 204)) // Ungu muda
        colors.add(Color.rgb(102, 255, 178)) // Hijau muda
        colors.add(Color.rgb(255, 153, 0))   // Oranye
        colors.add(Color.rgb(153, 153, 255)) // Ungu
        colors.add(Color.rgb(255, 153, 255)) // Pink
        colors.add(Color.rgb(0, 204, 153))   // Hijau tosca
        colors.add(Color.rgb(204, 0, 204))   // Ungu tua
        return colors
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
