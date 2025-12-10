// ui/viewmodel/WeatherViewModel.kt
package com.example.weatherapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch

sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()

    var state by mutableStateOf<WeatherState>(WeatherState.Idle)
        private set

    var cityInput by mutableStateOf("Surabaya")
        private set

    fun updateCity(input: String) {
        cityInput = input
    }

    fun getWeather() {
        viewModelScope.launch {
            state = WeatherState.Loading
            try {
                val response = repository.getWeather(cityInput)
                state = WeatherState.Success(response)
            } catch (e: Exception) {
                state = WeatherState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}