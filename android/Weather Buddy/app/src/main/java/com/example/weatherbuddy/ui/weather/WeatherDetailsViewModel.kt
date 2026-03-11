package com.example.weatherbuddy.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherbuddy.data.model.WeatherResponse
import com.example.weatherbuddy.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun fetchWeather(lat: Double, lon: Double) {
        _uiState.value = WeatherUiState.Loading
        viewModelScope.launch {
            repository.getWeatherForLocation(lat, lon).collect { result ->
                result.onSuccess { weatherResponse ->
                    _uiState.value = WeatherUiState.Success(weatherResponse)
                }.onFailure { exception ->
                    _uiState.value = WeatherUiState.Error(exception.message ?: "Unknown Error")
                }
            }
        }
    }
}
