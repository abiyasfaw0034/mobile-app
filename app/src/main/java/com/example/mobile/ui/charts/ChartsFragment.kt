package com.example.mobile.ui.charts

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobile.databinding.FragmentChartsBinding
import com.example.mobile.viewmodel.TransactionViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.Month
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter

import java.util.*

class ChartsFragment : Fragment() {

    private var _binding: FragmentChartsBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionViewModel: TransactionViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartsBinding.inflate(inflater, container, false)

        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId != -1) {
            transactionViewModel.getAll(userId).observe(viewLifecycleOwner) { transactions ->
                setupPieChart(transactions)
                setupBarChart(transactions)
            }
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPieChart(transactions: List<com.example.mobile.data.database.entities.Transaction>) {
        val now = LocalDate.now()
        val currentMonthTransactions = transactions.filter {
            val date = Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()
            date.monthValue == now.monthValue && date.year == now.year
        }

        val totalIncome = currentMonthTransactions.filter { it.type == "income" }.sumOf { it.amount }
        val totalExpense = currentMonthTransactions.filter { it.type == "expense" }.sumOf { it.amount }
        val total = totalIncome + totalExpense

        val entries = listOf(
            PieEntry(totalIncome.toFloat(), "Income"),
            PieEntry(totalExpense.toFloat(), "Expense")
        )

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.colors = listOf(
            ContextCompat.getColor(requireContext(), android.R.color.holo_green_light),
            ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
        )
        pieDataSet.valueTextSize = 16f
        pieDataSet.valueTextColor = Color.LTGRAY
        pieDataSet.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val percent = if (total > 0) (value / total * 100) else 0f
                return String.format(Locale.getDefault(), "%.0f%%", percent)
            }
        }

        val pieData = PieData(pieDataSet)

        binding.pieChart.data = pieData
        binding.pieChart.setEntryLabelColor(Color.LTGRAY)
        binding.pieChart.setEntryLabelTextSize(12f)
        binding.pieChart.description = Description().apply { text = "" }
        binding.pieChart.centerText = now.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        binding.pieChart.setCenterTextColor(Color.WHITE)
        binding.pieChart.animateY(1000)
        binding.pieChart.invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupBarChart(transactions: List<com.example.mobile.data.database.entities.Transaction>) {
        // Group transactions by "Year-Month"
        val grouped = transactions.groupBy {
            val date = Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()
            "${date.year}-${date.monthValue}"
        }
        val sortedKeys = grouped.keys.sorted()

        val incomeEntries = mutableListOf<BarEntry>()
        val expenseEntries = mutableListOf<BarEntry>()
        val monthLabels = mutableListOf<String>()

        sortedKeys.forEachIndexed { index, key ->
            val income = grouped[key]!!.filter { it.type == "income" }.sumOf { it.amount }
            val expense = grouped[key]!!.filter { it.type == "expense" }.sumOf { it.amount }
            val date = key.split("-")
            val label = Month.of(date[1].toInt()).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            monthLabels.add(label)

            incomeEntries.add(BarEntry(index.toFloat(), income.toFloat()) )
            expenseEntries.add(BarEntry(index.toFloat(), expense.toFloat()) )
        }


        val incomeSet = BarDataSet(incomeEntries, "Income")
        incomeSet.color = ContextCompat.getColor(requireContext(), android.R.color.holo_green_light)
        incomeSet.valueTextColor = Color.LTGRAY
        incomeSet.valueTextSize = 12f

        val expenseSet = BarDataSet(expenseEntries, "Expense")
        expenseSet.color = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
        expenseSet.valueTextColor = Color.LTGRAY
        expenseSet.valueTextSize = 12f

        val barData = BarData(incomeSet, expenseSet)
        barData.barWidth = 0.35f

        binding.barChart.data = barData
        binding.barChart.description.isEnabled = false
        binding.barChart.setFitBars(true)
        binding.barChart.setExtraOffsets(0f, 0f, 0f, 24f)

        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(monthLabels)
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textColor = Color.LTGRAY
        xAxis.labelRotationAngle = -45f

        binding.barChart.axisLeft.textColor = Color.LTGRAY
        binding.barChart.axisRight.isEnabled = false
        binding.barChart.legend.textColor = Color.LTGRAY

        // To group bars side by side
        val groupSpace = 0.1f
        val barSpace = 0f
        barData.barWidth = 0.35f

        binding.barChart.xAxis.axisMinimum = -0.5f
        binding.barChart.xAxis.axisMaximum = sortedKeys.size.toFloat()
        binding.barChart.groupBars(0f, groupSpace, barSpace)

        binding.pieChart.legend.textColor = Color.LTGRAY

        binding.barChart.animateY(1000)
        binding.barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
