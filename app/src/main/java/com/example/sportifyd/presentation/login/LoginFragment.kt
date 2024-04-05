package com.example.sportifyd.presentation.login

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sportifyd.MainActivity
import com.example.sportifyd.R
import com.example.sportifyd.databinding.FragmentLoginBinding
import com.example.sportifyd.presentation.pin.PinCodeFragment
import com.example.sportifyd.presentation.registration.RegistrationFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth
        binding.run {
            registrationPageButton.setOnClickListener {
                val fragment = RegistrationFragment()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            createButton.setOnClickListener {
                if (emailEditText.text.isNullOrEmpty()) return@setOnClickListener
                if (passwordEditText.text.isNullOrEmpty()) return@setOnClickListener

                auth.signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {

                            saveLoginState(true, auth.currentUser?.uid.orEmpty())
                            val fragment = PinCodeFragment()

                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()
                        }
                }
            }

        }
    }

    private fun saveLoginState(isLoggedIn: Boolean, userId: String) {
        val sharedPref =
            activity?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.is_logged_in_key), isLoggedIn)
            putString(getString(R.string.user_id), userId)
            apply()
        }
    }

}