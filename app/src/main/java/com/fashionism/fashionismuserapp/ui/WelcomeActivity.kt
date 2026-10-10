package com.fashionism.fashionismuserapp.ui

import android.content.Intent
import com.fashionism.fashionismuserapp.tools.setStatusBarColorCompat
import com.fashionism.fashionismuserapp.tools.startActivityWithAnim
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
        setStatusBarColorCompat(ContextCompat.getColor(this, R.color.button_colorbackground))

        binding.switchOnOff.setOnCheckedChangeListener { _, checked ->
            val gradientBackground = if (checked) {
                R.drawable.gradient_background
            } else {
                R.drawable.gradient_background_2
            }
            binding.root.setBackgroundResource(gradientBackground)

            val windowColor = if (checked) {
                R.color.button_colorbackground
            } else {
                R.color.welcome_statusbarcolor
            }

            setStatusBarColorCompat(ContextCompat.getColor(this, windowColor))

            // Ubah juga warna teks berdasarkan status switch
            binding.tvSwitchYes.setTextColor(
                ContextCompat.getColor(
                    this@WelcomeActivity,
                    if (checked) R.color.switch_false_colortext else R.color.switch_true_colortext
                )
            )
            binding.tvSwitchNo.setTextColor(
                ContextCompat.getColor(
                    this@WelcomeActivity,
                    if (checked) R.color.switch_true_colortext else R.color.switch_false_colortext
                )
            )
        }

        binding.tvSwitchNo.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.tvSwitchYes.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

    }
}