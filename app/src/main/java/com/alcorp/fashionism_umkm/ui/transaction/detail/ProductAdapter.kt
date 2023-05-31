package com.alcorp.fashionism_umkm.ui.transaction.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.fashionism_umkm.data.remote.response.ProductList
import com.alcorp.fashionism_umkm.databinding.ItemTransactionBinding

class ProductAdapter(private val listProduct: List<ProductList>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listProduct[position]) {
                binding.tvDate.text = this.title
                binding.tvName.text = "${this.quantity} X ${this.price} = ${this.total}"
            }
        }
    }

    override fun getItemCount(): Int = listProduct.size

    inner class ViewHolder(val binding: ItemTransactionBinding): RecyclerView.ViewHolder(binding.root)
}