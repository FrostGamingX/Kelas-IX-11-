package com.example.ecommerceapp.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ecommerceapp.ui.viewmodel.ProductViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: ProductViewModel = hiltViewModel()) {
    val products = viewModel.products.collectAsState().value

    LazyColumn {
        items(products) { product ->
            Card(onClick = { navController.navigate("detail/${product.id}") }) {
                AsyncImage(model = product.image, contentDescription = null)
                Text(product.title)
                Text("$${product.price}")
            }
        }
    }
}