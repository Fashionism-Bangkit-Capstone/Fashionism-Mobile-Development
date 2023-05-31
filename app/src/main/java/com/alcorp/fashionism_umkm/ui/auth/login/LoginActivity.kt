package com.alcorp.fashionism_umkm.ui.auth.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alcorp.fashionism_umkm.MainActivity
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.databinding.ActivityLoginBinding
import com.alcorp.fashionism_umkm.ui.auth.signup.SignUpActivity
import com.alcorp.fashionism_umkm.utils.Helper.showToast
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.Status
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        init()
    }

    private fun init() {
        setupToolbar()
        setupView()
        checkLogin()
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
        pref = getSharedPreferences("fashionism_umkm", MODE_PRIVATE)

        loadingDialog = LoadingDialog(this)
        binding.tvSignUp.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    private fun checkLogin() {
        val token = pref.getString("token", "")
        if (token != null && token != "") {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onClick(view: View) {
        when(view) {
            binding.tvSignUp -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            binding.btnLogin -> {
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()

//                if (checkEmailFormat(email)) {
//                    if (password.length >= 8) {
                        lifecycleScope.launch {
                            loginViewModel.loginUser(email, password)
                            loginViewModel.loginState.collect {
                                when (it.status) {
                                    Status.LOADING -> {
                                        loadingDialog.showLoading(true)
                                    }

                                    Status.SUCCESS -> {
                                        loadingDialog.showLoading(false)
                                        it.data?.let { data ->
                                            prefEdit = pref.edit()
                                            prefEdit.putInt("id", data.id!!)
                                            prefEdit.putString("image", data.image)
                                            prefEdit.putString("email", data.email)
                                            prefEdit.putString("name", data.username)
                                            prefEdit.putString("token", data.token)
                                            prefEdit.apply()

                                            showToast(this@LoginActivity, "Login Success")

                                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }

                                    else -> {
                                        loadingDialog.showLoading(false)
                                        showToast(this@LoginActivity, it.message.toString())
                                    }
                                }
                            }
                        }
//                    } else {
//                        Toast.makeText(this, getString(R.string.toast_password_min_length), Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this, getString(R.string.toast_email_validate), Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        loadingDialog.showLoading(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.showLoading(false)
    }
}

//hi my name is nicola yanni alivant. I have 4+ year experience coding in general and 2+ year experience in mobile development. i'm from trunojoyo university and my major is informatics engineering. when i in college i have handled certain projectl, my most project is web project but some is android project