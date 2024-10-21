package com.dicoding.intermediate.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate.data.md.Story
import com.dicoding.intermediate.data.network.ApiResponse
import com.dicoding.intermediate.data.repo.StoryRepo
import kotlinx.coroutines.launch

class DetailStoryVM(private val repository: StoryRepo) : ViewModel() {
    val storyResult: LiveData<ApiResponse<Story>> by lazy { _storyResult }
    private val _storyResult = MutableLiveData<ApiResponse<Story>>()

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            repository.getDetailStory(id).collect {
                _storyResult.postValue(it)
            }
        }
    }
}