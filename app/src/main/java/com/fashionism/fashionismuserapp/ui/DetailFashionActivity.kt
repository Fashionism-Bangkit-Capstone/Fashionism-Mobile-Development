package com.fashionism.fashionismuserapp.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.db.ProductDetail
import com.fashionism.fashionismuserapp.data.dummy.DummyData2
import com.fashionism.fashionismuserapp.databinding.ActivityDetailFashionBinding

class DetailFashionActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailFashionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFashionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<ProductDetail>(EXTRA_FASHION_ITEM)

        binding.tvDetailNameProduct.text = data?.product
        binding.tvDetaiilPriceProduct.text = data?.price
        binding.tvDescriptionProduct.text = data?.description
        Glide.with(this)
            .load(data?.imageFashion)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.ivProductImage)

    }

    companion object {
        const val EXTRA_FASHION_ITEM = "extra_fashion_item"
    }
}