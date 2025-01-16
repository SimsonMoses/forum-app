package com.chat.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreference {
    private val fileName: String = "chat_shared_preference";

    fun sharedAccessToken(context: Context,accessToken: String){
        val preference: SharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        preference.edit().putString("accessToken",accessToken).apply();
    }
}