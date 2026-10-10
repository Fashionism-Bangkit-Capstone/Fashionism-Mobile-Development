package com.fashionism.fashionismuserapp.tools

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.ui.common.showToast
import java.io.File

/**
 * Gets a photo from the camera app or the system photo picker (no storage permission needed) as a File.
 * Create it as a property of the Activity/Fragment: result launchers must be registered before it starts.
 */
class PhotoPicker(
    caller: ActivityResultCaller,
    private val context: () -> Context,
    private val onPhoto: (File) -> Unit,
) {
    private var pendingCameraFile: File? = null

    private val takePicture = caller.registerForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        if (saved) pendingCameraFile?.let(onPhoto)
    }

    private val pickImage = caller.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let(::copyToCache)?.let(onPhoto)
    }

    /** Asks camera or gallery. */
    fun show() {
        val context = context()
        val options = arrayOf(
            context.getString(R.string.cameraOption),
            context.getString(R.string.fileManagerOption),
            context.getString(R.string.cancelBtn),
        )
        AlertDialog.Builder(context)
            .setTitle(R.string.chooseOneOption)
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> takePhoto()
                    1 -> pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun takePhoto() {
        val context = context()
        val file = File.createTempFile("photo_", ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        pendingCameraFile = file
        try {
            takePicture.launch(FileProvider.getUriForFile(context, context.packageName, file))
        } catch (e: ActivityNotFoundException) {
            context.showToast(context.getString(R.string.camera_unavailable))
        }
    }

    private fun copyToCache(uri: Uri): File? {
        val context = context()
        val file = File(context.cacheDir, "picked_photo.jpg")
        return context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { input.copyTo(it) }
            file
        }
    }
}
