package com.example.ecommerceapp.ui.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.ui.viewmodel.ProductViewModel

@Composable
fun CheckoutScreen(navController: NavController, viewModel: ProductViewModel = hiltViewModel()) {
    Text("Checkout Page")
    // Tambahkan form alamat, payment, dll. jika perlu
    Button(onClick = {
        viewModel.clearCart()
        navController.navigate("home")
    }) {
        Text("Confirm Purchase")
    }
}