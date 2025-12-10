// ui/screen/WeatherScreen.kt
package com.example.weatherapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.viewmodel.WeatherState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val state = viewModel.state
    val cityInput = viewModel.cityInput

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E88E5), Color(0xFF1565C0))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Weather App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input kota
        OutlinedTextField(
            value = cityInput,
            onValueChange = { viewModel.updateCity(it) },
            label = { Text("Masukkan nama kota") },
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { viewModel.getWeather() }),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.getWeather() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
        ) {
            Text("Cari Cuaca", color = Color.Black, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        when (state) {
            is WeatherState.Loading -> {
                CircularProgressIndicator(color = Color.White)
            }
            is WeatherState.Success -> {
                val data = state.data
                val iconUrl = "https://openweathermap.org/img/wn/${data.weather[0].icon}@2x.png"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("${data.name}, ${data.sys.country}", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(data.weather[0].description.replaceFirstChar { it.uppercase() }, fontSize = 20.sp)

                        Image(
                            painter = rememberAsyncImagePainter(iconUrl),
                            contentDescription = "Weather icon",
                            modifier = Modifier.size(120.dp)
                        )

                        Text("${data.main.temp}°C", fontSize = 64.sp, fontWeight = FontWeight.Bold)
                        Text("Terasa ${data.main.feels_like}°C", fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            WeatherDetail(label = "Kelembapan", value = "${data.main.humidity}%")
                            WeatherDetail(label = "Tekanan", value = "${data.main.pressure} hPa")
                        }
                    }
                }
            }
            is WeatherState.Error -> {
                Text(state.message, color = Color.Red, fontSize = 18.sp, textAlign = TextAlign.Center)
            }
            else -> {}
        }
    }
}

@Composable
fun WeatherDetail(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 16.sp, color = Color.Gray)
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
    }
}