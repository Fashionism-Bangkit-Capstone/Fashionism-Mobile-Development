package com.alcorp.fashionism_umkm.ui.home.detail_outfit

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alcorp.fashionism_umkm.MainActivity
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.data.remote.response.DetailOutfitData
import com.alcorp.fashionism_umkm.databinding.ActivityDetailOutfitBinding
import com.alcorp.fashionism_umkm.ui.home.add_or_edit_outfit.AddEditOutfitActivity
import com.alcorp.fashionism_umkm.ui.home.add_or_edit_outfit.AddEditOutfitActivity.Companion.EXTRA_EDIT_OUTFIT
import com.alcorp.fashionism_umkm.utils.Helper.showToast
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.PrefData
import com.alcorp.fashionism_umkm.utils.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DetailOutfitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOutfitBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var id: String
    private lateinit var detailOutfitData: DetailOutfitData
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
        id = intent.getStringExtra(EXTRA_DETAIL_OUTFIT).toString()

        loadingDialog = LoadingDialog(this)
    }

    private fun loadData() {
        lifecycleScope.launch {
            detailOutfitViewModel.getOutfitDetail(PrefData.token, PrefData.idUser, id)
            detailOutfitViewModel.detailOutfitState.collect {
                when (it.status) {
                    Status.LOADING -> loadingDialog.showLoading(true)

                    Status.SUCCESS -> {
                        loadingDialog.showLoading(false)
                        it.data?.let { detail ->
                            detailOutfitData = DetailOutfitData(detail.data?.id, detail.data?.name, detail.data?.description, detail.data?.stock, detail.data?.price, detail.data?.product_image)
                            Glide.with(this@DetailOutfitActivity)
                                .load(detail.data?.product_image)
                                .placeholder(ContextCompat.getDrawable(this@DetailOutfitActivity, R.drawable.default_image))
                                .error(ContextCompat.getDrawable(this@DetailOutfitActivity, R.drawable.default_image))
                                .into(binding.ivOutfit)

                            binding.tvOutfitDetailTitle.text = detail.data?.name
                            binding.tvDesc.text = detail.data?.description
                            binding.tvStock.text = detail.data?.stock
                            binding.tvPrice.text = detail.data?.price
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

    private fun deleteData() {
        lifecycleScope.launch {
            detailOutfitViewModel.deleteOutfit(PrefData.token, PrefData.idUser, id)
            detailOutfitViewModel.deleteOutfitState.collect {
                when (it.status) {
                    Status.LOADING -> loadingDialog.showLoading(true)

                    Status.SUCCESS -> {
                        loadingDialog.showLoading(false)
                        it.data?.let { data ->
                            showToast(this@DetailOutfitActivity, data.message.toString())
                            val intent = Intent(this@DetailOutfitActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    else -> {
                        loadingDialog.showLoading(false)
                        showToast(this@DetailOutfitActivity, it.data?.message.toString())
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@DetailOutfitActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_outfit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_update -> {
                val intent = Intent(this, AddEditOutfitActivity::class.java)
                intent.putExtra(EXTRA_EDIT_OUTFIT, detailOutfitData)
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_delete -> {
                deleteData()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_DETAIL_OUTFIT = "extra_detail_outfit"
    }
}