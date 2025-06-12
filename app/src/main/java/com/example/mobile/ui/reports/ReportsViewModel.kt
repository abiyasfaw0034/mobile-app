package com.example.mobile.ui.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobile.data.database.entities.Transaction

class ReportsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    fun generateReport(transactions: List<Transaction>) {
        val totalIncome = transactions.filter { it.type.lowercase() == "income" }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type.lowercase() == "expense" }.sumOf { it.amount }
        val balance = totalIncome - totalExpense

        val report = """
            🧾 Monthly Financial Summary

            ✅ Total Income: $totalIncome
            ❌ Total Expenses: $totalExpense
            💰 Balance: $balance

            ✅ Transactions: ${transactions.size}
        """.trimIndent()

        _text.value = report
    }
}
