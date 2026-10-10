package com.fashionism.fashionismuserapp.ui.profile

import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.databinding.ActivityChangeProfileBinding
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.PhotoPicker
import com.fashionism.fashionismuserapp.tools.finishWithAnim
import com.fashionism.fashionismuserapp.ui.common.collectWhenStarted
import com.fashionism.fashionismuserapp.ui.common.showError
import com.fashionism.fashionismuserapp.ui.common.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ChangeProfileActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityChangeProfileBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileViewModel by viewModel()

    private var newAvatar: File? = null
    private var isEditEnable = false

    private val photoPicker = PhotoPicker(this, { this }) { photo ->
        newAvatar = photo
        binding.profileImage.setImageURI(null) // the picker reuses file paths, so force a reload
        binding.profileImage.setImageURI(Uri.fromFile(photo))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.profile.observe(this) { profile ->
            binding.nameSeeProfileValue.text = profile.name
            binding.emailSeeProfileValue.text = profile.email
            binding.phoneSeeProfileValue.text = profile.phone
            binding.addressSeeProfileValue.text = profile.address
            if (newAvatar == null) {
                Glide.with(this)
                    .load(profile.avatar)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.profileImage)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading, binding.progressBarChangeProfile)
        }

        collectWhenStarted(viewModel.errors) { showError(it) }

        collectWhenStarted(viewModel.events) { event ->
            if (event == ProfileViewModel.Event.ProfileUpdated) {
                showToast(getString(R.string.profileUpdated))
            }
        }

        binding.editProfileBtn.setOnClickListener {
            isEditEnable = true
            binding.titleSeeProfile.text = resources.getString(R.string.editProfileBtnChangeProfile)
            binding.editProfileBtn.text = resources.getString(R.string.saveBtn)
            hiddenTextView()
            showEditText()
            binding.profileImage.strokeColor = ColorStateList.valueOf(Color.RED)
            binding.profileImage.strokeWidth = 3F
            binding.editImageIcon.visibility = View.VISIBLE
            viewModel.profile.value?.let { profile ->
                binding.inputNameProfileField.setText(profile.name)
                binding.inputEmailProfileField.setText(profile.email)
                binding.inputPhoneProfileField.setText(profile.phone)
                binding.inputAddressProfileField.setText(profile.address)
            }
        }

        binding.editImageIcon.setOnClickListener {
            if (isEditEnable) photoPicker.show()
        }

        binding.profileImage.setOnClickListener {
            if (isEditEnable) photoPicker.show()
        }

        binding.btnBackRegister.setOnClickListener {
            finishWithAnim(R.anim.slidefrombottom_in, R.anim.slidefrombottom_out)
        }

        binding.saveProfileBtn.setOnClickListener {
            if (isInputValid()) {
                window.setDimAmount(0.5f)
                showBottomSheetDialog()
            }
        }
    }

    private fun isInputValid(): Boolean {
        var isValid = true
        if (binding.inputNameProfileField.text.toString().isEmpty()) {
            binding.inputNameProfileField.error = resources.getString(R.string.nameRequired)
            isValid = false
        }
        if (binding.inputEmailProfileField.text.toString().isEmpty()) {
            binding.inputEmailProfileField.error = resources.getString(R.string.emailRequired)
            isValid = false
        }
        return isValid
    }

    private fun hiddenTextView() {
        binding.nameSeeProfileValue.visibility = View.GONE
        binding.emailSeeProfileValue.visibility = View.GONE
        binding.phoneSeeProfileValue.visibility = View.GONE
        binding.addressSeeProfileValue.visibility = View.GONE

        binding.nameSeeProfile.visibility = View.GONE
        binding.emailSeeProfile.visibility = View.GONE
        binding.phoneSeeProfile.visibility = View.GONE
        binding.addressSeeProfile.visibility = View.GONE
        binding.editProfileBtn.visibility = View.GONE
    }

    private fun showEditText() {
        binding.nameSeeEditProfile.visibility = View.VISIBLE
        binding.emailSeeEditProfile.visibility = View.VISIBLE
        binding.phoneSeeEditProfile.visibility = View.VISIBLE
        binding.addressSeeEditProfile.visibility = View.VISIBLE

        binding.inputNameProfile.visibility = View.VISIBLE
        binding.inputEmailProfile.visibility = View.VISIBLE
        binding.inputPhoneProfile.visibility = View.VISIBLE
        binding.inputAddressProfile.visibility = View.VISIBLE
        binding.saveProfileBtn.visibility = View.VISIBLE
    }

    private fun changeDataProfile() {
        viewModel.updateProfile(
            name = binding.inputNameProfileField.text.toString().trim(),
            email = binding.inputEmailProfileField.text.toString().trim(),
            phone = binding.inputPhoneProfileField.text.toString().trim(),
            address = binding.inputAddressProfileField.text.toString().trim(),
            avatar = newAvatar,
        )
    }

    private fun showBottomSheetDialog() {

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.popup_savedata)
        val save = bottomSheetDialog.findViewById<MaterialButton>(R.id.saveDataBtn)
        val close = bottomSheetDialog.findViewById<MaterialButton>(R.id.cancelDataChanges)

        bottomSheetDialog.show()

        save?.setOnClickListener {
            changeDataProfile()
            bottomSheetDialog.dismiss()
        }

        close?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

}