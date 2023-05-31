package com.alcorp.fashionism_umkm.ui.transaction

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.fashionism_umkm.data.remote.response.CartsList
import com.alcorp.fashionism_umkm.databinding.ItemTransactionBinding
import com.alcorp.fashionism_umkm.ui.home.detail_outfit.DetailOutfitActivity
import com.alcorp.fashionism_umkm.ui.transaction.detail.ProductActivity

class TransactionAdapter(private val listTransaction: List<CartsList>): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listTransaction[position]) {
                binding.tvDate.text = this.id.toString()

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, ProductActivity::class.java)
                    intent.putExtra(DetailOutfitActivity.EXTRA_ID_OUTFIT, this.id.toString())
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = listTransaction.size

    inner class ViewHolder(val binding: ItemTransactionBinding): RecyclerView.ViewHolder(binding.root)
}