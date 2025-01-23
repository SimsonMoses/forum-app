package com.chat.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreference {
    private val fileName: String = "chat_shared_preference";

    /** Get Access Token from the Device
     * @param Context
     * */
    fun getAccessToken(context: Context): String{
        val preference: SharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return preference.getString("accessToken","").toString();
    }

    /**Set Access token in the Device
     * @param Context
     * @param Accesstoken
     * */
    fun setAccessToken(context: Context, accessToken: String){
        val preference: SharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        preference.edit().putString("accessToken",accessToken).apply();
    }

    /** Remove access token*/
    fun remove(context: Context,key:String){
        val preference: SharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
        preference.edit().remove(key).apply();
    }
}