package com.example.weatherbuddy.data.repository

import com.example.weatherbuddy.data.api.WeatherApiService
import com.example.weatherbuddy.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApiService: WeatherApiService
) {
    // Hardcoded for demo/simplicity - should go in BuildConfig in production.
    private val API_KEY = "YOUR_API_KEY_HERE"

    fun getWeatherForLocation(lat: Double, lon: Double): Flow<Result<WeatherResponse>> = flow {
        try {
            val response = weatherApiService.getWeatherFromCoordinates(lat, lon, API_KEY)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("API Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
