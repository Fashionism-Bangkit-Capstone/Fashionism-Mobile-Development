package com.alcorp.fashionism_umkm.ui.auth.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alcorp.fashionism_umkm.MainActivity
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.databinding.ActivitySignUpBinding
import com.alcorp.fashionism_umkm.utils.Helper
import com.alcorp.fashionism_umkm.utils.Helper.showToast
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.Status
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var loadingDialog: LoadingDialog
    private val signUpViewModel: SignUpViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        init()
    }

    private fun init() {
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupView() {
        loadingDialog = LoadingDialog(this)
        binding.tvLogin.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
    }

    private fun signUpUser() {
        val name = binding.edtShopName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confPassword = binding.edtKonfPassword.text.toString().trim()

        if (name != "") {
            if (Helper.checkEmailFormat(email)) {
                if (password.length >= 6) {
                    if (password == confPassword) {
                        lifecycleScope.launch {
                            signUpViewModel.signUpUser(name, email, password)
                            signUpViewModel.signUpState.collect {
                                when (it.status) {
                                    Status.LOADING -> loadingDialog.showLoading(true)

                                    Status.SUCCESS -> {
                                        loadingDialog.showLoading(false)
                                        it.data?.let { data ->
                                            showToast(this@SignUpActivity, data.message.toString())
                                            finish()
                                        }
                                    }

                                    else -> {
                                        loadingDialog.showLoading(false)
                                        showToast(this@SignUpActivity, it.message.toString())
                                    }
                                }
                            }
                        }
                    } else {
                        showToast(this, getString(R.string.toast_password_match))
                    }
                } else {
                    showToast(this, getString(R.string.toast_password_min_length))
                }
            } else {
                showToast(this, getString(R.string.toast_email_validate))
            }
        } else {
            showToast(this, getString(R.string.toast_insert_columns))
        }
    }

    override fun onClick(view: View) {
        when(view) {
            binding.tvLogin -> finish()
            binding.btnSignUp -> { signUpUser() }
        }
    }
}