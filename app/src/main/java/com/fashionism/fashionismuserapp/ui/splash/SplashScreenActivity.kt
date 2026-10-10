package com.fashionism.fashionismuserapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fashionism.fashionismuserapp.BuildConfig
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.repository.AuthRepository
import com.fashionism.fashionismuserapp.tools.LanguageUtil.applyLanguage
import com.fashionism.fashionismuserapp.ui.auth.WelcomeActivity
import com.fashionism.fashionismuserapp.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashScreenActivity : AppCompatActivity() {
    private val authRepository: AuthRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLanguage(this)
        setContentView(R.layout.activity_splash_screen)
        findViewById<TextView>(R.id.splashVersion).text =
            getString(R.string.app_version, BuildConfig.VERSION_NAME)

        // Tied to this activity: cancelled if it is closed early, so the next screen opens exactly once
        lifecycleScope.launch {
            delay(SPLASH_DURATION_MS)
            val next = if (authRepository.isLoggedIn()) MainActivity::class.java else WelcomeActivity::class.java
            startActivity(Intent(this@SplashScreenActivity, next))
            finish()
        }
    }

    private companion object {
        const val SPLASH_DURATION_MS = 3000L
    }
}
