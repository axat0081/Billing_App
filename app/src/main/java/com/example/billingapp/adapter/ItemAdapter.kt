package com.example.billingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.billingapp.R
import com.example.billingapp.databinding.ListItemDisplayBinding
import com.example.billingapp.viewModels.ListViewModel

class ItemAdapter(
    private val listener: OnItemClickListener,
    private val viewModel: ListViewModel,
    private val context: Context
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ListItemDisplayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(viewModel.itemList[position])
    }

    override fun getItemCount(): Int = viewModel.itemList.size

    interface OnItemClickListener {
        fun onItemClick(item: String)
    }

    inner class ItemViewHolder(private val binding: ListItemDisplayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = viewModel.itemList[position]
                    if (!viewModel.orderedItems.contains(item)){
                        binding.card.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.teal_200
                            )
                        )
                    }
                    else {
                        binding.card.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                    }
                    listener.onItemClick(item)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: String) {
            binding.apply {
                if(viewModel.orderedItems.contains(item)){
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.teal_200
                        )
                    )
                } else {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                }
                val price = viewModel.priceList[item].toString()
                nameTextView.text = item + "       Price: ${price}"
            }
        }
    }
}