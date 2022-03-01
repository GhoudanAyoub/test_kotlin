package com.example.test_kotlin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_kotlin.R
import com.example.test_kotlin.databinding.MainFragmentBinding
import com.example.test_kotlin.ui.adapter.userAdapter
import com.example.test_kotlin.ui.main.MainViewModel
import com.example.test_kotlin.utils.DataHandler
import com.example.test_kotlin.utils.LogData
import javax.inject.Inject

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var binding: MainFragmentBinding

    @Inject
    lateinit var usersAdapter:  userAdapter

    val mainViewModel: MainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)
        childClickListener()
        mainViewModel.getUsers()
        mainViewModel.users.observe(viewLifecycleOwner, { Observer->
            when (Observer) {
                is DataHandler.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    usersAdapter.differ.submitList(Observer.data?.Users)
                }
                is DataHandler.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    LogData("onViewCreated: ERROR " + Observer.message)
                }
                is DataHandler.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    LogData("onViewCreated: LOADING..")

                }
            }
        })
    }
    private fun childClickListener(){

        usersAdapter.onUsersClicked {
            val bundle = Bundle().apply {
                putParcelable("user", it)
            }
            findNavController().navigate(
                R.id.action_mainFragment_to_actionFragment,
                bundle
            )
        }

        binding.usersRecycler.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}