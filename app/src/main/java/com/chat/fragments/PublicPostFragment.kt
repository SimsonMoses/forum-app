package com.chat.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chat.R
import com.chat.adapters.post.PostAdapter
import com.chat.model.CommonResponse
import com.chat.model.Post
import com.chat.util.SharedPreference
import com.chat.util.UtilityClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class PublicPostFragment : Fragment() {

    lateinit var sharedPreference: SharedPreference
    lateinit var posts: ArrayList<Post>
    lateinit var recyclerView: RecyclerView
    lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(context)
        postAdapter = PostAdapter(ArrayList())
        recyclerView.adapter = postAdapter
    }

    fun getData() {
        val queue = Volley.newRequestQueue(context)
        val url = UtilityClass.getBackEndHost() + "/api/public/post"

        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                Log.i("PublicPostFragment_api_post", "Api all on explore public post")
                Log.i("PublicPostFragment_api_post",response)
                try {
                    val gson = Gson()
                    val postListType = object : TypeToken<ArrayList<Post>>() {}.type
                    val commonResponse = gson.fromJson(response.toString(),CommonResponse::class.java)
                    Log.i("data",commonResponse.data.toString())
                    val jsonData = gson.toJson(commonResponse.data)
                    val posts:ArrayList<Post> = gson.fromJson(jsonData,postListType)
                    Log.i("PublicPostFragment_api_post_data",posts.toArray().toString())
                    postAdapter.setData(posts)
                }catch (e: Exception){
                    Log.e("PublicPostFragment_api_post",e.message.toString())
                }

                // TODO: implement status success in the response
//                if(commonResponse.status == "SUCCESS"){
//                }else{
//                    context?.let {
//                        UtilityClass.showAlert(it,"Error",commonResponse.message);
//                    }
//                }
            },
            Response.ErrorListener { error ->
                Log.e("PublicPostFragment_api",error.message.toString())
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer " + context?.let {
                    sharedPreference.getAccessToken(it)
                }
                return headers
            }
        }
        queue.add(stringRequest)
    }

    override fun onResume() {
        super.onResume()
        posts  = ArrayList()
        sharedPreference = SharedPreference()
        getData()
    }
}