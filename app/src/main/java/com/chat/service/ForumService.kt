package com.chat.service

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chat.model.CommonResponse
import com.chat.model.Forum
import com.chat.util.SharedPreference
import com.google.gson.Gson
import org.json.JSONObject

class ForumService {

    fun exploreForums(context: Context, callback: (Forum?) -> Unit) {
        val queue = Volley.newRequestQueue(context)
        val url = SharedPreference().getEndPointHost(context) + "/api/forum";

        val exploreForumRequest: StringRequest = object : StringRequest(
            Method.GET,
            url,
            Response.Listener { response ->
                try {
                    val gson = Gson()
                    val commonResponse =
                        gson.fromJson(response.toString(), CommonResponse::class.java)
                    if (commonResponse.status == "SUCCESS") {
                        Log.i("api_explore_forum", "Api Success")
                        val forum = gson.fromJson(
                            JSONObject(response).getJSONObject("data").toString(),
                            Forum::class.java
                        )
                        callback(forum)
                        // TODO: toast a message that forum fetch successfully
                    } else {
                        Log.e(
                            "api_explore_forum",
                            "Api Failed, Status: ${commonResponse.status} Message: ${commonResponse.message}"
                        )
                        callback(null)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("api_explore_forum", "Error: ${e.message}")
                    callback(null)
                }
            },
            Response.ErrorListener { error -> }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer " + SharedPreference().getAccessToken(context)
                return headers
            }
        }
        queue.add(exploreForumRequest);
    }
}