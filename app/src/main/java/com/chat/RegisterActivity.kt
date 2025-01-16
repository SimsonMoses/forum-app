package com.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chat.activity.LoginActivity
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

class RegisterActivity : AppCompatActivity() {

    lateinit var name: EditText // lateint mean later will be initialized
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var register: Button
    lateinit var login: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        name = findViewById(R.id.name)
        register = findViewById(R.id.register)
        login = findViewById(R.id.login)

        login.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        register.setOnClickListener {
            val queue = Volley.newRequestQueue(applicationContext)
            val url = "https://qflh6g8m-3000.inc1.devtunnels.ms" + "/api/user/register"

            val requestBody = JSONObject()
            try {
                requestBody.put("name", name.text)
                requestBody.put("email", email.text)
                requestBody.put("password", password.text)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val stringRequest: StringRequest =
                object : StringRequest(Method.POST, url, Listener { res ->
                    Log.i("register_res", res)
                }, Response.ErrorListener { error ->
                    Log.i("register_err_res", error.message.toString())
                }) {
                    override fun getBody(): ByteArray {
                        return requestBody.toString().toByteArray(Charset.defaultCharset())
                    }

                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>();
                        headers["Content-Type"] = "application/json"
                        return headers;
                    }
                }
            queue.add(stringRequest)

        }

    }
}