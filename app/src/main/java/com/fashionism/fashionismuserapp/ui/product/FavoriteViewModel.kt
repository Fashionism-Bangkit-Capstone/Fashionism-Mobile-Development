package com.fashionism.fashionismuserapp.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fashionism.fashionismuserapp.data.model.Product
import com.fashionism.fashionismuserapp.data.repository.ProductRepository
import com.fashionism.fashionismuserapp.ui.common.BaseViewModel

class FavoriteViewModel(repository: ProductRepository) : BaseViewModel() {

    private val _favorites = MutableLiveData<List<Product>>()
    val favorites: LiveData<List<Product>> = _favorites

    init {
        request(call = { repository.getFavorites() }) { _favorites.value = it }
    }
}
