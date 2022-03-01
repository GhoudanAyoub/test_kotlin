package com.example.test_kotlin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_kotlin.Models.response
import com.example.test_kotlin.Models.responseAction
import com.example.test_kotlin.di.retroRepository
import com.example.test_kotlin.utils.DataHandler
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class MainViewModel @Inject constructor(private val retroRepository: retroRepository) : ViewModel() {

    private val _usersMutable = MutableLiveData<DataHandler<response>>()
    val users: LiveData<DataHandler<response>> = _usersMutable

    private val _actionMutable = MutableLiveData<DataHandler<responseAction>>()
    val actions: LiveData<DataHandler<responseAction>> = _actionMutable

    fun getUsers(){
        _usersMutable.postValue(DataHandler.LOADING())
        viewModelScope.launch {
            val response = retroRepository.getUsers()
            _usersMutable.postValue(handleResponseUsers(response))
        }
    }

    private fun handleResponseUsers(response: Response<response>): DataHandler<response> {
        if (response.isSuccessful) {
            response.body()?.let { responseAction ->
                return DataHandler.SUCCESS(responseAction)
            }
        }
        return DataHandler.ERROR(message = response.errorBody().toString())
    }

    fun getAction(id:Int?){
        _actionMutable.postValue(DataHandler.LOADING())
        viewModelScope.launch {
            val response = retroRepository.getAction(id)
            _actionMutable.postValue(handleResponseActions(response))
        }
    }

    private fun handleResponseActions(response: Response<responseAction>): DataHandler<responseAction> {
        if (response.isSuccessful) {
            response.body()?.let { responseAction ->
                return DataHandler.SUCCESS(responseAction) }
        }
        return DataHandler.ERROR(message = response.errorBody().toString())
    }

}