package com.example.test_kotlin.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_kotlin.R
import com.example.test_kotlin.databinding.FragmentOfflineBinding
import com.example.test_kotlin.ui.ViewModels.OfflineViewModel
import com.example.test_kotlin.ui.adapter.userAdapter
import com.example.test_kotlin.utils.DataHandler
import com.example.test_kotlin.utils.LogData
import com.example.test_kotlin.utils.checkForInternet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OffLineFragment : Fragment(R.layout.fragment_offline) {

    lateinit var binding: FragmentOfflineBinding
    val offlineViewModel: OfflineViewModel by viewModels()

    @Inject
    lateinit var userAdapter: userAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOfflineBinding.bind(view)
        init()
        offlineViewModel.users.observe(viewLifecycleOwner, Observer { dataHandler ->
            when (dataHandler) {
                is DataHandler.SUCCESS -> {
                    LogData("onViewCreated: SUCCESS ")
                    userAdapter.differ.submitList(dataHandler.data)

                }
                is DataHandler.ERROR -> {
                    LogData("onViewCreated: ERROR ${dataHandler.message}")
                }
                is DataHandler.LOADING -> {
                    LogData("onViewCreated: LOADING")
                }
            }
        })
    }

    private fun init() {
        binding.offlineRecycler.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkForInternet(getView()!!.context))
            findNavController().navigate(R.id.action_offLineFragment_to_mainFragment)
    }
}