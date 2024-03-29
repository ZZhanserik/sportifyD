package com.example.sportifyd.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sportifyd.databinding.FragmentAccountEditBinding

class AccountInformationFragment: Fragment() {

    private var _binding: FragmentAccountEditBinding? = null

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

        _binding = FragmentAccountEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


}