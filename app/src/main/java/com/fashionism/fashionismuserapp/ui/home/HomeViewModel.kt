package com.fashionism.fashionismuserapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fashionism.fashionismuserapp.data.model.FashionRecommendation
import com.fashionism.fashionismuserapp.data.model.Product
import com.fashionism.fashionismuserapp.data.model.ProfileDetail
import com.fashionism.fashionismuserapp.data.repository.ProductRepository
import com.fashionism.fashionismuserapp.data.repository.UserRepository
import com.fashionism.fashionismuserapp.ui.common.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.File

class HomeViewModel(
    private val productRepository: ProductRepository,
    userRepository: UserRepository,
) : BaseViewModel() {

    private val _profile = MutableLiveData<ProfileDetail>()
    val profile: LiveData<ProfileDetail> = _profile

    private val _mostLiked = MutableLiveData<List<Product>>()
    val mostLiked: LiveData<List<Product>> = _mostLiked

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _isLoadingRecommendation = MutableLiveData(false)
    val isLoadingRecommendation: LiveData<Boolean> = _isLoadingRecommendation

    private val _recommendations = Channel<FashionRecommendation>(Channel.BUFFERED)
    val recommendations = _recommendations.receiveAsFlow()

    private var selectedCategory = -1
    private var productsJob: Job? = null

    init {
        request(call = { userRepository.getProfile() }) { _profile.value = it }
        request(call = { productRepository.getMostLikedProducts() }) { _mostLiked.value = it }
    }

    /** [position] in the category spinner: 0 is "All Products", otherwise it equals the category id. */
    fun selectCategory(position: Int) {
        if (position == selectedCategory) return
        selectedCategory = position
        productsJob?.cancel() // a slow answer for the previous category must not overwrite this one
        productsJob = request(call = { productRepository.getProducts(position.takeIf { it > 0 }) }) {
            _products.value = it
        }
    }

    fun getRecommendation(photo: File) {
        _isLoadingRecommendation.value = true
        request(
            call = { productRepository.getRecommendation(photo) },
            showLoading = false,
            onError = { _isLoadingRecommendation.value = false; emitError(it) },
        ) {
            _isLoadingRecommendation.value = false
            _recommendations.trySend(it)
        }
    }
}
