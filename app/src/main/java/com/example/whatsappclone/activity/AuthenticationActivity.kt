package com.example.whatsappclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.whatsappclone.Login
import com.example.whatsappclone.MainActivity
import com.example.whatsappclone.SignUp
import com.example.whatsappclone.databinding.ActivityAuthenticationBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
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

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
        if(FirebaseAuth.getInstance().currentUser != null){
            startMainActivity()
        }
    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        if(p0.currentUser != null){
            startMainActivity()
        }
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }
    private fun startMainActivity() {
        val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
        startActivity(intent)
    }
}