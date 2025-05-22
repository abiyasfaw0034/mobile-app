package com.example.mobile.ui.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)

        // ✅ Setup ViewModels
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        // ✅ Load user name from sharedPref
        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)

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
