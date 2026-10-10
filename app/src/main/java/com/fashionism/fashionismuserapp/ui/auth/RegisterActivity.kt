package com.fashionism.fashionismuserapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.databinding.ActivityRegisterBinding
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.finishWithAnim
import com.fashionism.fashionismuserapp.tools.startActivityWithAnim
import com.fashionism.fashionismuserapp.ui.common.collectWhenStarted
import com.fashionism.fashionismuserapp.ui.common.showError
import com.fashionism.fashionismuserapp.ui.common.showToast
import com.fashionism.fashionismuserapp.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBarRegister)
        }

        collectWhenStarted(viewModel.events) { event ->
            when (event) {
                AuthViewModel.Event.Registered -> showToast(getString(R.string.registerSuccess))
                is AuthViewModel.Event.LoggedIn -> openMain()
            }
        }

        collectWhenStarted(viewModel.errors) { error ->
            if (error.message?.contains("already") == true) {
                binding.inputEmailRegisterField.error = getString(R.string.emailAlreadyExist)
            }
            showError(error)
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            registerBtn.setOnClickListener {
                inputNameRegisterField.clearFocus()
                inputEmailRegisterField.clearFocus()
                inputPasswordRegisterField.clearFocus()
                inputRetypePasswordRegisterField.clearFocus()

                if (isDataValid()) {
                    viewModel.register(
                        inputNameRegisterField.text.toString().trim(),
                        inputEmailRegisterField.text.toString().trim(),
                        inputPasswordRegisterField.text.toString().trim(),
                    )
                }
            }

            llLoginNavigate.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivityWithAnim(intent, R.anim.slide_up_enter, R.anim.slide_up_exit)
            }

            btnBackRegister.setOnClickListener {
                finishWithAnim(R.anim.slidefromleft_in, R.anim.slidefromleft_out)
            }
        }
    }

    private fun isDataValid(): Boolean {
        val name = binding.inputNameRegisterField.text.toString().trim()
        val email = binding.inputEmailRegisterField.text.toString().trim()
        val phone = binding.inputPhoneRegisterField.text.toString().trim()
        val passwordEditText = binding.inputPasswordRegisterField
        val password = passwordEditText.text.toString().trim()
        val retypePasswordEditText = binding.inputRetypePasswordRegisterField
        val retypePassword = retypePasswordEditText.text.toString().trim()

        val nameErrorRes = if (name.isEmpty()) R.string.nameRequired else null
        val emailErrorRes = if (email.isEmpty()) R.string.emailRequired
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) R.string.emailInvalid
        else null

        val phoneErrorRes = if (phone.isEmpty()) R.string.phoneRequired
        else if (phone.length < 10 || phone.length > 13) R.string.phoneInvalid
        else null

        val passwordErrorRes = if (password.isEmpty()) R.string.passwordRequired
        else if (password.length < 6) R.string.passwordInvalid
        else null

        val retypePasswordErrorRes = if (retypePassword.isEmpty()) R.string.confirmPasswordRequired
        else if (retypePassword != password) R.string.confirmPasswordInvalid
        else null

        binding.inputNameRegister.error = nameErrorRes?.let { getString(it) }
        binding.inputEmailRegister.error = emailErrorRes?.let { getString(it) }
        binding.inputPhoneRegister.error = phoneErrorRes?.let { getString(it) }
        binding.inputPasswordRegister.error = passwordErrorRes?.let { getString(it) }
        binding.inputRetypePasswordRegister.error = retypePasswordErrorRes?.let { getString(it) }

        return nameErrorRes == null && emailErrorRes == null && phoneErrorRes == null && passwordErrorRes == null && retypePasswordErrorRes == null
    }

    private fun openMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
    }
}
