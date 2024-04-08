package com.example.sportifyd.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sportifyd.data.Service
import com.example.sportifyd.databinding.FragmentChangePasswordBinding

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

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
}