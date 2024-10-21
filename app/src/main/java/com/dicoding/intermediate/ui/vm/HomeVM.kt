package com.dicoding.intermediate.ui.vm

import androidx.lifecycle.ViewModel
import com.dicoding.intermediate.data.repo.StoryRepo

class HomeVM(private val repository: StoryRepo) : ViewModel() {
    fun getStory() = repository.getPagingStories()
}