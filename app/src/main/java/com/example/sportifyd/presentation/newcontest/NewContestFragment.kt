package com.example.sportifyd.presentation.newcontest

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
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

    private var formattedTimeForDataRef: String = ""
    private var selectedDateForDataRef: String = ""
    private var _binding : FragmentNewContestBinding? = null
    private val binding get() = _binding!!
    private var selectedDuration: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewContestBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.run {
            val suggestions = arrayOf("Easy", "Hard", "Medium")
            val adapterEventLevel =
                ArrayAdapter(requireActivity(), R.layout.simple_dropdown_item_1line, suggestions)
            adapterEventLevel.notifyDataSetChanged()
            eventLevel.setAdapter(adapterEventLevel)
            eventLevel.setOnClickListener { eventLevel.showDropDown() }

            eventDate.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                val datePicker = builder.build()
                datePicker.addOnPositiveButtonClickListener {
                    val selectedDateInMillis = datePicker.selection ?: 0
                    val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
                    val dateFormatForDataRef = SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault())
                    val selectedDate = dateFormat.format(Date(selectedDateInMillis))
                    selectedDateForDataRef = dateFormatForDataRef.format(Date(selectedDateInMillis))
                    eventDate.text = selectedDate
                }

                datePicker.show(childFragmentManager, "DATE_PICKER")
            }

            eventTime.setOnClickListener {
                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .build()

                picker.addOnPositiveButtonClickListener {
                    val hour = picker.hour
                    val minute = picker.minute
                    val formattedTime =
                        String.format("%02d:%02d %s", hour, minute, if (hour < 12) "AM" else "PM")
                    formattedTimeForDataRef = String.format("%02d:%02d", hour, minute)
                    eventTime.text = formattedTime
                }

                picker.show(childFragmentManager, "timePicker")
            }

            val durations = arrayOf("1 hour", "2 hours", "3 hours", "4 hours", "5 hours", "6 hours")
            val adapterHour =
                ArrayAdapter(requireContext(), R.layout.simple_spinner_item, durations)
            adapterEventLevel.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            eventDuration.adapter = adapterHour
            eventDuration.setSelection(0)

            eventDuration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedDuration = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedDuration = parent?.getItemAtPosition(0).toString()
                }
            }
        }

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.createButton.setOnClickListener {
            invalidate()
        }

    }

    private fun invalidate() {
        binding.run {
            if (eventName.text.isNullOrEmpty()
                || eventLevel.text.isNullOrEmpty()
                || eventLocation.text.isNullOrEmpty()
                || formattedTimeForDataRef.isBlank()
                || selectedDateForDataRef.isBlank()
                || price.text.isNullOrEmpty()
                || maxPlayersEditText.text.isNullOrEmpty()
                || description.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "fill all fields",
                    Toast.LENGTH_SHORT,
                ).show()
                return
            } else {
                createNewEvent()
            }
        }
    }

    private fun createNewEvent() {
        binding.run {
            val event = SportEvent(
                eventName = eventName.text.toString(),
                level = eventLevel.text.toString(),
                location = eventLocation.text.toString(),
                price = price.text.toString() + "TG",
                date = selectedDateForDataRef,
                time = formattedTimeForDataRef,
                duration = selectedDuration,
                sportCategory = "",
                maxParticipants = maxPlayersEditText.text.toString()
            )
            Service.createNewEventToDB(event).addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "You have succesfully created Event",
                    Toast.LENGTH_LONG,
                ).show()
                clearFields()
            }
        }
    }

    private fun clearFields() {
        binding.run {
            eventName.text.clear()
            eventLevel.text.clear()
            eventLocation.text.clear()
            price.text.clear()
            eventDate.text = ""
            eventTime.text = ""
            eventDuration.setSelection(0)
            maxPlayersEditText.text.clear()
            description.text.clear()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}