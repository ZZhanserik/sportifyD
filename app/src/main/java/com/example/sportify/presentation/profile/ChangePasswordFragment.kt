package com.example.sportify.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.sportify.R
import com.example.sportify.data.Service
import com.example.sportify.databinding.FragmentChangePasswordBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        bottomNavigationView.isVisible = false


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        invalidate()
    }

    private fun invalidate() {
        val user = Service.getCurrentUser()

        binding.run {

            button.setOnClickListener {
                val newPassword = newPasswordEditText.text.toString()
                val confirmPassword = newPasswordConfirmEditText.text.toString()

                if (newPassword.isNotBlank() && newPassword == confirmPassword) {
                    user?.updatePassword(newPassword)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                // Пароль успешно обновлен
                                "Password updated"

                                Log.d("TAG", "Password updated")
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to update password: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                // Возникла ошибка при обновлении пароля
                                Log.d(
                                    "TAG",
                                    "Failed to update password: ${task.exception?.message}"
                                )
                            }
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "fill all fields",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView.isVisible = true
    }
}