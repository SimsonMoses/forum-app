package com.chat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chat.R
import com.chat.RegisterActivity
import com.chat.model.CommonResponse
import com.chat.model.User
import com.chat.util.SharedPreference
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

class LoginActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var login: Button
    lateinit var register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = findViewById(R.id.input_email)
        password = findViewById(R.id.input_password)
        register = findViewById(R.id.register)
        login = findViewById(R.id.login)

        register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        login.setOnClickListener {
            val queue = Volley.newRequestQueue(applicationContext)
            val url = "https://qflh6g8m-3000.inc1.devtunnels.ms" + "/api/user/login"

            val requestBody = JSONObject();
            try {
                requestBody.put("email", email.text)
                requestBody.put("password", password.text)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val stringRequest: StringRequest =
                object : StringRequest(Method.POST, url, Response.Listener { res ->
                    try {
                        val gson = Gson()
                        val response = gson.fromJson(res.toString(), CommonResponse::class.java)
                        if (response.status == "SUCCESS") {
                            val user = gson.fromJson(
                                JSONObject(res).getJSONObject("data").toString(), User::class.java
                            )
                            Log.i("login_res_data", user.name);
                            Log.i("login_res", response.toString())
                            val toast = Toast.makeText(this, response.message, Toast.LENGTH_SHORT)
                            toast.show()
                            startActivity(Intent(this,HomeActivity::class.java))
                            finish()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("login_err_res", e.message.toString());
                    }
                }, Response.ErrorListener { error ->
                    val response = error.networkResponse.data.decodeToString();
                    val jsonObject = JSONObject(response)

                    Log.i("login_err_res Status :", error.networkResponse.statusCode.toString())
                    Log.i("login_err_res:", jsonObject.toString())

                    val toast =
                        Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT)
                    toast.show()
                }) {
                    override fun getBody(): ByteArray {
                        return requestBody.toString().toByteArray(Charset.defaultCharset())
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>();
                        headers["Content-Type"] = "application/json"
                        return headers;
                    }

                    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                        Log.i("login_res", response.toString())
                        return try {
                            val headers = response?.headers
                            val token = headers?.get("token")
                            token?.let {
                                Log.i("login_res_token", "Token: $it")
                            }
                            saveShared(applicationContext, token.toString())
                            Response.success(
                                String(response?.data ?: ByteArray(0), Charsets.UTF_8),
                                HttpHeaderParser.parseCacheHeaders(response)
                            )

                        } catch (e: Exception) {
                            Response.error(ParseError(e))
                        }
                    }
                }
            queue.add(stringRequest)
        }
    }

    fun saveShared(context: Context, value: String) {
        val preference: SharedPreference = SharedPreference()
        Log.i("shared:", value.toString())
        preference.setAccessToken(context, value)
    }
}