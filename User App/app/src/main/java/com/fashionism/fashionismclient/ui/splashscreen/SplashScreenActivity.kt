package com.fashionism.fashionismclient.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fashionism.fashionismclient.R
import com.fashionism.fashionismclient.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(Runnable {
            val i = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }, 3000) // waktu delay dalam milidetik sebelum pindah ke MainActivity
    }
}