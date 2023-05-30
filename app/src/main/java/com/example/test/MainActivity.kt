package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.example.test.databinding.ActivityMainBinding
import com.example.test.fragment.LogInFragment
import com.example.test.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is already signed in, proceed to the main screen
            // or any other relevant logic
            currentUser.email?.let { viewModel.setDbName(it) }
            val fragment = MainFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_cont, fragment).commit()
        } else {
            // User is not signed in, show the sign-in UI
            val fragment = LogInFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_cont, fragment).commit()
        }


    }
}