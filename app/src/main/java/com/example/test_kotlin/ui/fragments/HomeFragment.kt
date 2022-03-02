package com.example.test_kotlin.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.test_kotlin.R
import com.example.test_kotlin.databinding.FragmentHomeBinding
import com.example.test_kotlin.utils.checkForInternet

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var binding: FragmentHomeBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        if (checkForInternet(getView()!!.context)) {
            Handler().postDelayed(Runnable {
                findNavController().navigate(R.id.action_homeFragment_to_mainFragment)
                binding.progressBarHome.visibility = View.GONE
            }, 1000)
        } else
            findNavController().navigate(R.id.action_homeFragment_to_offLineFragment)


    }

}
