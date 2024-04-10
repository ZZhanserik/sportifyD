package com.example.sportify.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.example.sportify.data.Service
import com.example.sportify.databinding.FragmentNotificationsBinding
import com.example.sportify.entity.SportEvent
import com.example.sportify.presentation.home.adapter.PopularEventAdapter
import com.example.sportify.presentation.home.adapter.PopularEventViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.Query
import java.text.SimpleDateFormat
import java.util.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var popularEvents: FirebaseRecyclerOptions<SportEvent>
    private lateinit var adapterEvents: FirebaseRecyclerAdapter<SportEvent, PopularEventViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        loadDataNearestWeek()
         }
    private fun loadDataNearestWeek() {

        val query = getQueryForNearestEvents()
        popularEvents = FirebaseRecyclerOptions.Builder<SportEvent>().setQuery(query, SportEvent::class.java).build()
        adapterEvents = PopularEventAdapter(popularEvents) {
            val bottomSheetFragment = EventDetailsBottomSheet.newInstance(it)
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
        adapterEvents.startListening()
        binding.popularEventsRv.adapter = adapterEvents
    }

    private fun getQueryForNearestEvents(): Query {
        val currentDate = Calendar.getInstance()
        val dateFormatForDataRef = SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault())

        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val startDate = currentDate.timeInMillis

        currentDate.add(Calendar.DAY_OF_WEEK, 7)
        val endDate = currentDate.timeInMillis


        val query = Service.getEventsDataRef()
            .orderByChild("date")
            .startAt(dateFormatForDataRef.format(startDate))
            .endAt(dateFormatForDataRef.format(endDate))
        return query
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}