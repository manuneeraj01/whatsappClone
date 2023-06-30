package com.example.whatsappclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var appPagerAdapter:AppPagerAdapter
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
            TabLayoutMediator(tabLayoutMain, viewPager2Main){
                tab, position->
                tab.text = titles[position]
            }.attach()
        }


    }
    class AppPagerAdapter(fragmentActivity : FragmentActivity):FragmentStateAdapter(fragmentActivity){
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0->Chats()
                2->Status()
                3->Calls()

                else->Chats()
            }
        }

    }
}

