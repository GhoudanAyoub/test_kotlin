package com.example.test_kotlin.ui.ViewModels

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_kotlin.Models.action
import com.example.test_kotlin.Models.users
import com.example.test_kotlin.di.DBRepository
import com.example.test_kotlin.di.Transformer
import com.example.test_kotlin.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineViewModel @Inject constructor(private val dbRepository: DBRepository) : ViewModel() {


    var users = Transformations.map(dbRepository.getAllOfflineUsers()) { list ->
        val temp = list.map {
            Transformer.convertUserEntityToUserModel(it)
        }
        if (temp.isNullOrEmpty()) {
            DataHandler.ERROR(null, "LIST IS EMPTY OR NULL")
        } else {
            DataHandler.SUCCESS(temp)
        }
    }

    fun insertUser(users: users) {
        viewModelScope.launch {
            dbRepository.insertUser(users)
        }
    }

    fun delete(users: users) {
        viewModelScope.launch {
            dbRepository.delete(users)
        }
    }

    fun getAllOfflineUsers() = dbRepository.getAllOfflineUsers()

    fun insertAction(actions: List<action>) {
        viewModelScope.launch {
            actions.forEach { action -> dbRepository.insertAction(action) }
        }
    }

    fun delete(action: action) {
        viewModelScope.launch {
            dbRepository.deleteAction(action)
        }
    }
}