package com.example.sportifyd.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sportifyd.databinding.ItemPopularEventsBinding

class PopularEventAdapter :
    ListAdapter<PopularEvent, PopularEventAdapter.MyViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemPopularEventsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(val binding: ItemPopularEventsBinding) : ViewHolder(binding.root) {
        fun bind(item: PopularEvent) {
            binding.run {
                eventName.text = item.eventName
                eventStatus.text = item.eventStatus
                eventPrice.text = item.pricePerPerson
                eventParticipantsNumber.text = item.taken
            }
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