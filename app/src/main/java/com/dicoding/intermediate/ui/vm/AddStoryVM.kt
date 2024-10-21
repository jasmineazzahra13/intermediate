package com.dicoding.intermediate.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate.data.network.ApiResponse
import com.dicoding.intermediate.data.repo.StoryRepo
import com.dicoding.intermediate.utils.helper.Event
import kotlinx.coroutines.launch
import java.io.File

class AddStoryVM(private val repository: StoryRepo) : ViewModel() {
    val uploadResult: LiveData<Event<ApiResponse<String>>> by lazy { _uploadResult }
    private val _uploadResult = MutableLiveData<Event<ApiResponse<String>>>()

    fun uploadStory(
        photo: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ) {
        viewModelScope.launch {
            repository.addStory(photo, description, latitude, longitude).collect {
                _uploadResult.postValue(Event(it))
            }
        }
    }

}