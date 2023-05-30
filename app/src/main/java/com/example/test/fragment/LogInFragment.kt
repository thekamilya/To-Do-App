package com.example.test.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.test.MainFragment
import com.example.test.R
import com.example.test.data.NOTE_NODE
import com.example.test.data.USER_NAME
import com.example.test.databinding.FragmentLogInBinding
import com.example.test.viewmodel.MyViewModel
import com.google.firebase.database.FirebaseDatabase

class LogInFragment : Fragment() {
    private val viewModel : MyViewModel by activityViewModels()
    lateinit var binding: FragmentLogInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLogInBinding.inflate(inflater, container , false)

        binding.login.setOnClickListener(){

            if (binding.emailEt.text.toString().isNotBlank() && binding.passwordEt.text.toString().isNotBlank()){
                viewModel.logIn(binding.emailEt.text.toString(),binding.passwordEt.text.toString()){ isSuccessful ->
                    if(isSuccessful){
                        USER_NAME =  binding.emailEt.text.toString()
                        viewModel.setDbName(binding.emailEt.text.toString())
                        val fragment = MainFragment.newInstance()
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_cont , fragment)
                            .commit()
                    }else{
                        Toast.makeText(requireContext(), "Wrong email or password" , Toast.LENGTH_SHORT).show()
                    }
                }
            }



        }

        binding.signUpLink.setOnClickListener(){
            val fragment = SignUpFragment.newInstance()
            parentFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_cont , fragment)
                .commit()
        }
        return binding.root

    }



    companion object {

        fun newInstance(): LogInFragment {
            return LogInFragment()
        }
    }
}
