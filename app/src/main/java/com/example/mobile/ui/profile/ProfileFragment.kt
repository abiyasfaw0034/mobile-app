package com.example.mobile.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobile.databinding.FragmentProfileBinding
import com.example.mobile.LoginActivity
import com.example.mobile.viewmodel.UserViewModel // IMPORT UserViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel // DECLARE UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // You won't typically need ProfileViewModel for a standard profile page
        // as user data comes from UserViewModel.
        // If ProfileViewModel has other specific data, keep it.
        // For this example, I'm assuming user data is handled by UserViewModel.
        // val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // INITIALIZE UserViewModel
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // --- Fetch and Display User Information ---
        val sharedPref = requireActivity().getSharedPreferences("app_pref", 0)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId != -1) {
            val user = userViewModel.getUserById(userId)
            user?.let {
                binding.tvUserName.text = it.name // Update based on new XML ID
                binding.tvUserEmail.text = it.email // Update based on new XML ID
                // You can add more user details here if needed (e.g., binding.tvUserPhone.text = it.phone)
            } ?: run {
                // Handle case where user is not found
                binding.tvUserName.text = "User Not Found"
                binding.tvUserEmail.text = "N/A"
            }
        } else {
            // Handle case where user ID is not found in SharedPreferences
            binding.tvUserName.text = "Guest User"
            binding.tvUserEmail.text = "Please log in"
        }

        // --- Logout logic ---
        binding.btnLogout.setOnClickListener {
            // Clear user session
            sharedPref.edit().clear().apply()

            // Navigate to LoginActivity and finish current activity stack
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Removed the original 'text_notifications' TextView and its observer
        // as it's not typically part of a static profile display.
        // If you need it for notifications, you can re-add it with a new purpose.

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}