package com.example.sportifyd.presentation.newcontest

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.sportifyd.databinding.FragmentNewContestBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class NewContestFragment:Fragment() {

    private var _binding : FragmentNewContestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewContestBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val suggestions = arrayOf("Apple", "Banana", "Orange", "Pineapple", "Grapes")


        val adapter =
            ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, suggestions)

        binding.autoCompleteTextView.setAdapter(adapter)

        val dateTextView = binding.dateTextView

        dateTextView.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val datePicker = builder.build()

            datePicker.addOnPositiveButtonClickListener {
                val selectedDateInMillis = datePicker.selection ?: 0
                val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
                val selectedDate = dateFormat.format(Date(selectedDateInMillis))

                dateTextView.text = selectedDate
            }

            datePicker.show(childFragmentManager, "DATE_PICKER")
        }

        val selectedTimeTextView = binding.timeTextView
        selectedTimeTextView.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            picker.addOnPositiveButtonClickListener {
                val hour = picker.hour
                val minute = picker.minute
                val formattedTime =
                    String.format("%02d:%02d %s", hour, minute, if (hour < 12) "AM" else "PM")
                selectedTimeTextView.text = formattedTime
            }

            picker.show(childFragmentManager, "timePicker")
        }


        val hoursSpinner = binding.hoursSpinner

        // Массив с вариантами продолжительности времени
        val durations = arrayOf("1 hour", "2 hours", "3 hours", "4 hours", "5 hours", "6 hours")

        // Создание адаптера для Spinner
        val adapterHour = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, durations)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        hoursSpinner.adapter = adapterHour
        hoursSpinner.setSelection(0)
        // Обработчик выбора элемента из Spinner
        hoursSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedDuration = parent?.getItemAtPosition(position).toString()
                // Действия при выборе продолжительности
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Действия, если ничего не выбрано
            }
        }

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val userRef = FirebaseDatabase.getInstance().getReference("events")

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}