package com.alcorp.fashionism_umkm.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.data.remote.response.OutfitData
import com.alcorp.fashionism_umkm.databinding.ItemOutfitBinding
import com.alcorp.fashionism_umkm.ui.home.detail_outfit.DetailOutfitActivity
import com.alcorp.fashionism_umkm.ui.home.detail_outfit.DetailOutfitActivity.Companion.EXTRA_DETAIL_OUTFIT
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class HomeAdapter(private val listOufit: List<OutfitData>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemOutfitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listOufit[position]) {
                Glide.with(itemView.context)
                    .load(this.product_image)
                    .placeholder(ContextCompat.getDrawable(itemView.context, R.drawable.default_image))
                    .error(ContextCompat.getDrawable(itemView.context, R.drawable.default_image))
                    .into(binding.ivOutfit)

                binding.tvOutfitTitle.text = this.name

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailOutfitActivity::class.java)
                    intent.putExtra(EXTRA_DETAIL_OUTFIT, this.id.toString())
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = listOufit.size

    inner class ViewHolder(val binding: ItemOutfitBinding): RecyclerView.ViewHolder(binding.root)
}