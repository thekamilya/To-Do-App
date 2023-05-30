package com.example.test.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.media.audiofx.Equalizer.Settings
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.test.R
import com.example.test.data.USER_NAME
import com.example.test.databinding.FragmentSettingsBinding
import com.example.test.databinding.FragmentStatisticsBinding
import com.example.test.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {
    val viewModel: MyViewModel by activityViewModels()
    lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater , container , false)



        binding.backBtn.setOnClickListener(){
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.logOut.setOnClickListener(){
           showOnLogOutDialog()
        }

        binding.userGmail.text = "KKK"

        return binding.root
    }

    fun showOnLogOutDialog(){
        val listener = DialogInterface.OnClickListener { _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {FirebaseAuth.getInstance().signOut()
                    val fragment = LogInFragment.newInstance()
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_cont ,fragment)
                        .commit()}
                DialogInterface.BUTTON_NEGATIVE -> Unit
            }


        }
        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setMessage("Do you really want to log out?")
            .setPositiveButton("Yes", listener)
            .setNegativeButton("No", listener)
            .create()

        dialog.show()

    }

    companion object {
        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()
            return fragment
        }
    }
}