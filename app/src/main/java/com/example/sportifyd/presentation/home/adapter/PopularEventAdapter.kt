package com.example.sportifyd.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sportifyd.databinding.ItemPopularEventsBinding
import com.example.sportifyd.entity.SportEvent
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class PopularEventAdapter(options: FirebaseRecyclerOptions<SportEvent>) : FirebaseRecyclerAdapter<SportEvent, MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemPopularEventsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: SportEvent) {
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
                class MyViewHolder(val binding: ItemPopularEventsBinding) : ViewHolder(binding.root) {
                    fun bind(item: PopularEvent) {
                        binding.run {
                            eventName.text = item.eventName
                            eventStatus.text = item.eventStatus
                            eventPrice.text = item.pricePerPerson
                            eventParticipantsNumber.text = item.taken
                        }
                    }
                }
                class DiffUtilCallback : DiffUtil.ItemCallback<PopularEvent>() {
                    override fun areItemsTheSame(oldItem: PopularEvent, newItem: PopularEvent): Boolean {
                        return oldItem == newItem
                    }
                    override fun areContentsTheSame(oldItem: PopularEvent, newItem: PopularEvent): Boolean {
                        return oldItem == newItem
                    }
                }