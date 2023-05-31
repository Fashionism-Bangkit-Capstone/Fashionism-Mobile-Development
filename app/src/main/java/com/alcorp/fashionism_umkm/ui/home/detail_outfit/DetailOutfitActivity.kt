package com.alcorp.fashionism_umkm.ui.home.detail_outfit

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.databinding.ActivityDetailOutfitBinding
import com.alcorp.fashionism_umkm.utils.Helper.showToast
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.launch

class DetailOutfitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOutfitBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var id: String
    private val detailOutfitViewModel: DetailOutfitViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOutfitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setupToolbar()
        setupView()
        loadData()
    }

    private fun setupToolbar() {
        supportActionBar?.title = getString(R.string.title_detail_outfit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupView() {
        id = intent.getStringExtra(EXTRA_ID_OUTFIT).toString()

        loadingDialog = LoadingDialog(this)
    }

    private fun loadData() {
        lifecycleScope.launch {
            detailOutfitViewModel.getOutfitDetail(id)
            detailOutfitViewModel.detailOutfitState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.showLoading(true)
                    }

                    Status.SUCCESS -> {
                        loadingDialog.showLoading(false)
                        it.data?.let { data ->
                            Glide.with(this@DetailOutfitActivity)
                                .load(data.thumbnail)
                                .apply(
                                    RequestOptions
                                        .centerCropTransform()
                                        .placeholder(ContextCompat.getDrawable(this@DetailOutfitActivity, R.drawable.default_image))
                                        .error(ContextCompat.getDrawable(this@DetailOutfitActivity, R.drawable.default_image))
                                )
                                .into(binding.ivOutfit)

                            binding.tvOutfitDetailTitle.text = data.title
                            binding.tvDesc.text = data.description
                            binding.tvStock.text = data.stock
                            binding.tvPrice.text = data.price
                        }
                    }

                    else -> {
                        loadingDialog.showLoading(false)
                        showToast(this@DetailOutfitActivity, it.message.toString())
                    }
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
        const val EXTRA_ID_OUTFIT = "extra_id_outfit"
    }
}