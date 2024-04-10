package com.example.sportify.presentation.home.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sportify.databinding.ItemPopularEventsBinding
import com.example.sportify.entity.SportEvent

class MyEventsAdapter(val onClick: (SportEvent) -> Unit) :
    ListAdapter<SportEvent, MyEventsAdapter.MyEventsViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyEventsViewHolder {
        return MyEventsViewHolder(
            ItemPopularEventsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onClick
        )
    }

    override fun onBindViewHolder(holder: MyEventsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyEventsViewHolder(
        val binding: ItemPopularEventsBinding,
        private val onClick: (SportEvent) -> Unit
    ) : ViewHolder(binding.root) {
        fun bind(item: SportEvent) {
            val popularEvent = convertSportEventToPopularEvent(item)

            binding.run {
                eventName.text = popularEvent.eventName
                eventStatus.text = popularEvent.eventStatus
                eventPrice.text = popularEvent.pricePerPerson
                eventParticipantsNumber.text = popularEvent.taken
                root.setOnClickListener { onClick.invoke(item) }
            }

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
}

class DiffUtilCallback : DiffUtil.ItemCallback<SportEvent>() {

    override fun areItemsTheSame(oldItem: SportEvent, newItem: SportEvent): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SportEvent, newItem: SportEvent): Boolean {
        return oldItem == newItem
    }
}