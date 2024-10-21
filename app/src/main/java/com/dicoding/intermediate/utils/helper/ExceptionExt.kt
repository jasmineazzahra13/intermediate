package com.dicoding.intermediate.utils.helper

import com.dicoding.intermediate.data.network.response.BaseResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.lang.reflect.Type

val gson = Gson()
val type: Type = object : TypeToken<BaseResponse>() {}.type

fun Exception.createResponse(): BaseResponse? {
    return when (this) {
        is HttpException -> {
            gson.fromJson(response()?.errorBody()?.charStream(), type)
        }
        else -> {
            BaseResponse(
                message = this.message ?: "",
                error = true
            )
        }
    }
}