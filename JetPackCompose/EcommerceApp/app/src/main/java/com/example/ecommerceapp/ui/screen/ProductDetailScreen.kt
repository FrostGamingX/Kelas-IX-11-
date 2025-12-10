package com.example.ecommerceapp.ui.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ecommerceapp.ui.viewmodel.ProductViewModel

@Composable
fun ProductDetailScreen(productId: Int, navController: NavController, viewModel: ProductViewModel = hiltViewModel()) {
    val product = viewModel.productDetail.collectAsState().value

    LaunchedEffect(productId) {
        viewModel.fetchProductDetail(productId)
    }

    product?.let {
        AsyncImage(model = it.image, contentDescription = null)
        Text(it.title)
        Text(it.description)
        Text("$${it.price}")
        Button(onClick = {
            viewModel.addToCart(it)
            navController.navigate("cart")
        }) {
            Text("Add to Cart")
        }
    }
}