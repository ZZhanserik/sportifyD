package com.example.sportify.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import android.graphics.Color
import com.example.sportify.data.Service
import com.example.sportify.databinding.EventDetailsBottomSheetBinding
import com.example.sportify.entity.SportEvent
import com.example.sportify.entity.SportEventStatus
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
        binding.run {
            if (Service.checkIfJoinedToEvent(item.participants)) {
                createButton.text = "JOINED"
                createButton.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                unJoinCard.isVisible = true
                unJoinButton.setOnClickListener {
                    Service.unSubscribeToEvent(sportEvent = item,
                        onSuccess = {
                            Toast.makeText(
                                requireContext(),
                                "You have unjoined from this event",
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        })
                }
            } else {
                createButton.setOnClickListener {
                    if (item.status == SportEventStatus.CLOSED.name) {
                        Toast.makeText(
                            requireContext(),
                            "You cant join to Current event, it is full",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Service.subscribeToEvent(sportEvent = item,
                            onSuccess = {
                                Toast.makeText(
                                    requireContext(),
                                    "You have joined",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dismiss()
                            }, onError = { message ->
                                Toast.makeText(
                                    requireContext(),
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                dismiss()
                            })
                    }
                }
            }
            eventName.text = item.eventName
            eventLevel.text = item.level
            eventLocation.text = item.location
            eventPrice.text = item.price
            eventPlayersNumber.text = buildString {
                append(item.participantsNumber)
                append("/")
                append(item.maxParticipants)
            }
            eventDuration.text = item.duration
            eventTime.text = buildString {
                append(item.time)
                append(item.date)
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