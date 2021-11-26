package com.example.billingapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billingapp.databinding.ListItemDisplayBinding
import com.example.billingapp.viewModels.ListViewModel

class OrderedItemsAdapter(private val viewModel: ListViewModel) :
    RecyclerView.Adapter<OrderedItemsAdapter.OrderedItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedItemsViewHolder =
        OrderedItemsViewHolder(
            ListItemDisplayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: OrderedItemsViewHolder, position: Int) {
        holder.bind(viewModel.orderedItems[position])
    }

    override fun getItemCount(): Int = viewModel.orderedItems.size

    inner class OrderedItemsViewHolder(private val binding: ListItemDisplayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: String) {
            binding.apply {
                val price = viewModel.priceList[item].toString()
                nameTextView.text = item + "       Price: ${price}"
            }
        }
    }
}