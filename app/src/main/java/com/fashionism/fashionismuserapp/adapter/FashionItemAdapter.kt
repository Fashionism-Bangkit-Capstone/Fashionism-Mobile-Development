package com.fashionism.fashionismuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.db.ProductDetail
import com.fashionism.fashionismuserapp.data.dummy.DummyData
import com.fashionism.fashionismuserapp.databinding.ItemFashionBinding

class FashionItemAdapter(private val list: List<ProductDetail>) :
    RecyclerView.Adapter<FashionItemAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ProductDetail)
    }

    class ViewHolder(private val binding: ItemFashionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductDetail) {
            Glide.with(itemView.context)
                .load(data.imageFashion)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(binding.ivFashionImage)
            binding.tvFashionName.text = data.product
            binding.tvPrice.text = data.price
            binding.tvStoreName.text = data.storeName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemFashionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(data)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(list[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}