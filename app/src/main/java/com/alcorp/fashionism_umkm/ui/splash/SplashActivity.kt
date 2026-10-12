package com.alcorp.fashionism_umkm.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.databinding.ActivitySplashBinding
import com.alcorp.fashionism_umkm.ui.auth.login.LoginActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.alcorp.fashionism_umkm.utils.setupEdgeToEdge
import com.alcorp.fashionism_umkm.utils.hideStatusBar

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root, padTop = false)

        init()
    }

    private fun init() {
        setupToolbar()
        setupView()
        setDelay()
    }

    private fun setupToolbar() {
        hideStatusBar()
        supportActionBar?.hide()
    }

    private fun setupView() {
        Glide.with(this)
            .load(R.drawable.logo)
            .apply(RequestOptions().override(300, 300))
            .into(binding.ivIcon)
    }

    private fun setDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}