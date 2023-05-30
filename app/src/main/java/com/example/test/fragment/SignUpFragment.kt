package com.example.test.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.test.databinding.FragmentSignUpBinding
import com.example.test.viewmodel.MyViewModel

class SignUpFragment : Fragment() {
    val viewModel: MyViewModel by activityViewModels()
    lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.loginLink.setOnClickListener() {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.signUp.setOnClickListener() {
            if (binding.emailEt.text.toString().isNotBlank() && binding.passwordEt.text.toString()
                    .isNotBlank()
            ) {
               var result =viewModel.addUser(binding.emailEt.text.toString()
                   ,binding.passwordEt.text.toString()){result ->
                   Toast.makeText(requireContext(), result , Toast.LENGTH_SHORT).show()
               }

            }

        }
        return binding.root
    }



    companion object {

        fun newInstance(): SignUpFragment {
            return SignUpFragment()
        }
    }
}