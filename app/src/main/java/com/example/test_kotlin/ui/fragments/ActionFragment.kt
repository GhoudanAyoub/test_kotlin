package com.example.test_kotlin.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_kotlin.R
import com.example.test_kotlin.databinding.ActionFragmentBinding
import com.example.test_kotlin.ui.adapter.actionAdapter
import com.example.test_kotlin.ui.main.MainViewModel
import com.example.test_kotlin.ui.main.OfflineViewModel
import com.example.test_kotlin.utils.DataHandler
import com.example.test_kotlin.utils.LogData
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActionFragment : Fragment(R.layout.action_fragment) {

    private lateinit var binding: ActionFragmentBinding
    private val args: ActionFragmentArgs by navArgs()

    @Inject
    lateinit var actionAdapter: actionAdapter

    val offlineViewModel: OfflineViewModel by viewModels()
    val mainViewModel: MainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = args.id
        binding = ActionFragmentBinding.bind(view)
        init()
        mainViewModel.getAction(user.id)
        mainViewModel.actions.observe(viewLifecycleOwner, { Observer ->
                when (Observer) {
                is DataHandler.SUCCESS -> {
                    binding.progressBarAction.visibility = View.GONE
                    actionAdapter.differ.submitList(Observer.data)
                    LogData(""+user.id)
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

        binding.fab.setOnClickListener {
            if (user != null) {
                offlineViewModel.insertUser(user)
            }
            Snackbar.make(binding.root, "User Saved ", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun init() {
        binding.actionsRecycler.apply {
            adapter = actionAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}