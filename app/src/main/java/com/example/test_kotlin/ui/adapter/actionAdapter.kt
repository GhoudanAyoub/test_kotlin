package com.example.test_kotlin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test_kotlin.Models.action
import com.example.test_kotlin.databinding.ActionChildLayoutBinding
import javax.inject.Inject

class actionAdapter @Inject constructor() : RecyclerView.Adapter<actionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ActionChildLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<action>() {

        override fun areItemsTheSame(oldItem: action, newItem: action): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: action, newItem: action): Boolean {
            return oldItem.equals(newItem)
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActionChildLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = differ.currentList[position]
        holder.binding.apply {
            title.text = action.title

            completed.isChecked = action.completed!!

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}