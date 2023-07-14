package com.example.whatsappclone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.whatsappclone.activity.MenuActivity
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appPagerAdapter: AppPagerAdapter
    private var titles = arrayListOf("Chats", "Status", "Calls")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        binding.apply {
            toolbarMain.title = "Whatsapp Clone"
            setSupportActionBar(toolbarMain)
            appPagerAdapter = AppPagerAdapter(this@MainActivity)
            viewPager2Main.adapter = appPagerAdapter
            TabLayoutMediator(tabLayoutMain, viewPager2Main) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val saveDataTask: TimerTask = object : TimerTask() {
            override fun run() {
                editor.putString("dataKey", "yourData")
                editor.apply()
                Log.d("ABC","${sharedPreferences.getString("dataKey","")}")
            }
        }
        val timer = Timer()
        timer.scheduleAtFixedRate(saveDataTask, 0, 5 * 1000)

    }

    class AppPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> Chats()
                2 -> Status()
                3 -> Calls()

                else -> Chats()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                intent.putExtra("OptionName", "profile")
                startActivity(intent)
            }
            R.id.about -> {
                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                intent.putExtra("OptionName", "about")
                startActivity(intent)
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, MenuActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

