package dam_a51811.weatherbuddy.domain.repository

import dam_a51811.weatherbuddy.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, lon: Double): Result<WeatherInfo>
}
