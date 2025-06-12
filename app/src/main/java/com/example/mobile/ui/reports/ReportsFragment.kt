package com.example.mobile.ui.reports

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobile.databinding.FragmentReportsBinding
import com.example.mobile.viewmodel.TransactionViewModel
import java.text.DecimalFormat

class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId != -1) {
            transactionViewModel.getAll(userId).observe(viewLifecycleOwner) { transactions ->
                val expenses = transactions.filter { it.type == "expense" }
                val income = transactions.filter { it.type == "income" }

                val totalExpense = expenses.sumOf { it.amount }
                val totalIncome = income.sumOf { it.amount }
                val balance = totalIncome - totalExpense

                val mostUsedCategory = expenses.groupBy { it.category }
                    .maxByOrNull { it.value.size }?.key ?: "N/A"

                val largestExpense = expenses.maxByOrNull { it.amount }?.amount ?: 0.0
                val largestIncome = income.maxByOrNull { it.amount }?.amount ?: 0.0

                val avgExpense = if (expenses.isNotEmpty()) expenses.sumOf { it.amount } / expenses.size else 0.0
                val avgIncome = if (income.isNotEmpty()) income.sumOf { it.amount } / income.size else 0.0

                val df = DecimalFormat("#,##0.00")
                val categoryMap = expenses.groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }

                val reportText = StringBuilder()
                reportText.appendLine("💰 Total Income: ₹${df.format(totalIncome)}")
                reportText.appendLine("💸 Total Expense: ₹${df.format(totalExpense)}")
                reportText.appendLine("📊 Balance: ₹${df.format(balance)}")
                reportText.appendLine()
                reportText.appendLine("⭐ Most Used Category: $mostUsedCategory")
                reportText.appendLine("🔺 Biggest Income: ₹${df.format(largestIncome)}")
                reportText.appendLine("🔻 Biggest Expense: ₹${df.format(largestExpense)}")
                reportText.appendLine("📈 Avg Income: ₹${df.format(avgIncome)}")
                reportText.appendLine("📉 Avg Expense: ₹${df.format(avgExpense)}")
                reportText.appendLine()
                reportText.appendLine("📂 Category Breakdown:")

                if (categoryMap.isNotEmpty()) {
                    categoryMap.forEach { (category, total) ->
                        reportText.appendLine("• $category: ₹${df.format(total)}")
                    }
                } else {
                    reportText.appendLine("No expense categories.")
                }

                binding.textReports.text = reportText.toString()
                binding.textReports.setTextColor(Color.LTGRAY)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
