package com.example.printhub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.printhub.DeviceAdapter
import com.example.printhub.DeviceViewModel
import com.example.printhub.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val deviceViewModel: DeviceViewModel by activityViewModels()
    private lateinit var adapter: DeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DeviceAdapter(mutableListOf())
        binding.printerList.layoutManager = LinearLayoutManager(requireContext())
        binding.printerList.adapter = adapter

        deviceViewModel.devices.observe(viewLifecycleOwner) { devices ->
            adapter.setDevices(devices)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}