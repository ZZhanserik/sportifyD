package com.example.sportifyd.presentation.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sportifyd.R
import com.example.sportifyd.SplashActivity
import com.example.sportifyd.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private var sharedPreferences: SharedPreferences? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
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

            // Переход к экрану входа
            val intent = Intent(requireActivity(), SplashActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}