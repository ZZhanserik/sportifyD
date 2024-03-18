package com.example.sportifyd

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import com.example.sportifyd.databinding.ActivitySplashBinding
import com.example.sportifyd.presentation.RegistrationFragment

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashView.setOnClickListener {
            val fragment = RegistrationFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()

            it.visibility = GONE
        }
    }

}