package com.example.whatsappclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.whatsappclone.AboutUsFragment
import com.example.whatsappclone.ProfileFragment
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMenuBinding
    private lateinit var optionValue:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //to avoid the screen lock or low screen brightness after some time.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityMenuBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        if (intent != null){
            optionValue = intent.getStringExtra("OptionName").toString()
            when(optionValue){
                "profile" ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ProfileFragment()).commit()
                    binding.toolbarMenu.title = "Profile"
                }
                "about" ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AboutUsFragment()).commit()
                    binding.toolbarMenu.title = "About Us"
                }
            }
        }

        binding.toolbarMenu
    }
}