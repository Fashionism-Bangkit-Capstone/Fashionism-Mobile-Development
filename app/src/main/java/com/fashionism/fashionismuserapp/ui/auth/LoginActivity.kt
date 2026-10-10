package com.fashionism.fashionismuserapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.databinding.ActivityLoginBinding
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.finishWithAnim
import com.fashionism.fashionismuserapp.tools.setStatusBarBackground
import com.fashionism.fashionismuserapp.tools.startActivityWithAnim
import com.fashionism.fashionismuserapp.ui.common.collectWhenStarted
import com.fashionism.fashionismuserapp.ui.common.errorText
import com.fashionism.fashionismuserapp.ui.common.showToast
import com.fashionism.fashionismuserapp.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setStatusBarBackground(ContextCompat.getColor(this, R.color.login_statusBar))

        setupClickListeners()

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBarLogin)
        }

        collectWhenStarted(viewModel.events) { event ->
            if (event is AuthViewModel.Event.LoggedIn) {
                showToast(event.message)
                openMain()
            }
        }

        collectWhenStarted(viewModel.errors) { error ->
            val text = when (error.code) {
                401 -> getString(R.string.loginWrongPassword)
                404 -> getString(R.string.loginWrongEmail)
                else -> errorText(error)
            }
            showToast(text)
        }
    }

    private fun isDataValid(): Boolean {
        val email = binding.inputEmailLoginField.text.toString().trim()
        val password = binding.inputPasswordLoginField.text.toString().trim()

        val emailErrorRes = if (email.isEmpty()) R.string.emailRequired
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) R.string.emailInvalid
        else null

        val passwordErrorRes = if (password.isEmpty()) R.string.passwordRequired
        else if (password.length < 6) R.string.passwordInvalid
        else null

        binding.inputEmailLogin.error = emailErrorRes?.let { getString(it) }
        binding.inputPasswordLogin.error = passwordErrorRes?.let { getString(it) }

        return emailErrorRes == null && passwordErrorRes == null
    }

    private fun setupClickListeners() {
        with(binding) {
            loginBtn.setOnClickListener {
                inputEmailLogin.clearFocus()
                inputPasswordLogin.clearFocus()

                if (isDataValid()) {
                    viewModel.login(
                        inputEmailLoginField.text.toString().trim(),
                        inputPasswordLoginField.text.toString().trim()
                    )
                }
            }

            signUpNavigate.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivityWithAnim(intent, R.anim.slide_up_enter, R.anim.slide_up_exit)
            }

            backButtonLogin.setOnClickListener {
                finishWithAnim(R.anim.slidefromleft_in, R.anim.slidefromleft_out)
            }
        }
    }

    private fun openMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
    }
}
