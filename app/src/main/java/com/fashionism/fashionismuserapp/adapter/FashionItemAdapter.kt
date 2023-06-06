package com.fashionism.fashionismuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fashionism.fashionismuserapp.data.dummy.DummyData
import com.fashionism.fashionismuserapp.databinding.ItemFashionBinding

class FashionItemAdapter(private val list: List<DummyData>) :
    RecyclerView.Adapter<FashionItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemFashionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DummyData) {
            binding.ivFashionImage.setImageResource(data.imageOne)
            binding.tvFashionName.text = data.name
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
    }

    override fun getItemCount(): Int {
        return list.size
    }
}