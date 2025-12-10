package com.example.ecommerceapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.model.CartItem
import com.example.ecommerceapp.data.model.Product
import com.example.ecommerceapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _productDetail = MutableStateFlow<Product?>(null)
    val productDetail: StateFlow<Product?> = _productDetail

    val cartItems: Flow<List<CartItem>> = repository.getCartItems()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchProductDetail(id: Int) {
        viewModelScope.launch {
            try {
                _productDetail.value = repository.getProductById(id)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val item = CartItem(
                productId = product.id,
                title = product.title,
                price = product.price,
                image = product.image
            )
            repository.addToCart(item)
        }
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity > 0) {
                repository.updateCartItem(item.copy(quantity = newQuantity))
            } else {
                repository.removeFromCart(item.productId)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}