package com.fashionism.fashionismuserapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.databinding.ActivityWelcomeBinding
import com.fashionism.fashionismuserapp.tools.SwitchTrackTextDrawable

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchButton = binding.switch1

        val leftTextId = R.string.loginTitle // ID dari string untuk teks kiri
        val rightTextId = R.string.registerTitle // ID dari string untuk teks kanan

        val switchTrackTextDrawable = SwitchTrackTextDrawable(this, leftTextId, rightTextId)
        switchButton.trackDrawable = switchTrackTextDrawable

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
    }
}