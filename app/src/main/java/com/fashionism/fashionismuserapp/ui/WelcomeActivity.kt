package com.fashionism.fashionismuserapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.button_colorbackground)

        binding.switchOnOff.setOnCheckedChangeListener { _, checked ->
            when {
                checked -> {
                    binding.tvSwitchYes.setTextColor(ContextCompat.getColor(this,R.color.switch_false_colortext))
                    binding.tvSwitchNo.setTextColor(ContextCompat.getColor(this,R.color.switch_true_colortext))
                }
                else -> {
                    binding.tvSwitchYes.setTextColor(ContextCompat.getColor(this,R.color.switch_true_colortext))
                    binding.tvSwitchNo.setTextColor(ContextCompat.getColor(this,R.color.switch_false_colortext))
                }
            }
        }

        binding.tvSwitchNo.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.tvSwitchYes.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}