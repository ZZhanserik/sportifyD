package com.example.sportifyd.presentation

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sportifyd.MainActivity
import com.example.sportifyd.R
import com.example.sportifyd.databinding.FragmentHomeBinding
import com.example.sportifyd.databinding.FragmentRegistrationBinding
import com.example.sportifyd.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegistrationViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth


        binding.createButton.setOnClickListener {
            binding.run {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            //updateUI(user)

                            val userId = user?.uid
                            val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId ?: "")
                            val userData = User(
                                fullName = fullNameTv.text.toString(),
                                userName = userNameTv.text.toString(),
                                phoneNumber = phoneNumber.text.toString(),
                                email = email,
                                password = password
                            )
                            userRef.setValue(userData)

                            Toast.makeText(requireContext(), "Success Authentication", Toast.LENGTH_SHORT,).show()

                            val fragment = LoginFragment()

                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            //updateUI(null)
                        }
                    }
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