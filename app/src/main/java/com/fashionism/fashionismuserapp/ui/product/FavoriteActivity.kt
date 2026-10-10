package com.fashionism.fashionismuserapp.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.adapter.FashionItemAdapter
import com.fashionism.fashionismuserapp.data.model.Product
import com.fashionism.fashionismuserapp.databinding.ActivityFavoriteBinding
import com.fashionism.fashionismuserapp.tools.GridSpacingItemDecoration
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.finishWithAnim
import com.fashionism.fashionismuserapp.ui.common.collectWhenStarted
import com.fashionism.fashionismuserapp.ui.common.showError
import com.fashionism.fashionismuserapp.ui.product.DetailFashionActivity.Companion.EXTRA_FASHION_ITEM
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFavoriteBinding.inflate(layoutInflater)
    }

    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBackFavorite.setOnClickListener {
            finishWithAnim(R.anim.slidefromleft_in, R.anim.slidefromleft_out)
        }

        with(binding.rvFavoriteItem) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
            val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
            addItemDecoration(GridSpacingItemDecoration(spacing, true))
        }

        viewModel.favorites.observe(this) { products ->
            binding.llNoDataFavorite.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
            binding.rvFavoriteItem.adapter = FashionItemAdapter(products, true).apply {
                setOnItemClickCallback(object : FashionItemAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Product) = showSelectedFavorite(data)
                })
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading, binding.progressBarFavorite)
        }

        collectWhenStarted(viewModel.errors) { showError(it) }
    }

    private fun showSelectedFavorite(fashionItem: Product) {
        val intent = Intent(this, DetailFashionActivity::class.java)
        intent.putExtra(EXTRA_FASHION_ITEM, fashionItem)
        startActivity(intent)
    }
}
