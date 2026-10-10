package com.fashionism.fashionismuserapp.ui.home

import android.os.Bundle
import androidx.core.content.IntentCompat
import com.fashionism.fashionismuserapp.tools.finishWithAnim
import com.fashionism.fashionismuserapp.tools.startActivityWithAnim
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.model.FashionRecommendation
import com.fashionism.fashionismuserapp.databinding.ActivityResultSearchBinding
import com.fashionism.fashionismuserapp.ui.product.DetailFashionActivity
import com.fashionism.fashionismuserapp.tools.FashionData
import com.fashionism.fashionismuserapp.tools.Helper.shortenText

class ResultSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultSearchBinding

    private lateinit var resultFashion: FashionRecommendation
    private var data1 = 0
    private var data2 = 0
    private var data3 = 0
    private var data4 = 0
    private var data5 = 0
    private val usedIndexes = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultFashion = IntentCompat.getParcelableExtra(intent, EXTRA_RECOMMENDATION, FashionRecommendation::class.java)!!

        // item 1
        data1 = getRandomIndex()
        usedIndexes.add(data1)
        Glide.with(this)
            .load(resultFashion.target_link[0])
            .transform(CenterCrop(), RoundedCorners(12))
            .into(binding.ivResultSearch)

        // item 2
        data2 = getRandomIndex()
        usedIndexes.add(data2)
        binding.tvFashionName.text = shortenText(FashionData.fashionValue[data2].name, 14)
        binding.tvStoreName.text = FashionData.fashionValue[data2].storeName
        binding.tvPrice.text = resultFashion.price_output[1]
        Glide.with(this)
            .load(resultFashion.target_link[1])
            .transform(CenterCrop(), RoundedCorners(12))
            .into(binding.ivFashionImage)

        // item 3
        data3 = getRandomIndex()
        usedIndexes.add(data3)
        binding.tvFashionName2.text = FashionData.fashionValue[data3].name
        binding.tvStoreName2.text = FashionData.fashionValue[data3].storeName
        binding.tvPrice2.text = resultFashion.price_output[2]
        binding
        Glide.with(this)
            .load(resultFashion.target_link[2])
            .transform(CenterCrop(), RoundedCorners(12))
            .into(binding.ivFashionImage2)

        // item 4
        data4 = getRandomIndex()
        usedIndexes.add(data4)
        binding.tvFashionName3.text = FashionData.fashionValue[data4].name
        binding.tvStoreName3.text = FashionData.fashionValue[data4].storeName
        binding.tvPrice3.text = resultFashion.price_output[3]
        Glide.with(this)
            .load(resultFashion.target_link[3])
            .transform(CenterCrop(), RoundedCorners(12))
            .into(binding.ivFashionImage3)

        // item 5
        data5 = getRandomIndex()
        usedIndexes.add(data5)
        binding.tvFashionName4.text = FashionData.fashionValue[data5].name
        binding.tvStoreName4.text = FashionData.fashionValue[data5].storeName
        binding.tvPrice4.text = resultFashion.price_output[4]
        Glide.with(this)
            .load(resultFashion.target_link[4])
            .transform(CenterCrop(), RoundedCorners(12))
            .into(binding.ivFashionImage4)

        onActionClick()
    }

    private fun getRandomIndex(): Int {
        val availableIndexes =
            (0 until FashionData.fashionValue.size).filterNot { usedIndexes.contains(it) }
        return availableIndexes.random()
    }

    private fun onActionClick() {
        binding.btnCloseResult.setOnClickListener {
            finishWithAnim(R.anim.slidefromleft_in, R.anim.slidefromleft_out)
        }

        binding.ivResultSearch.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[0])
            intent.putExtra("priceRecommendation", resultFashion.price_output[0])
            intent.putExtra("nameRecommendation", FashionData.fashionValue[data1].name)
            intent.putExtra("storeNameRecommendation", FashionData.fashionValue[data1].storeName)
            intent.putExtra(
                "descriptionRecommendation",
                FashionData.fashionValue[data1].description
            )
            startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.CV1.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[1])
            intent.putExtra("priceRecommendation", resultFashion.price_output[1])
            intent.putExtra("nameRecommendation", FashionData.fashionValue[data2].name)
            intent.putExtra("storeNameRecommendation", FashionData.fashionValue[data2].storeName)
            intent.putExtra(
                "descriptionRecommendation",
                FashionData.fashionValue[data2].description
            )
            startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.CV2.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[2])
            intent.putExtra("priceRecommendation", resultFashion.price_output[2])
            intent.putExtra("nameRecommendation", FashionData.fashionValue[data3].name)
            intent.putExtra("storeNameRecommendation", FashionData.fashionValue[data3].storeName)
            intent.putExtra(
                "descriptionRecommendation",
                FashionData.fashionValue[data3].description
            )
            startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.CV3.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[3])
            intent.putExtra("priceRecommendation", resultFashion.price_output[3])
            intent.putExtra("nameRecommendation", FashionData.fashionValue[data4].name)
            intent.putExtra("storeNameRecommendation", FashionData.fashionValue[data4].storeName)
            intent.putExtra(
                "descriptionRecommendation",
                FashionData.fashionValue[data4].description
            )
            startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.CV4.setOnClickListener {
            val intent = intent.setClass(this, DetailFashionActivity::class.java)
            intent.putExtra("imageRecommendation", resultFashion.target_link[4])
            intent.putExtra("priceRecommendation", resultFashion.price_output[4])
            intent.putExtra("nameRecommendation", FashionData.fashionValue[data5].name)
            intent.putExtra("storeNameRecommendation", FashionData.fashionValue[data5].storeName)
            intent.putExtra(
                "descriptionRecommendation",
                FashionData.fashionValue[data5].description
            )
            startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }
    }

    companion object {
        const val EXTRA_RECOMMENDATION = "fashionOutput"
    }
}
