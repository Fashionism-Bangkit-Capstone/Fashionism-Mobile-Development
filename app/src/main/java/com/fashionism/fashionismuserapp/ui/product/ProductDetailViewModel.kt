package com.fashionism.fashionismuserapp.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fashionism.fashionismuserapp.data.repository.ProductRepository
import com.fashionism.fashionismuserapp.ui.common.BaseViewModel

class ProductDetailViewModel(private val repository: ProductRepository) : BaseViewModel() {

    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var productId = 0

    /** Products opened from a recommendation have no id (0) and cannot be favorited. */
    fun load(productId: Int) {
        if (productId == 0 || productId == this.productId) return
        this.productId = productId
        request(call = { repository.isFavorite(productId) }, showLoading = false) { _isFavorite.value = it }
    }

    fun toggleFavorite() {
        if (productId == 0) return
        val favorite = _isFavorite.value != true
        _isFavorite.value = favorite // update the heart right away, roll back if the request fails
        request(
            call = { repository.setFavorite(productId, favorite) },
            showLoading = false,
            onError = { _isFavorite.value = !favorite; emitError(it) },
        ) {}
    }
}
