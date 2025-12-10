// data/remote/WeatherApi.kt
package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "id" // bahasa Indonesia
    ): WeatherResponse

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}