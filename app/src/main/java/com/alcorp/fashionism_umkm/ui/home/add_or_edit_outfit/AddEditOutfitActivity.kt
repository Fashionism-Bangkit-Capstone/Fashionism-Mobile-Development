package com.alcorp.fashionism_umkm.ui.home.add_or_edit_outfit

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.databinding.ActivityAddEditOutfitBinding
import com.alcorp.fashionism_umkm.utils.LoadingDialog

class AddEditOutfitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditOutfitBinding
    private lateinit var loadingDialog: LoadingDialog
    private val addEditOutfitViewModel: AddEditOutfitViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditOutfitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        init()
    }

    private fun init() {
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        supportActionBar?.title = getString(R.string.title_add_outfit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupView() {
        loadingDialog = LoadingDialog(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}