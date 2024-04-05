package com.example.sportify.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sportifyd.data.Service
import com.example.sportifyd.databinding.FragmentSearchBinding
import com.example.sportifyd.entity.SportEvent
import com.example.sportifyd.presentation.home.adapter.PopularEventAdapter
import com.example.sportifyd.presentation.home.adapter.PopularEventViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var popularEvents: FirebaseRecyclerOptions<SportEvent>
    private lateinit var adapterEvents: FirebaseRecyclerAdapter<SportEvent, PopularEventViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadData("")

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != null) loadData(s.toString())
                else loadData("")
            }
        })
    }

    private fun loadData(data: String) {
        val query = Service.getEventsDataRef().orderByChild("eventName").startAt(data)
            .endAt(data + "\uf8ff")
        popularEvents =
            FirebaseRecyclerOptions.Builder<SportEvent>().setQuery(query, SportEvent::class.java)
                .build()
        adapterEvents = PopularEventAdapter(popularEvents)
        adapterEvents.startListening()
        binding.popularEventsRv.adapter = adapterEvents
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}