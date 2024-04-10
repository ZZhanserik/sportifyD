package com.example.sportify

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import com.example.sportify.databinding.ActivitySplashBinding
import com.example.sportify.presentation.LoginFragment
import com.example.sportify.presentation.PinCodeFragment

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashView.setOnClickListener {
            if (isLoggedIn()) {
                val fragment = PinCodeFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                val fragment = LoginFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            it.visibility = GONE
        }
    }

    private fun isLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE) ?: return false
        return sharedPref.getBoolean(getString(R.string.is_logged_in_key), false)
    }

}