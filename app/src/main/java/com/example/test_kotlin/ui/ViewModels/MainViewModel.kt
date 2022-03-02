package com.example.test_kotlin.ui.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_kotlin.Models.action
import com.example.test_kotlin.Models.users
import com.example.test_kotlin.di.retroRepository
import com.example.test_kotlin.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val retroRepository: retroRepository) : ViewModel() {

    private val _usersMutable = MutableLiveData<DataHandler<List<users>>>()
    val users: LiveData<DataHandler<List<users>>> = _usersMutable

    private val _actionMutable = MutableLiveData<DataHandler<List<action>>>()
    val actions: LiveData<DataHandler<List<action>>> = _actionMutable

    fun getUsers() {
        _usersMutable.postValue(DataHandler.LOADING())
        viewModelScope.launch {
            val response = retroRepository.getUsers()
            _usersMutable.postValue(handleResponseUsers(response))
        }
    }

    private fun handleResponseUsers(response: Response<List<users>>): DataHandler<List<users>> {
        if (response.isSuccessful) {
            response.body()?.let { responseAction ->
                return DataHandler.SUCCESS(responseAction)
            }
        }
        return DataHandler.ERROR(message = response.errorBody().toString())
    }

    fun getAction(id: Int?) {
        _actionMutable.postValue(DataHandler.LOADING())
        viewModelScope.launch {
            val response = retroRepository.getAction(id)
            _actionMutable.postValue(handleResponseActions(response))
        }
    }

    private fun handleResponseActions(response: Response<List<action>>): DataHandler<List<action>> {
        if (response.isSuccessful) {
            response.body()?.let { responseAction ->
                return DataHandler.SUCCESS(responseAction)
            }
        }
        return DataHandler.ERROR(message = response.errorBody().toString())
    }

}