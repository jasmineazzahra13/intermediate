package com.dicoding.intermediate.data.network.response

import com.dicoding.intermediate.data.md.Story
import com.google.gson.annotations.SerializedName

data class StoryResponse (
    val error: Boolean,
    val message: String,
    @SerializedName("listStory")
    val data: List<Story>
)