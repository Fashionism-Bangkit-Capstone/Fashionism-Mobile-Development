package com.alcorp.fashionism_umkm.ui.transaction.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.databinding.ActivityProductBinding
import com.alcorp.fashionism_umkm.ui.home.detail_outfit.DetailOutfitActivity
import com.alcorp.fashionism_umkm.utils.Helper
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.Status
import kotlinx.coroutines.launch

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var id: String
    private val productViewModel: ProductViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setupToolbar()
        setupView()
        loadData()
    }

    private fun setupToolbar() {
        supportActionBar?.title = getString(R.string.title_product_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupView() {
        id = intent.getStringExtra(EXTRA_ID_TRANSACTION).toString()

        loadingDialog = LoadingDialog(this)
    }

    private fun loadData() {
        lifecycleScope.launch {
            productViewModel.getTransactionDetail(id)
            productViewModel.productState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.showLoading(true)
                    }

                    Status.SUCCESS -> {
                        loadingDialog.showLoading(false)
                        it.data?.let { data ->
                            val productAdapter = ProductAdapter(data.products!!)
                            binding.rvProduct.setHasFixedSize(true)
                            binding.rvProduct.layoutManager = LinearLayoutManager(this@ProductActivity)
                            binding.rvProduct.adapter = productAdapter
                        }
                    }

                    else -> {
                        loadingDialog.showLoading(false)
                        Helper.showToast(this@ProductActivity, it.message.toString())
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
        const val EXTRA_ID_TRANSACTION = "extra_id_transaction"
    }
}