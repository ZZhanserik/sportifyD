package com.example.sportifyd.presentation.newcontest

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sportifyd.data.Service
import com.example.sportifyd.databinding.FragmentNewContestBinding
import com.example.sportifyd.entity.SportEvent
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewContestFragment:Fragment() {

    private var selectedDuration: String = ""
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
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, suggestions)

        binding.eventLevel.setAdapter(adapter)

        val dateTextView = binding.eventDate

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

        val selectedTimeTextView = binding.eventTime
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


        val hoursSpinner = binding.eventDuration

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
                selectedDuration = parent?.getItemAtPosition(position).toString()
                // Действия при выборе продолжительности
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Действия, если ничего не выбрано
            }
        }

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val event = SportEvent(
            eventId = 0,
            eventName = binding.eventName.text.toString(),
            location = binding.eventLocation.text.toString(),
            date = binding.eventDate.text.toString(),
            time = binding.eventTime.text.toString(),
            maxParticipants = binding.maxPlayersEditText.text.toString(),
            sportCategory = ""
        )

        binding.createButton.setOnClickListener {
            invalidate()
            Service.addNewEventToDB(event)
        }

    }
    private fun invalidate() {
        binding.run {
            if (eventName.text.isNullOrEmpty() || eventLevel.text.isNullOrEmpty() || eventDate.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "fill all fields",
                    Toast.LENGTH_SHORT,
                ).show()
                return
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}