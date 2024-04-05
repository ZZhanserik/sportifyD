package com.example.sportify.presentation.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.sportify.R
import com.example.sportify.SplashActivity
import com.example.sportify.data.Service
import com.example.sportify.databinding.FragmentProfileBinding
import com.example.sportify.entity.User
import com.example.sportifyd.R
import com.example.sportifyd.SplashActivity
import com.example.sportifyd.databinding.FragmentProfileBinding
import com.example.sportifyd.entity.User
import com.example.sportifyd.presentation.profile.ProfileViewModel
import com.google.gson.Gson

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var sharedPreferences: SharedPreferences? = null
    private var imageUri: Uri? = null
    private var isImageAdded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedPreferences = context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val navController = findNavController()
        binding.accountInformation.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_accountInformationFragment)
        }

        binding.logoutButton.setOnClickListener {
            sharedPreferences?.edit()?.clear()?.apply()

            val intent = Intent(requireActivity(), SplashActivity::class.java)
            startActivity(intent)
        }

        if (loadUserDataFromSharedPreferences()?.photo != null) {
            setPhotoProfile()
        }

        binding.imageProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.data
            isImageAdded = true
            setPhotoProfile(imageUri)

            Service.getUserPhotoStorageRef().child(Service.getCurrentUser()?.uid.orEmpty() + ".jpg")
                .putFile(imageUri!!)
                .addOnSuccessListener {

                    Service.getUserPhotoStorageRef()
                        .child(Service.getCurrentUser()?.uid.orEmpty() + ".jpg").downloadUrl.addOnSuccessListener {

                            Service.getUsersDataRef().child(Service.getCurrentUser()?.uid.orEmpty())
                                .child("photo").setValue(it.toString())

                        }
                }
        }
    }

    override fun onStop() {
        if (isImageAdded && imageUri != null) {
            saveUserDataToSharedPreferences(
                loadUserDataFromSharedPreferences()?.copy(photo = imageUri.toString())!!
            )
            imageUri = null
            isImageAdded = false
        }
        super.onStop()
    }


    private fun setPhotoProfile(uri: Uri? = null) {
        binding.run {
            Glide.with(this@ProfileFragment)
                .load(uri?:loadUserDataFromSharedPreferences()?.photo)
                .into(profileImage)

            icOnline.isVisible = false
            icPhoto.isVisible = false
        }
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

    companion object {
        private const val REQUEST_CODE_IMAGE: Int = 101
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}