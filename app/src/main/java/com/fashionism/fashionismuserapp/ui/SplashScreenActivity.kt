package com.fashionism.fashionismuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.session.UserSession
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModel
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModelFactory
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModel
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModelFactory
import com.fashionism.fashionismuserapp.tools.LanguageUtil.applyLanguage

class SplashScreenActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLanguage(this)
        setContentView(R.layout.activity_splash_screen)

        val pref = UserSession.getInstance(dataStore)
        val loginViewModel =
            ViewModelProvider(
                this,
                UserSessionViewModelFactory(pref)
            )[UserSessionViewModel::class.java]

        mainViewModel.userProfile.observe(this) {
            loginViewModel.saveName(it.data.name)
            loginViewModel.saveEmail(it.data.email)
        }

        loginViewModel.getLoginSession().observe(this) { isLoggedIn ->

            // Check login status before starting MainActivity or HomeActivity
            val intent = if (isLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, WelcomeActivity::class.java)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, 3000) // waktu delay dalam milidetik sebelum pindah ke LoginActivity

        }
    }
}