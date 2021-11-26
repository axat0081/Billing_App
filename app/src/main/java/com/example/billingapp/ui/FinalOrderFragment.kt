package com.example.billingapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billingapp.R
import com.example.billingapp.adapter.OrderedItemsAdapter
import com.example.billingapp.databinding.FragmentFinalOrderBinding
import com.example.billingapp.viewModels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FinalOrderFragment:Fragment(R.layout.fragment_final_order) {
    private var _binding: FragmentFinalOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<ListViewModel>()
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFinalOrderBinding.bind(view)
        val orderedItemsAdapter = OrderedItemsAdapter(viewModel)
        binding.apply {
            orderedItemsRecyclerview.apply {
                adapter = orderedItemsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.totalPrice.collectLatest { price->
                    totalPriceTextView.text = "Total Price: ${price}"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}