package com.example.sportifyd.presentation.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sportifyd.data.Service
import com.example.sportifyd.databinding.FragmentNotificationsBinding
import com.example.sportifyd.entity.SportEvent
import com.example.sportifyd.presentation.home.adapter.PopularEventAdapter
import com.example.sportifyd.presentation.home.adapter.PopularEventViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var popularEvents: FirebaseRecyclerOptions<SportEvent>
    private lateinit var adapterEvents: FirebaseRecyclerAdapter<SportEvent, PopularEventViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
        loadDataRV2()
    }
    private fun loadData() {
        popularEvents =
            FirebaseRecyclerOptions.Builder<SportEvent>()
                .setQuery(Service.getEventsDataRef(), SportEvent::class.java)
                .build()
        adapterEvents = PopularEventAdapter(popularEvents, {})
        adapterEvents.startListening()
        binding.popularEventsRv.adapter = adapterEvents
    }
    private fun loadDataRV2() {
        popularEvents =
            FirebaseRecyclerOptions.Builder<SportEvent>()
                .setQuery(Service.getEventsDataRef(), SportEvent::class.java)
                .build()
        adapterEvents = PopularEventAdapter(popularEvents, {})
        adapterEvents.startListening()
        binding.popularOrganizersRv.adapter = adapterEvents
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}