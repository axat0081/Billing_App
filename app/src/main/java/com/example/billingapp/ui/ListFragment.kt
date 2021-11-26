package com.example.billingapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billingapp.R
import com.example.billingapp.adapter.ItemAdapter
import com.example.billingapp.databinding.FragmentListBinding
import com.example.billingapp.viewModels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), ItemAdapter.OnItemClickListener {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ListViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListBinding.bind(view)
        val itemAdapter = ItemAdapter(this, viewModel, requireContext())
        binding.apply {
            listRecyclerview.apply {
                adapter = itemAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.totalPrice.collectLatest { price ->
                    totalPriceTextView.text = "Total Price: ${price}"
                }
            }
        }
    }

    override fun onItemClick(item: String) {
        viewModel.onOrderClick(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}