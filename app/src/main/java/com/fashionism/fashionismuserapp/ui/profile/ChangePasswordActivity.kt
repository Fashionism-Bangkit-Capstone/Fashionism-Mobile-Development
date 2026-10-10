package com.fashionism.fashionismuserapp.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.databinding.ActivityChangePasswordBinding
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.finishWithAnim
import com.fashionism.fashionismuserapp.ui.common.collectWhenStarted
import com.fashionism.fashionismuserapp.ui.common.showError
import com.fashionism.fashionismuserapp.ui.common.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.profile.observe(this) { profile ->
            Glide.with(this)
                .load(profile.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.userImage)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBarChangePassword)
        }

        collectWhenStarted(viewModel.errors) { showError(it) }

        collectWhenStarted(viewModel.events) { event ->
            if (event is ProfileViewModel.Event.PasswordChanged) {
                showToast(event.message ?: getString(R.string.passwordChanged))
            }
        }

        binding.btnBackChangePassword.setOnClickListener {
            finishWithAnim(R.anim.slidefromleft_in, R.anim.slidefromleft_out)
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
            binding.passwordChangesProfile.error = resources.getString(R.string.oldPasswordRequired)
            isValid = false
        }
        if (binding.newPasswordChangesField.text.toString().isEmpty()) {
            binding.newPasswordChanges.error = resources.getString(R.string.passwordRequired)
            isValid = false
        }
        if (binding.confirmNewPasswordChangesProfileField.text.toString().isEmpty()) {
            binding.confirmNewPasswordChangesProfile.error =
                resources.getString(R.string.confirmPasswordRequired)
            isValid = false
        }
        if (binding.newPasswordChangesField.text.toString() != binding.confirmNewPasswordChangesProfileField.text.toString()) {
            binding.confirmNewPasswordChangesProfile.error =
                resources.getString(R.string.confirmPasswordInvalid)
            isValid = false
        }
        return isValid
    }

    private fun changePassword() {
        viewModel.changePassword(
            binding.passwordChangesProfileField.text.toString(),
            binding.newPasswordChangesField.text.toString(),
            binding.confirmNewPasswordChangesProfileField.text.toString()
        )
    }

    private fun showBottomSheetDialog() {

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.popup_savedata)
        val save = bottomSheetDialog.findViewById<MaterialButton>(R.id.saveDataBtn)
        val close = bottomSheetDialog.findViewById<MaterialButton>(R.id.cancelDataChanges)

        bottomSheetDialog.show()

        save?.setOnClickListener {
            changePassword()
            bottomSheetDialog.dismiss()
        }

        close?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }
}