package com.example.mobile.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobile.MainActivity
import com.example.mobile.data.database.entities.User
import com.example.mobile.databinding.FragmentRegisterBinding
import com.example.mobile.viewmodel.UserViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        requireActivity().title = "Register"
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)

        binding.btnRegister.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val newUser = User(name = name, email = email, password = password)
                userViewModel.register(newUser)

                Handler(Looper.getMainLooper()).postDelayed({
                    val registeredUser = userViewModel.login(email, password)
                    if (registeredUser != null) {
                        sharedPref.edit().putInt("user_id", registeredUser.id).apply()
                        Toast.makeText(requireContext(), "Welcome ${registeredUser.name}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Failed to log in after registration", Toast.LENGTH_SHORT).show()
                    }
                }, 800)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack() // go back to login
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
