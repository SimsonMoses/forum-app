package com.chat

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chat.activity.HomeActivity
import com.chat.util.SharedPreference
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Timer().schedule(1000) {

            val accessToken = SharedPreference().getAccessToken(applicationContext);
            if (accessToken.isBlank() || accessToken.isEmpty()) {
                startActivity(Intent(applicationContext, RegisterActivity::class.java))
            } else {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
            }

            finish()
        }
    }
}