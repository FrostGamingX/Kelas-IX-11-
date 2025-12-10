package com.example.ecommerceapp.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.ui.viewmodel.ProductViewModel

@Composable
fun CartScreen(navController: NavController, viewModel: ProductViewModel = hiltViewModel()) {
    val cartItems = viewModel.cartItems.collectAsState(initial = emptyList()).value

    LazyColumn {
        items(cartItems) { item ->
            Text(item.title)
            Text("Quantity: ${item.quantity}")
            Button(onClick = { viewModel.updateQuantity(item, item.quantity + 1) }) { Text("+") }
            Button(onClick = { viewModel.updateQuantity(item, item.quantity - 1) }) { Text("-") }
        }
    }

    val total = cartItems.sumOf { it.price * it.quantity }
    Text("Total: $$total")
    Button(onClick = { navController.navigate("checkout") }) {
        Text("Checkout")
    }
}