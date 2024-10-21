package com.dicoding.intermediate.utils

import android.content.Context
import android.content.SharedPreferences
import com.dicoding.intermediate.utils.constant.ConstantApp.KEY_TOKEN
import com.dicoding.intermediate.utils.constant.ConstantApp.PREFS_NAME

class PreferManager(context: Context) {

    private var prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    fun storeLoginData(token: String) {
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun clearAllPreferences() {
        editor.remove(KEY_TOKEN)
        editor.apply()
    }

    val getToken = prefs.getString(KEY_TOKEN, "") ?: ""
    val isLogin = getToken.isNotEmpty()

}