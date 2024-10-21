package com.dicoding.intermediate.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate.data.network.ApiResponse
import com.dicoding.intermediate.data.repo.AuthRepo
import com.dicoding.intermediate.utils.helper.Event
import kotlinx.coroutines.launch

class LoginVM(private val repository: AuthRepo) : ViewModel() {

    val loginResult: LiveData<Event<ApiResponse<String>>> by lazy { _loginResult }
    private val _loginResult = MutableLiveData<Event<ApiResponse<String>>>()

    fun loginUser(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            repository.login(email, password)
                .collect {
                    _loginResult.postValue(Event(it))
                }
        }
    }
}