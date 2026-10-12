package com.fashionism.fashionismuserapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.model.FashionDataML
import com.fashionism.fashionismuserapp.data.model.FashionRecommendation
import com.fashionism.fashionismuserapp.databinding.ActivityResultSearchBinding
import com.fashionism.fashionismuserapp.tools.FashionData
import com.fashionism.fashionismuserapp.tools.Helper.shortenText
import com.fashionism.fashionismuserapp.tools.finishWithAnim
import com.fashionism.fashionismuserapp.tools.startActivityWithAnim
import com.fashionism.fashionismuserapp.ui.common.showToast
import com.fashionism.fashionismuserapp.ui.product.DetailFashionActivity

class ResultSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultSearchBinding

    /** One recommended item: image and price from the ML service, text from the local catalogue. */
    private data class Item(val image: String, val price: String, val info: FashionDataML)

    private class Card(val root: View, val image: ImageView, val name: TextView, val store: TextView, val price: TextView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCloseResult.setOnClickListener {
            finishWithAnim(R.anim.slidefromleft_in, R.anim.slidefromleft_out)
        }

        val recommendation = IntentCompat.getParcelableExtra(intent, EXTRA_RECOMMENDATION, FashionRecommendation::class.java)
        val items = recommendation?.let(::toItems).orEmpty()
        if (items.isEmpty()) {
            showToast(getString(R.string.recommendationEmpty))
            finish()
            return
        }

        // First item is the large match; the rest fill the "similar" cards, unused cards are hidden
        val main = items.first()
        loadImage(main.image, binding.ivResultSearch)
        binding.ivResultSearch.setOnClickListener { openDetail(main) }

        val cards = with(binding) {
            listOf(
                Card(CV1, ivFashionImage, tvFashionName, tvStoreName, tvPrice),
                Card(CV2, ivFashionImage2, tvFashionName2, tvStoreName2, tvPrice2),
                Card(CV3, ivFashionImage3, tvFashionName3, tvStoreName3, tvPrice3),
                Card(CV4, ivFashionImage4, tvFashionName4, tvStoreName4, tvPrice4),
            )
        }
        val similar = items.drop(1)
        binding.tvSimiliarSearch.visibility = if (similar.isEmpty()) View.GONE else View.VISIBLE
        cards.forEachIndexed { i, card ->
            val item = similar.getOrNull(i)
            card.root.visibility = if (item == null) View.GONE else View.VISIBLE
            if (item != null) {
                card.name.text = shortenText(item.info.name, 14)
                card.store.text = item.info.storeName
                card.price.text = item.price
                loadImage(item.image, card.image)
                card.root.setOnClickListener { openDetail(item) }
            }
        }
    }

    /** The service may return fewer than 5 results, or lists of different lengths: pair only what exists. */
    private fun toItems(recommendation: FashionRecommendation): List<Item> {
        val count = minOf(recommendation.target_link.size, recommendation.price_output.size, MAX_ITEMS)
        val catalogue = FashionData.fashionValue.shuffled()
        return List(minOf(count, catalogue.size)) { i ->
            Item(recommendation.target_link[i], recommendation.price_output[i], catalogue[i])
        }
    }

    private fun loadImage(url: String, into: ImageView) {
        Glide.with(this)
            .load(url)
            .transform(CenterCrop(), RoundedCorners(12))
            .into(into)
    }

    private fun openDetail(item: Item) {
        val intent = Intent(this, DetailFashionActivity::class.java)
            .putExtra("imageRecommendation", item.image)
            .putExtra("priceRecommendation", item.price)
            .putExtra("nameRecommendation", item.info.name)
            .putExtra("storeNameRecommendation", item.info.storeName)
            .putExtra("descriptionRecommendation", item.info.description)
        startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
    }

    companion object {
        const val EXTRA_RECOMMENDATION = "fashionOutput"
        private const val MAX_ITEMS = 5
    }
}
