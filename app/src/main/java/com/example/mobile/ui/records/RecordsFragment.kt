package com.example.mobile.ui.records

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile.databinding.FragmentRecordsBinding
import com.example.mobile.viewmodel.UserViewModel
import com.example.mobile.viewmodel.TransactionViewModel

class RecordsFragment : Fragment() {

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)

        // ✅ Load user name from sharedPref
        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)

        // ✅ Setup ViewModels
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        // Inside onCreateView after loading transactions

        transactionViewModel.getAll(userId).observe(viewLifecycleOwner) { transactions ->
            transactionAdapter = TransactionAdapter(transactions)
            binding.recyclerTransactions.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = transactionAdapter
            }

            // 🧮 Calculate expenses, income, balance
            val expenses = transactions.filter { it.amount < 0 }.sumOf { it.amount }
            val income = transactions.filter { it.amount > 0 }.sumOf { it.amount }
            val balance = income + expenses

            binding.tvExpenses.text = "₹${-expenses}"
            binding.tvIncome.text = "₹$income"
            binding.tvBalance.text = "₹$balance"
        }

// Optional: Set current month
        val currentMonth = java.time.LocalDate.now().month.name.lowercase().replaceFirstChar { it.uppercase() }
        binding.tvMonthFilter.text = currentMonth




        if (userId != -1) {
            val user = userViewModel.getUserById(userId)
            binding.tvWelcomeUser.text = "Welcome, ${user?.name ?: "User"}"

            // ✅ Load transactions for the user
            transactionViewModel.getAll(userId).observe(viewLifecycleOwner) { transactions ->
                transactionAdapter = TransactionAdapter(transactions)
                binding.recyclerTransactions.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = transactionAdapter
                }
            }
        } else {
            binding.tvWelcomeUser.text = "Welcome!"
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
