package com.fashionism.fashionismuserapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.db.ChangePassword
import com.fashionism.fashionismuserapp.data.session.UserSession
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModel
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModelFactory
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModel
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModelFactory
import com.fashionism.fashionismuserapp.databinding.ActivityChangePasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    private val userSessionViewModel by lazy {
        ViewModelProvider(
            this,
            UserSessionViewModelFactory(UserSession.getInstance(dataStore))
        )[UserSessionViewModel::class.java]
    }

    private var idUser = 0
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnBackChangePassword.setOnClickListener {
            finish()
        }

        binding.saveChangesPassword.setOnClickListener {
            if (isInputValid()) {
                showBottomSheetDialog()
            }
        }
    }

    private fun isInputValid(): Boolean {
        var isValid = true
        if (binding.passwordChangesProfileField.text.toString().isEmpty()) {
            binding.passwordChangesProfile.error = "Old Password is required"
            isValid = false
        }
        if (binding.newPasswordChangesField.text.toString().isEmpty()) {
            binding.newPasswordChanges.error = "New Password is required"
            isValid = false
        }
        if (binding.confirmNewPasswordChangesProfileField.text.toString().isEmpty()) {
            binding.confirmNewPasswordChangesProfile.error = "Confirm Password is required"
            isValid = false
        }
        if (binding.newPasswordChangesField.text.toString() != binding.confirmNewPasswordChangesProfileField.text.toString()) {
            binding.confirmNewPasswordChangesProfile.error =
                "Confirm Password must be same with New Password"
            isValid = false
        }
        return isValid
    }

    private fun changePassword() {
        val newPassword = ChangePassword(
            binding.passwordChangesProfileField.text.toString(),
            binding.newPasswordChangesField.text.toString(),
            binding.confirmNewPasswordChangesProfileField.text.toString()
        )
        userSessionViewModel.getAllUserData().observe(this) { dataUser ->
            idUser = dataUser.idUser
            token = dataUser.token
            mainViewModel.updatePassword(idUser, newPassword, token)
        }
    }

    private fun showBottomSheetDialog() {

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.popup_savedata)
        val warnIcon = bottomSheetDialog.findViewById<ShapeableImageView>(R.id.warnIconSave)
        val changeTitle =
            bottomSheetDialog.findViewById<MaterialTextView>(R.id.changeConfirmationNotify)
        val changeDesc =
            bottomSheetDialog.findViewById<MaterialTextView>(R.id.changeConfirmationNotifyDesc)
        val save = bottomSheetDialog.findViewById<MaterialButton>(R.id.saveDataBtn)
        val close = bottomSheetDialog.findViewById<MaterialButton>(R.id.cancelDataChanges)

        bottomSheetDialog.show()

        save?.setOnClickListener {
            Toast.makeText(applicationContext, "Password berhasil diubah", Toast.LENGTH_LONG).show()
            changePassword()
            bottomSheetDialog.dismiss()
        }

        close?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }
}