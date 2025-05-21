package com.example.mobile.ui.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobile.databinding.FragmentRecordsBinding
import com.example.mobile.viewmodel.UserViewModel

class RecordsFragment : Fragment() {

    private var _binding: FragmentRecordsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        val root = binding.root

        // ✅ Instantiate ViewModel properly
        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // ✅ Read from shared preferences
        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)

        // ✅ Call instance method
        if (userId != -1) {
            val user = userViewModel.getUserById(userId)
            binding.tvWelcomeUser.text = "Welcome, ${user?.name ?: "User"}"
        }

        // ✅ Other viewmodel you already had
        val recordsViewModel = ViewModelProvider(this)[RecordsViewModel::class.java]

        val textView: TextView = binding.textHome
        recordsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}