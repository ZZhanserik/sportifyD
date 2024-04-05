package com.example.sportifyd.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sportifyd.databinding.ItemPopularEventsBinding
import com.example.sportifyd.entity.SportEvent
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class PopularEventAdapter(options: FirebaseRecyclerOptions<SportEvent>) : FirebaseRecyclerAdapter<SportEvent, PopularEventViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularEventViewHolder {
        return PopularEventViewHolder(
            ItemPopularEventsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: PopularEventViewHolder, position: Int, model: SportEvent) {
        val popularEvent = convertSportEventToPopularEvent(model)
        holder.bind(popularEvent)
    }

    private fun convertSportEventToPopularEvent(sportEvent: SportEvent): PopularEvent {
        return PopularEvent(
            eventName = sportEvent.eventName,
            eventStatus = "Some Status", // Пример значения для статуса события
            pricePerPerson = "Some Price", // Пример значения для цены
            taken = "Some Taken" // Пример значения для занятости
        )
    }
}
class PopularEventViewHolder(val binding: ItemPopularEventsBinding) : ViewHolder(binding.root) {

    fun bind(item: PopularEvent) {
                        binding.run {
                            eventName.text = item.eventName
                            eventStatus.text = item.eventStatus
                            eventPrice.text = item.pricePerPerson
                            eventParticipantsNumber.text = item.taken
                        }
                    }
                }
