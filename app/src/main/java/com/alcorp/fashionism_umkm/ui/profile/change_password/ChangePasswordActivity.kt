package com.alcorp.fashionism_umkm.ui.profile.change_password

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.alcorp.fashionism_umkm.MainActivity
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.databinding.ActivityChangePasswordBinding
import com.alcorp.fashionism_umkm.databinding.ActivityLoginBinding
import com.alcorp.fashionism_umkm.ui.auth.login.LoginViewModel
import com.alcorp.fashionism_umkm.utils.Helper
import com.alcorp.fashionism_umkm.utils.Helper.showToast
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.PrefData
import com.alcorp.fashionism_umkm.utils.Status
import kotlinx.coroutines.launch

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var loadingDialog: LoadingDialog
    private val changePasswordViewModel: ChangePasswordViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        init()
    }

    private fun init() {
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        supportActionBar?.title = getString(R.string.title_change_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupView() {
        loadingDialog = LoadingDialog(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun submitData() {
        val passwordOld = binding.edtPasswordOld.text.toString().trim()
        val passwordNew = binding.edtPasswordNew.text.toString().trim()
        val passwordConf = binding.edtConfPassword.text.toString().trim()

        if (passwordOld.length >= 6 || passwordNew.length >= 6 || passwordConf.length >= 6) {
            if (passwordNew == passwordConf) {
                lifecycleScope.launch {
                    changePasswordViewModel.changePassword(PrefData.token, PrefData.idUser, passwordOld, passwordNew, passwordConf)
                    changePasswordViewModel.changePasswordState.collect {
                        when (it.status) {
                            Status.LOADING -> loadingDialog.showLoading(true)

                            Status.SUCCESS -> {
                                loadingDialog.showLoading(false)
                                showToast(this@ChangePasswordActivity, it.data?.message.toString())
                                finish()
                            }

                            else -> {
                                loadingDialog.showLoading(false)
                                showToast(this@ChangePasswordActivity, it.data?.message.toString())
                            }
                        }
                    }
                }
            } else {
                showToast(this, getString(R.string.toast_password_match))
            }
        } else {
            Toast.makeText(this, getString(R.string.toast_password_min_length), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnSubmit -> submitData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}