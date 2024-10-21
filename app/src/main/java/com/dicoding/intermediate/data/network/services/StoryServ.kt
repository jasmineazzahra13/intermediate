package com.dicoding.intermediate.data.network.services

import com.dicoding.intermediate.data.network.response.BaseResponse
import com.dicoding.intermediate.data.network.response.DetailStoryResponse
import com.dicoding.intermediate.data.network.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryServ {

    @GET("stories")
    suspend fun fetchStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun fetchDetailStory(
        @Path("id") id: String
    ): DetailStoryResponse

    @POST("stories")
    @Multipart
    suspend fun addStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody? = null,
        @Part("lon") longitude: RequestBody? = null,
    ): BaseResponse

}