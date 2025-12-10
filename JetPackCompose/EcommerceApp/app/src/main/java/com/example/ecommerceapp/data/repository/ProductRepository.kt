package com.example.ecommerceapp.data.repository

import com.example.ecommerceapp.data.local.CartDao
import com.example.ecommerceapp.data.model.CartItem
import com.example.ecommerceapp.data.remote.ApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ApiService,
    private val cartDao: CartDao
) {
    suspend fun getProducts() = apiService.getProducts()
    suspend fun getProductById(id: Int) = apiService.getProductById(id)

    fun getCartItems() = cartDao.getAllCartItems()
    suspend fun addToCart(item: CartItem) = cartDao.insertCartItem(item)
    suspend fun updateCartItem(item: CartItem) = cartDao.updateCartItem(item)
    suspend fun removeFromCart(id: Int) = cartDao.deleteCartItem(id)
    suspend fun clearCart() = cartDao.clearCart()
}