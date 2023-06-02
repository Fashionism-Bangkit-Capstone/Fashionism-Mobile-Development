package com.alcorp.fashionism_umkm.ui.home.add_or_edit_outfit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.databinding.ActivityAddEditOutfitBinding
import com.alcorp.fashionism_umkm.ui.home.add_or_edit_outfit.camera.CameraActivity
import com.alcorp.fashionism_umkm.utils.Helper.showToast
import com.alcorp.fashionism_umkm.utils.UploadImage.rotateFile
import com.alcorp.fashionism_umkm.utils.UploadImage.uriToFile
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.UploadImage.reduceFileImage
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddEditOutfitActivity : AppCompatActivity(), View.OnClickListener {

    private var getFile: File? = null
    private lateinit var binding: ActivityAddEditOutfitBinding
    private lateinit var loadingDialog: LoadingDialog
    private val addEditOutfitViewModel: AddEditOutfitViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            getFile = myFile

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)

                Glide.with(this@AddEditOutfitActivity)
                    .load(BitmapFactory.decodeFile(file.path))
                    .into(binding.ivPreviewImage)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddEditOutfitActivity)

            getFile = myFile
            Glide.with(this@AddEditOutfitActivity)
                .load(selectedImg)
                .into(binding.ivPreviewImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditOutfitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        init()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                showToast(this, getString(R.string.toast_permission))
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun init() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        supportActionBar?.title = getString(R.string.title_add_outfit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupView() {
        loadingDialog = LoadingDialog(this)

        binding.btnUpload.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.txt_choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_upload_title))
        builder.setMessage(getString(R.string.dialog_upload_message))
        builder.setPositiveButton(getString(R.string.dialog_camera)) { _, _ -> startCameraX() }
        builder.setNegativeButton(getString(R.string.dialog_gallery)) { _, _ -> startGallery() }
        builder.setNeutralButton(getString(R.string.dialog_cancel)) { dialog, _ -> dialog.cancel() }
        builder.create().show()
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnUpload -> uploadDialog()
            binding.btnSubmit -> {
                if (getFile != null) {
                    val txtOutfitName = binding.edtOutfitName.text.toString()

                    if (txtOutfitName != "") {

                        val file = reduceFileImage(getFile as File)

                        val description = txtOutfitName . toRequestBody("text/plain" . toMediaType())
                        val requestImageFile = file.asRequestBody("image/jpeg" . toMediaTypeOrNull())
                        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "photo",
                            file.name,
                            requestImageFile
                        )

//                        addViewModel.addStory(PrefData.token, description, imageMultiPart, lat, lon)
//                        addViewModel.storyResponse.observe(this) {
//                            setResult(1)
//                            finish()
//                        }
//                        addViewModel.isLoading.observe(this) {
//                            showLoading(it)
//                        }
//                        addViewModel.message.observe(this ) {
//                            Toast.makeText(this@AddStoryActivity, it, Toast.LENGTH_SHORT).show()
//                        }
                    } else {
                        Toast.makeText(this, getString(R.string.toast_insert_columns), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.toast_insert_image), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}