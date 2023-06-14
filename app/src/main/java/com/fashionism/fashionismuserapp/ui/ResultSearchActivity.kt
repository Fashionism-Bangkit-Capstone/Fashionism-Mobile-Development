package com.fashionism.fashionismuserapp.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.db.FashionRecommendation
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModel
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModelFactory
import com.fashionism.fashionismuserapp.databinding.ActivityResultSearchBinding
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import java.io.File

class ResultSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultSearchBinding

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    private lateinit var resultFashion: FashionRecommendation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultFashion = intent.getParcelableExtra<FashionRecommendation>("fashionOutput")!!
        if (resultFashion != null) {

            // item 1
            Glide.with(this)
                .load(resultFashion.target_link[0])
                .transform(CenterCrop(), RoundedCorners(12))
                .into(binding.ivResultSearch)

            // item 2
            binding.tvPrice.text = resultFashion.price_output[1]
            Glide.with(this)
                .load(resultFashion.target_link[1])
                .transform(CenterCrop(), RoundedCorners(12))
                .into(binding.ivFashionImage)

            // item 3
            binding.tvPrice2.text = resultFashion.price_output[2]
            Glide.with(this)
                .load(resultFashion.target_link[2])
                .transform(CenterCrop(), RoundedCorners(12))
                .into(binding.ivFashionImage2)

            // item 4
            binding.tvPrice3.text = resultFashion.price_output[3]
            Glide.with(this)
                .load(resultFashion.target_link[3])
                .transform(CenterCrop(), RoundedCorners(12))
                .into(binding.ivFashionImage3)

            // item 5
            binding.tvPrice4.text = resultFashion.price_output[4]
            Glide.with(this)
                .load(resultFashion.target_link[4])
                .transform(CenterCrop(), RoundedCorners(12))
                .into(binding.ivFashionImage4)
        }

        onActionClick()

//        val imageUrlString = intent.getStringExtra("imageUrl")
//        if (imageUrlString != null) {
//            Glide.with(this)
//                .load(imageUrlString)
//                .transform(CenterCrop(), RoundedCorners(12))
//                .into(binding.ivResultSearch)
//        }

    }

    private fun onActionClick() {
        binding.btnCloseResult.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slidefromleft_in, R.anim.slidefromleft_out)
        }

        binding.ivResultSearch.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[0])
            intent.putExtra("priceRecommendation", resultFashion.price_output[0])
            startActivity(intent)
            overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.CV1.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[1])
            intent.putExtra("priceRecommendation", resultFashion.price_output[1])
            startActivity(intent)
            overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.CV2.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[2])
            intent.putExtra("priceRecommendation", resultFashion.price_output[2])
            startActivity(intent)
            overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.CV3.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[3])
            intent.putExtra("priceRecommendation", resultFashion.price_output[3])
            startActivity(intent)
            overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.CV4.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[4])
            intent.putExtra("priceRecommendation", resultFashion.price_output[4])
            startActivity(intent)
            overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
        }
    }
}