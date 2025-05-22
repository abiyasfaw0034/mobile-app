package com.example.mobile.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobile.databinding.FragmentAddBinding
import com.example.mobile.data.database.entities.Transaction
import com.example.mobile.data.database.entities.Category
import com.example.mobile.util.CategorySeeder
import com.example.mobile.viewmodel.CategoryViewModel
import com.example.mobile.viewmodel.TransactionViewModel

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var categoryViewModel: CategoryViewModel

    private var selectedType = "expense"
    private var selectedCategory: Category? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        CategorySeeder.seed(requireContext())

        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)

        // Type selector
        binding.radioGroupType.setOnCheckedChangeListener { _, checkedId ->
            selectedType = if (checkedId == binding.radioIncome.id) "income" else "expense"
            loadCategories()
        }

        // Load default type on start
        loadCategories()

        binding.btnSave.setOnClickListener {
            val amountText = binding.editAmount.text.toString().trim()
            val note = binding.editNote.text.toString().trim()

            if (amountText.isEmpty() || selectedCategory == null) {
                Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transaction = Transaction(
                userId = userId,
                type = selectedType,
                amount = amountText.toDouble(),
                category = selectedCategory!!.name,
                date = System.currentTimeMillis(),
                note = note
            )

            transactionViewModel.insert(transaction)
            Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_SHORT).show()
            clearForm()
        }

        return binding.root
    }

    private fun loadCategories() {
        categoryViewModel.getAllCategories().observe(viewLifecycleOwner) { categories ->
            val categoryNames = categories.map { it.name }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter

            binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    selectedCategory = categories[pos]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    private fun clearForm() {
        binding.editAmount.text?.clear()
        binding.editNote.text?.clear()
        binding.radioExpense.isChecked = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
