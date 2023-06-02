package com.fashionism.fashionismuserapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fashionism.fashionismuserapp.data.dummy.DummyData2
import com.fashionism.fashionismuserapp.databinding.ItemAdsBinding

class AdsItemAdapter(private val list: List<DummyData2>, private val context: Context) :
    RecyclerView.Adapter<AdsItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemAdsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DummyData2) {
            binding.ivTopbar.setImageResource(data.imageOne)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAdsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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