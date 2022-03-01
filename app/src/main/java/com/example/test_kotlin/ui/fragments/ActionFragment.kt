package com.example.test_kotlin.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.test_kotlin.R
import com.example.test_kotlin.databinding.ActionFragmentBinding
import com.example.test_kotlin.ui.adapter.actionAdapter
import com.example.test_kotlin.ui.main.MainViewModel
import com.example.test_kotlin.utils.DataHandler
import com.example.test_kotlin.utils.LogData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActionFragment : Fragment(R.layout.action_fragment) {

    private lateinit var binding: ActionFragmentBinding
    private val args: ActionFragmentArgs by navArgs()

    @Inject
    lateinit var actionAdapter: actionAdapter

    val mainViewModel: MainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.id
        binding = ActionFragmentBinding.bind(view)
        mainViewModel.getAction(id)
        mainViewModel.actions.observe(viewLifecycleOwner, { Observer ->
            when (Observer) {
                is DataHandler.SUCCESS -> {
                    binding.progressBarAction.visibility = View.GONE
                    actionAdapter.differ.submitList(Observer.data?.Action)
                }
                is DataHandler.ERROR -> {
                    binding.progressBarAction.visibility = View.GONE
                    LogData("onViewCreated: ERROR " + Observer.message)
                }
                is DataHandler.LOADING -> {
                    binding.progressBarAction.visibility = View.VISIBLE
                    LogData("onViewCreated: LOADING..")

                }
            }
        })
    }

}