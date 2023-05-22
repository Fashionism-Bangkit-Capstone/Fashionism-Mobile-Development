package com.fashionism.fashionismclient.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fashionism.fashionismclient.R
import com.fashionism.fashionismclient.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // installSplashScreen()
        setContentView(R.layout.activity_splash_screen)

        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(2000)
                    val loginIntent = Intent(baseContext, LoginActivity::class.java)
                    startActivity(loginIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}