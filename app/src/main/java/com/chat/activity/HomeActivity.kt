package com.chat.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.chat.R
import com.chat.adapters.HomeTabAdapter
import com.chat.model.User
import com.chat.service.UserService
import com.chat.util.SharedPreference
import com.chat.util.UtilityClass
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity(), OnNavigationItemSelectedListener {

    lateinit var drawLayout: DrawerLayout;
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle;
    lateinit var navigationView: NavigationView;
    lateinit var sharedPreference: SharedPreference;
    lateinit var user: User;

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerlayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val decorView = window.decorView
            val windowInsetsController =
                WindowInsetsControllerCompat(window, decorView)

            // Ensure content respects the system bars (e.g., notch, pinhole camera)
            ViewCompat.setOnApplyWindowInsetsListener(decorView) { view, insets ->
                val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(0, systemBarsInsets.top, 0, systemBarsInsets.bottom)
                insets
            }
        }

        sharedPreference = SharedPreference();

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Enable the "Up" button in the action bar
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        drawLayout = findViewById(R.id.drawerlayout) // drawlayout is activity homepage
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawLayout, R.string.nav_open, R.string.nav_close)

        drawLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        tabLayout.addTab(
            tabLayout.newTab().setText("Forums")
        )

        val adapter = HomeTabAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        )

        tabLayout.addOnTabSelectedListener(
            object:TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position;
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    TODO("Not yet implemented")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    TODO("Not yet implemented")
                }


            }
        )

        getMe();
    }

    private fun getMe() {
        Log.i("home_activity_me_method", "Me Function execution start")
//        TODO("Not yet implemented")
        UserService().me(applicationContext) { userData ->
            Log.i("home_page_me_api_callback", "Triggered")
            if (userData != null) {
                user = userData;
                val headerView = navigationView.getHeaderView(0)
                val name: TextView = headerView.findViewById(R.id.name)
                val email: TextView = headerView.findViewById(R.id.email)
                name.text = user.name;
                email.text = user.email;
            } else {

            }
        }
//        ForumService().exploreForums(this) { forum ->
//            if (forum != null) {
//                Toast.makeText(this, "Forum fetched: ${forum.name}", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Failed To fetch forum", Toast.LENGTH_SHORT).show();
//            }
//        }

    }

    fun logout() {
        // TODO: logout api, if session are maintained in backend system
        Log.i("test", "Logout method triggered")
        UtilityClass.showAlert(this, "Logout", "Are sure need to logout", Runnable {
            Log.i("test", "runnable reached")
            sharedPreference.remove(this, "accessToken")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            logout()
        }
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

}