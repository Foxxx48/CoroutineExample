package com.fox.coroutineexample.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fox.coroutineexample.R
import com.fox.coroutineexample.data.Item
import com.fox.coroutineexample.databinding.ItemBinding

class ItemAdapter :
    ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemDiffCallback) {

    class ItemViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val context = holder.binding.root.context
        val cryptoCurrency = getItem(position)
        holder.binding.textViewCurrencyName.text = cryptoCurrency.name
        holder.binding.textViewCurrencyPrice.text = context.getString(
            R.string.currency_price,
            "${cryptoCurrency.price}"
        )
    }

}

private object ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}