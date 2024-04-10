package com.example.sportify.presentation.profile

import android.os.Bundle
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sportify.R
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.sportify.databinding.FragmentAccountEditBinding
import com.example.sportify.entity.User
import com.example.sportify.data.Service
import com.google.gson.Gson

class AccountInformationFragment: Fragment() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var _binding: FragmentAccountEditBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        bottomNavigationView.isVisible = false
        _binding = FragmentAccountEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.button.setOnClickListener {
            if (invalidate()) {
                Service.getUsersDataRef().child(Service.getCurrentUser()?.uid ?: "").setValue(
                    binding.run {
                        loadUserDataFromSharedPreferences()?.copy(
                            fullName = fullNameTv.text.toString(),
                            userName = userNameTv.text.toString(),
                            email = emailEditText.text.toString(),
                            phoneNumber = phoneNumber.text.toString(),
                            bio = bioEditText.text.toString()
                        )
                    }

                ).addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "You have successfully updated your personal info",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.run {
                        saveUserDataToSharedPreferences(
                            loadUserDataFromSharedPreferences()?.copy(
                                fullName = fullNameTv.text.toString(),
                                userName = userNameTv.text.toString(),
                                email = emailEditText.text.toString(),
                                phoneNumber = phoneNumber.text.toString(),
                                bio = bioEditText.text.toString()
                            ) ?: User()
                        )
                    }
                    fragmentManager?.popBackStack()
                }
            }
        }
    }

    private fun invalidate(): Boolean {
        binding.run {
            if (fullNameTv.text.isNullOrEmpty()
                || userNameTv.text.isNullOrEmpty()
                || phoneNumber.text.isNullOrEmpty()
                || emailEditText.text.isNullOrEmpty()
                || fullNameTv.text.isNullOrEmpty()
                || bioEditText.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "fill all fields",
                    Toast.LENGTH_SHORT,
                ).show()
                return false
            }
        }
        return true
    }

    private fun saveUserDataToSharedPreferences(user: User) {
        val gson = Gson()
        val userJsonString = gson.toJson(user)

        val sharedPreferences =
            requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_data", userJsonString)
        editor.apply()
    }

    private fun loadUserDataFromSharedPreferences(): User? {
        val sharedPreferences =
            requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val userJsonString = sharedPreferences.getString("user_data", null)

        if (userJsonString != null) {
            val gson = Gson()
            return gson.fromJson(userJsonString, User::class.java)
        } else {
            return null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView.isVisible = true
    }

    companion object {
        const val USER_DATA = "USER_DATA"
    }


}