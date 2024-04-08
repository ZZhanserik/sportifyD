package com.example.sportifyd.presentation.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sportifyd.data.Service
import com.example.sportifyd.R
import com.example.sportifyd.SplashActivity
import com.example.sportifyd.databinding.FragmentProfileBinding
import com.example.sportifyd.entity.User
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
        val user = loadUserDataFromSharedPreferences()

        if (user?.photo != null) {
            setPhotoProfile()
        }

        binding.run {
            fullNameTv.text = user?.fullName

            bioTextView.text = user?.bio

            editBio.setOnClickListener {
                val editText = EditText(requireContext())
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Update Bio")
                    .setView(editText).setPositiveButton("Update") { dialog, which ->
                        val newText = editText.text.toString()
                        bioTextView.text = newText
                        updateBioInFirebase(newText)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .create()

                builder.show()
            }

            location.text = getLocation()

            editLocation.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateLocation.isVisible = true
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
            )
            updateLocation.setOnClickListener {
                location.text = editLocation.text.toString()
                saveLocation(editLocation.text.toString())
                editLocation.clearFocus()
                editLocation.text.clear()
                updateLocation.isVisible = false
            }

            imageProfile.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }
                startActivityForResult(intent, REQUEST_CODE_IMAGE)
            }
            accountInformation.setOnClickListener {
                navController.navigate(R.id.action_navigation_profile_to_accountInformationFragment)
            }



            changePassword.setOnClickListener {
                navController.navigate(R.id.action_navigation_profile_to_changePasswordFragment)
            }


            logoutButton.setOnClickListener {
                sharedPreferences?.edit()?.clear()?.apply()

                val intent = Intent(requireActivity(), SplashActivity::class.java)
                startActivity(intent)
            }
        }
        private fun updateBioInFirebase(newText: String) {
            Service.getUsersDataRef().child(Service.getCurrentUser()?.uid ?: "")
                .setValue(
                    binding.run {
                        loadUserDataFromSharedPreferences()?.copy(
                            bio = newText
                        )
                    }
                ).addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "You have successfully updated Bio",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.run {
                        saveUserDataToSharedPreferences(
                            loadUserDataFromSharedPreferences()?.copy(
                                bio = newText
                            ) ?: User()
                        )
                    }
                }
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
                .load(uri ?: loadUserDataFromSharedPreferences()?.photo)
                .apply(RequestOptions.circleCropTransform())
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

    private fun getLocation(): String? {
        val sharedPref = activity?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPref?.getString(getString(R.string.location), "Almaty, Kazakhstan")
    }

    private fun saveLocation(location: String) {
        val sharedPref =
            activity?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.location), location)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}