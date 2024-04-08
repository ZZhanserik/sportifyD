package com.example.sportifyd.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sportifyd.R
import com.example.sportifyd.data.Service
import com.example.sportifyd.databinding.FragmentRegistrationBinding
import com.example.sportifyd.entity.User

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.createButton.setOnClickListener {
            binding.run {
                invalidate()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                Service.createUser(email, password)
                    .addOnSuccessListener {

                        val userData = User(
                            fullName = fullNameTv.text.toString(),
                            userName = userNameTv.text.toString(),
                            phoneNumber = phoneNumber.text.toString(),
                            email = email,
                            password = password
                        )
                        Service.createNewUserToDB(userData)

                        Toast.makeText(
                            requireContext(),
                            "Success Authentication",
                            Toast.LENGTH_SHORT
                        ).show()

                        val fragment = LoginFragment()

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit()

                    }
                    .addOnFailureListener {  // If sign in fails, display a message to the user.
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
            }
        }

        binding.loginPageButton.setOnClickListener {
            val fragment = LoginFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
    private fun invalidate() {
        binding.run{
            if (fullNameTv.text.isNullOrEmpty()
                || userNameTv.text.isNullOrEmpty()
                || phoneNumber.text.isNullOrEmpty()
                || emailEditText.text.isNullOrEmpty()
                || passwordEditText.text.isNullOrEmpty()
                || confirmPassword.text.isNullOrEmpty())
            {
                Toast.makeText(
                    requireContext(),
                    "fill all fields",
                    Toast.LENGTH_SHORT,
                ).show()
                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "REGISTRATION_FRAGMENT"
        fun newInstance() = RegistrationFragment()
    }
}