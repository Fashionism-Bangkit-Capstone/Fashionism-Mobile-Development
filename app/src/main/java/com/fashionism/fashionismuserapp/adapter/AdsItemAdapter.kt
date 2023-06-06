package com.fashionism.fashionismuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fashionism.fashionismuserapp.data.dummy.DummyData2
import com.fashionism.fashionismuserapp.databinding.ItemFavoriteBinding

class AdsItemAdapter(private val list: List<DummyData2>) :
    RecyclerView.Adapter<AdsItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DummyData2) {
            binding.ivProductFavImage.setImageResource(data.imageOne)
            binding.productFavTitle.text = data.name
            binding.productFavPrice.text = data.price
            binding.productFavStoreName.text = data.storeName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}