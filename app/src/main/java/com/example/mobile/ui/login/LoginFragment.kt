package com.example.mobile.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobile.data.database.entities.User
import com.example.mobile.databinding.FragmentLoginBinding
import com.example.mobile.viewmodel.UserViewModel
import com.example.mobile.MainActivity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.mobile.R


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        requireActivity().title = "Login"
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // ✅ Auto-skip login if user already logged in
        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)
        if (userId != -1) {
            findNavController().navigate(com.example.mobile.R.id.navigation_records)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val user = userViewModel.login(email, password)

            if (user != null) {
                val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
                sharedPref.edit().putInt("user_id", user.id).apply()
                Toast.makeText(requireContext(), "Welcome ${user.name}", Toast.LENGTH_SHORT).show()

                // ✅ Start main activity
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.login_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
