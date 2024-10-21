package com.dicoding.intermediate.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate.data.network.ApiResponse
import com.dicoding.intermediate.data.repo.AuthRepo
import com.dicoding.intermediate.utils.helper.Event
import kotlinx.coroutines.launch

class RegisterVM(private val repository: AuthRepo) : ViewModel() {

    val registerResult: LiveData<Event<ApiResponse<String>>> by lazy { _registerResult }
    private val _registerResult = MutableLiveData<Event<ApiResponse<String>>>()

    fun registerUser(
        name: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            repository.register(name, email, password)
                .collect {
                    _registerResult.postValue(Event(it))
                }
        }
    }

}