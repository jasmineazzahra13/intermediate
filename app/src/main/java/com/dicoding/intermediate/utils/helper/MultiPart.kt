package com.dicoding.intermediate.utils.helper

import com.dicoding.intermediate.utils.constant.ConstantApp.MULTIPART_FILE_NAME
import com.dicoding.intermediate.utils.constant.ConstantApp.MULTIPART_FORM_DATA
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun File.toMultipart(): MultipartBody.Part {
    return MultipartBody.Part
        .createFormData(
            name = MULTIPART_FILE_NAME,
            filename = this.name,
            body = this.asRequestBody()
        )
}

fun String.toRequestBody() :RequestBody {
    return this.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
}

fun Float?.toRequestBody(): RequestBody? {
    if (this == null) return null
    return this.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
}