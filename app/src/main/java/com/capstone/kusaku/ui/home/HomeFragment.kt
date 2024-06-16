package com.capstone.kusaku.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kusaku.R
import com.capstone.kusaku.data.remote.response.ExpenseItemAggregate
import com.capstone.kusaku.databinding.FragmentHomeBinding
import com.capstone.kusaku.ui.ViewModelFactory
import com.capstone.kusaku.ui.main.MainActivity
import com.capstone.kusaku.utils.ProgressBarHelper
import com.capstone.kusaku.utils.RupiahFormatter
import com.capstone.kusaku.utils.Status
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvExpenseHistory: RecyclerView
    private lateinit var expenseHistoryAdapter: ExpenseHistoryAdapter
    private lateinit var pieChart: PieChart
    private lateinit var progressBarHelper: ProgressBarHelper

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        progressBarHelper = ProgressBarHelper(requireActivity() as MainActivity)

        viewModel.userName.observe(viewLifecycleOwner) {
            binding.tvGreeting.text = "Hello, $it"
        }

        viewModel.income.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvIncome.text = RupiahFormatter.format(it.toLong())
            }
        }

        viewModel.getTotalExpensesByCategory().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let { data ->
                        val expenses = data.expenses
                        pieChart = binding.pieChartView
                        initPieChart()
                        showPieChart(expenses)
                    }
                    progressBarHelper.hide()
                }
                Status.ERROR -> {
                    progressBarHelper.hide()
                }
                Status.LOADING -> {
                    progressBarHelper.show()
                }
            }
        }

        rvExpenseHistory = binding.rvExpenseHistory
        expenseHistoryAdapter = ExpenseHistoryAdapter()
        rvExpenseHistory.layoutManager = LinearLayoutManager(requireContext())
        rvExpenseHistory.adapter = expenseHistoryAdapter
        viewModel.getExpensesHistory().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let { data ->
                        val expenses = data.expenses
                        (rvExpenseHistory.adapter as? ExpenseHistoryAdapter)?.setData(expenses)
                    }
                    progressBarHelper.hide()
                }
                Status.ERROR -> {
                    progressBarHelper.hide()
                }
                Status.LOADING -> {
                    progressBarHelper.show()
                }
            }
        }

        return root
    }

    private fun calculateTotalExpense(expenseList: List<ExpenseItemAggregate>): Float {
        return expenseList.sumOf { it.totalExpense.toDouble() }.toFloat()
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

    private fun showPieChart(expenseList: List<ExpenseItemAggregate>) {
        val pieEntries = ArrayList<PieEntry>()
        val typeAmountMap = mutableMapOf<String, Float>()
        for (expenseItem in expenseList) {
            val amount = expenseItem.totalExpense.toFloat()
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
        val legendLayout = binding.legendLayout
        legendLayout.removeAllViews()

        val sortedEntries = typeAmountMap.entries.sortedByDescending { it.value }

        for ((index, entry) in sortedEntries.withIndex()) {
            val legendItem = layoutInflater.inflate(R.layout.legend_item, legendLayout, false)
            legendItem.findViewById<View>(R.id.legend_color).setBackgroundColor(colors[index])
            legendItem.findViewById<TextView>(R.id.legend_label).text = entry.key
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