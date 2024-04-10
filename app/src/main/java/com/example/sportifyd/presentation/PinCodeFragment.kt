package com.example.sportifyd.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sportifyd.MainActivity
import com.example.sportifyd.databinding.FragmentPinCodeBinding

class PinCodeFragment : Fragment() {


    private var _binding: FragmentPinCodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (getPin() == null) {
            binding.run {
                setPinBlock.visibility = VISIBLE
                enterPinBlock.visibility = GONE

                setPinButton.setOnClickListener {

                    if (pinSetEditText.text.toString().length != 4) {
                        Toast.makeText(
                            requireContext(),
                            "Pin should be 4 characters",
                            Toast.LENGTH_SHORT,
                        ).show()
                        return@setOnClickListener
                    }

                    savePin(pinSetEditText.text.toString())
                    enterWithPin()
                }
            }
        } else {
            enterWithPin()
        }

    }

    private fun enterWithPin() {
        binding.run {
            pinEditText.requestFocus()
            setPinBlock.visibility = GONE
            enterPinBlock.visibility = VISIBLE

            loginButton.setOnClickListener {
                if (getPin() == pinEditText.text.toString()) {
                    requireContext().startActivity(
                        Intent(
                            requireContext(),
                            MainActivity::class.java
                        )
                    )
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Incorrect Pin",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    // Сохранение PIN-кода
    private fun savePin(pin: String) {
        val sharedPreferences = context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString(PIN_KEY, pin)?.apply()
    }

    // Получение PIN-кода
    private fun getPin(): String? {
        val sharedPreferences = context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(PIN_KEY, null)
    }

    companion object {
        fun newInstance() = PinCodeFragment()
        private const val PIN_KEY = "pin"
    }
}