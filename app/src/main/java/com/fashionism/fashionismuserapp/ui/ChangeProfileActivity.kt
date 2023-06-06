package com.fashionism.fashionismuserapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.db.ProfileDetail
import com.fashionism.fashionismuserapp.data.session.UserSession
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModel
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModelFactory
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModel
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModelFactory
import com.fashionism.fashionismuserapp.databinding.ActivityChangeProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView


class ChangeProfileActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityChangeProfileBinding.inflate(layoutInflater)
    }

    private val mainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    private var idUser = 0
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userSession = UserSession.getInstance(dataStore)
        val userSessionViewModel =
            ViewModelProvider(
                this,
                UserSessionViewModelFactory(userSession)
            )[UserSessionViewModel::class.java]

        userSessionViewModel.getAllUserData().observe(this) { dataUser ->
            idUser = dataUser.idUser
            token = dataUser.token
            mainViewModel.getProfile(idUser, token)
        }

        mainViewModel.userProfile.observe(this) { userProfile ->
            binding.nameSeeProfileValue.text = userProfile.data.name
            binding.emailSeeProfileValue.text = userProfile.data.email
            binding.phoneSeeProfileValue.text = userProfile.data.phone
            binding.addressSeeProfileValue.text = userProfile.data.address
            Glide.with(this)
                .load(userProfile.data.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.profileImage)
        }

        mainViewModel.message.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.editProfileBtn.setOnClickListener {
            binding.titleSeeProfile.text = resources.getString(R.string.editProfileBtnChangeProfile)
            binding.editProfileBtn.text = resources.getString(R.string.saveBtn)
            hiddenTextView()
            showEditText()
            mainViewModel.userProfile.observe(this) { userProfile ->
                binding.inputNameProfileField.setText(userProfile.data.name)
                binding.inputEmailProfileField.setText(userProfile.data.email)
                binding.inputPhoneProfileField.setText(userProfile.data.phone)
                binding.inputAddressProfileField.setText(userProfile.data.address)
            }
        }

        binding.btnBackRegister.setOnClickListener {
            finish()
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
            binding.inputNameProfileField.error = "Name is required"
            isValid = false
        }
        if (binding.inputEmailProfileField.text.toString().isEmpty()) {
            binding.inputEmailProfileField.error = "Email is required"
            isValid = false
        }
        return isValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarChangeProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    private fun showTextView() {
        binding.nameSeeProfileValue.visibility = View.VISIBLE
        binding.emailSeeProfileValue.visibility = View.VISIBLE
        binding.phoneSeeProfileValue.visibility = View.VISIBLE
        binding.addressSeeProfileValue.visibility = View.VISIBLE
    }

    private fun hiddenEditText() {
        binding.inputNameProfile.visibility = View.GONE
        binding.inputEmailProfile.visibility = View.GONE
        binding.inputPhoneProfile.visibility = View.GONE
        binding.inputAddressProfile.visibility = View.GONE
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
        val dataUser = ProfileDetail(
            idUser,
            binding.inputNameProfileField.text.toString(),
            binding.inputEmailProfileField.text.toString(),
            binding.inputPhoneProfileField.text.toString(),
            binding.inputAddressProfileField.text.toString()
        )
        mainViewModel.updateProfile(idUser, dataUser, token)
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
            Toast.makeText(applicationContext, "Data berhasil disimpan", Toast.LENGTH_LONG).show()
            changeDataProfile()
            bottomSheetDialog.dismiss()
        }

        close?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }
}