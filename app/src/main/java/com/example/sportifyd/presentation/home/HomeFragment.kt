package com.example.sportifyd.presentation.home

import PopularOrganizersAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sportifyd.databinding.FragmentHomeBinding
import com.example.sportifyd.presentation.home.adapter.PopularEventAdapter
import com.example.sportifyd.presentation.home.adapter.PopularOrganizers
import com.example.sportifyd.presentation.home.adapter.mockListForPopularEvents
import com.example.sportifyd.presentation.home.adapter.mockListForPopularOrganizers

class HomeFragment : Fragment() {

    private var dataList = mutableListOf<PopularOrganizers>()
    private lateinit var adapter: PopularEventAdapter
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = PopularEventAdapter()
        adapter.submitList(mockListForPopularEvents)
        binding.popularEventsRv.adapter = adapter

        dataList = mockListForPopularOrganizers
        val adapterOrganizers = PopularOrganizersAdapter(dataList)
        binding.popularOrganizersRv.adapter = adapterOrganizers

    }

    fun updateData(newData: List<PopularOrganizers>) {
        dataList.clear()
        dataList.addAll(newData)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}