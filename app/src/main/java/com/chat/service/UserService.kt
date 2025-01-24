package com.chat.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chat.model.CommonResponse
import com.chat.model.User
import com.chat.util.SharedPreference
import com.google.gson.Gson
import org.json.JSONObject

class UserService {

    fun me(context: Context, callback: (User?) -> Unit) {
        val queue = Volley.newRequestQueue(context)
        val url = SharedPreference().getEndPointHost(context) + "/api/user/me";

        val meRequest: StringRequest = object : StringRequest(
            Method.GET,
            url,
            Response.Listener { response ->
                try {
                    val gson = Gson()
                    val commonResponse =
                        gson.fromJson(response.toString(), CommonResponse::class.java)
                    if (commonResponse.status == "SUCCESS") {
                        Log.i("api_me", "Api Success")
                        val user: User = gson.fromJson(
                            JSONObject(response).getJSONObject("data").toString(),
                            User::class.java
                        )
                        callback(user)
                    } else {
                        Log.e(
                            "api_me",
                            "Api Failed, Status: ${commonResponse.message} Message: ${commonResponse.message}"
                        )
                        callback(null)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("api_me", "Api Failed: ${e.message}")
                    callback(null)
                }
            },
            Response.ErrorListener { error ->
                val response = error.networkResponse.data.decodeToString();
                val jsonObject = JSONObject(response)

                Log.e("api_me", "Error: status: ${error.networkResponse.statusCode.toString()}")
                Log.e("api_me", "Error Response: ${error.networkResponse}")
                val toast: Toast =
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT)
                toast.show();
                callback(null)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer " + SharedPreference().getAccessToken(context)
                return headers;
            }
        }
        queue.add(meRequest)
    }
}