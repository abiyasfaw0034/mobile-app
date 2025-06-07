package com.example.mobile.ui.records

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile.databinding.FragmentRecordsBinding
import com.example.mobile.viewmodel.TransactionViewModel
import com.example.mobile.viewmodel.UserViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*

class RecordsFragment : Fragment() {

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    private var selectedMonth: Int = LocalDate.now().monthValue
    private var selectedYear: Int = LocalDate.now().year

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)

        // ✅ Setup ViewModels
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        // ✅ Load user ID
        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)

        // ✅ Show user name
        if (userId != -1) {
            val user = userViewModel.getUserById(userId)
            binding.tvWelcomeUser.text = "Welcome, ${user?.name ?: "User"}"
        } else {
            binding.tvWelcomeUser.text = "Welcome!"
        }

        // ✅ Setup Month filter
        val currentMonthName = Month.of(selectedMonth).name.lowercase().replaceFirstChar { it.uppercase() }
        binding.tvMonthFilter.text = "$currentMonthName $selectedYear"

        // START MODIFICATION
        // Replace 'binding.tvMonthFilter.setOnClickListener' with 'binding.cardFilter.setOnClickListener'
        binding.cardFilter.setOnClickListener { // <--- MODIFIED LINE
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)

            val datePicker = DatePickerDialog(requireContext(),
                { _, pickedYear, pickedMonthIndex, _ ->
                    selectedMonth = pickedMonthIndex + 1
                    selectedYear = pickedYear

                    val selectedMonthName = Month.of(selectedMonth).name.lowercase().replaceFirstChar { it.uppercase() }
                    binding.tvMonthFilter.text = "$selectedMonthName $selectedYear"

                    filterTransactionsByMonth()
                },
                year, month, 1
            )
            // Hide day picker
            datePicker.datePicker.findViewById<View>(
                resources.getIdentifier("day", "id", "android")
            )?.visibility = View.GONE
            datePicker.show()
        }
        // END MODIFICATION

        // ✅ Load first time
        filterTransactionsByMonth()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterTransactionsByMonth() {
        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) return

        transactionViewModel.getAll(userId).observe(viewLifecycleOwner) { transactions ->
            val filtered = transactions.filter {
                val date = Instant.ofEpochMilli(it.date)
                    .atZone(ZoneId.systemDefault()).toLocalDate()
                date.monthValue == selectedMonth && date.year == selectedYear
            }

            Log.d("RecordsFragment", "Filtered transactions count: ${filtered.size}")

            transactionAdapter = TransactionAdapter(filtered)
            binding.recyclerTransactions.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = transactionAdapter
            }

            val expenses = filtered.filter { it.type == "expense" }.sumOf { it.amount }
            val income = filtered.filter { it.type == "income" }.sumOf { it.amount }
            val balance = income - expenses

            binding.tvExpenses.text = "₹${expenses}"
            binding.tvIncome.text = "₹${income}"
            binding.tvBalance.text = "₹${balance}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}