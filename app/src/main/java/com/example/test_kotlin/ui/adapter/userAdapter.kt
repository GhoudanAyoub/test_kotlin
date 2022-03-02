package com.example.test_kotlin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test_kotlin.Models.users
import com.example.test_kotlin.databinding.ChildLayoutBinding
import javax.inject.Inject

class userAdapter @Inject constructor() : RecyclerView.Adapter<userAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ChildLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<users>() {

        override fun areItemsTheSame(oldItem: users, newItem: users): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: users, newItem: users): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ChildLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = differ.currentList[position]
        holder.binding.apply {
            name.text = users.name
            email.text = users.email

        }

        holder.itemView.setOnClickListener {
            setUsersClickListener?.let {
                it(users)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var setUsersClickListener: ((users: users) -> Unit)? = null

    fun onUsersClicked(listener: (users) -> Unit) {
        setUsersClickListener = listener
    }

}