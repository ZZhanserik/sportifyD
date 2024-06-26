package com.example.sportify.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sportify.databinding.ItemPopularEventsBinding
import com.example.sportify.entity.SportEvent
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.example.sportify.data.Service
import com.example.sportify.entity.SportEventStatus

class PopularEventAdapter(
    options: FirebaseRecyclerOptions<SportEvent>,
    val onClick: (SportEvent) -> Unit
) : FirebaseRecyclerAdapter<SportEvent, PopularEventViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularEventViewHolder {
        return PopularEventViewHolder(
            ItemPopularEventsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onClick
        )
    }


    override fun onBindViewHolder(
        holder: PopularEventViewHolder,
        position: Int,
        model: SportEvent
    ) {
        holder.bind(model)
    }

}

class PopularEventViewHolder(
    val binding: ItemPopularEventsBinding,
    private val onClick: (SportEvent) -> Unit
) : ViewHolder(binding.root) {
    fun bind(item: SportEvent) {
        val popularEvent = convertSportEventToPopularEvent(item)
        val joined = Service.checkIfJoinedToEvent(item.participants)

        binding.run {
            eventName.text = popularEvent.eventName
            eventStatus.text = popularEvent.eventStatus
            eventPrice.text = popularEvent.pricePerPerson
            eventStatus.text = if (item.maxParticipants == item.participantsNumber) SportEventStatus.CLOSED.name else SportEventStatus.OPEN.name
            eventParticipantsNumber.text = popularEvent.taken
            joinText.text = if (joined) "JOINED" else "JOIN"
            joinText.setBackgroundColor(if (joined) Color.WHITE else Color.GREEN)
            root.setOnClickListener { onClick.invoke(item) }
        }
    }

    private fun convertSportEventToPopularEvent(sportEvent: SportEvent): PopularEvent {
        return PopularEvent(
            eventName = sportEvent.eventName,
            eventStatus = sportEvent.status, // Пример значения для статуса события
            pricePerPerson = sportEvent.price, // Пример значения для цены
            taken = "${sportEvent.participantsNumber}/${sportEvent.maxParticipants}"
        )
    }
}

