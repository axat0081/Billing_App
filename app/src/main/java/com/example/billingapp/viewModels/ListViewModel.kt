package com.example.billingapp.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor() : ViewModel() {
    var orderedItems = ArrayList<String>()
    var priceList = HashMap<String, Int>()
    var itemList = ArrayList<String>()

    init {
        var i = 1
        while (i <= 20) {
            priceList.put("Item $i", i * 10)
            itemList.add("Item $i")
            i++
        }
        orderedItems.clear()
    }

    val totalPrice = MutableStateFlow(0)

    fun onOrderClick(item: String) {
        if (orderedItems.contains(item)) {
            orderedItems.remove(item)
            totalPrice.value = totalPrice.value - priceList[item]!!
        } else {
            orderedItems.add(item)
            totalPrice.value = totalPrice.value + priceList[item]!!
        }
    }
}