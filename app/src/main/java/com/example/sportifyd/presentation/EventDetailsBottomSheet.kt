package com.example.sportifyd.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sportifyd.data.Service
import com.example.sportifyd.databinding.EventDetailsBottomSheetBinding
import com.example.sportifyd.entity.SportEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EventDetailsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: EventDetailsBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var item: SportEvent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        item = arguments?.getSerializable("item") as SportEvent
        _binding = EventDetailsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.createButton.setOnClickListener {
            Service.subscribeToEvent(item.eventId){

                Toast.makeText(
                    requireContext(),
                    "You have joined",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        fun newInstance(item: SportEvent): EventDetailsBottomSheet {
            val fragment = EventDetailsBottomSheet()
            val args = Bundle()
            // Передача данных в аргументы фрагмента
            args.putSerializable("item", item)
            fragment.arguments = args
            return fragment
        }
    }
}