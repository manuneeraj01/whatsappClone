package com.example.whatsappclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.whatsappclone.Login
import com.example.whatsappclone.SignUp
import com.example.whatsappclone.databinding.ActivityAuthenticationBinding
import com.google.android.material.tabs.TabLayoutMediator

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var viewPagerAdapter:AuthenticationPagerAdapter
    private var titles = arrayListOf("Login", "SignUp")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        viewPagerAdapter = AuthenticationPagerAdapter(this@AuthenticationActivity)
        binding.viewPager2Authentication.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayoutAuthentication, binding.viewPager2Authentication){tab, position->
            tab.text = titles[position]
        }.attach()




    }
    class AuthenticationPagerAdapter(fragmentActivity:FragmentActivity):FragmentStateAdapter(fragmentActivity){
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0->Login()
                1-> SignUp()
                else->Login()
            }
        }

    }
}