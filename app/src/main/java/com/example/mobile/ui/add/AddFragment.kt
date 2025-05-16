package com.example.mobile.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mobile.R
import com.example.mobile.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    // View Binding
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example usage of ViewModel
        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textAdd.text = it
        })

        // TODO: add your logic here (button clicks, form input, etc.)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
