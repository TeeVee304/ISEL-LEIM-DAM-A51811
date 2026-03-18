package dam_a51811.weatherbuddy.data.repository

import dam_a51811.weatherbuddy.data.remote.OpenWeatherApi
import dam_a51811.weatherbuddy.domain.model.WeatherInfo
import dam_a51811.weatherbuddy.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenWeatherApi,
    private val apiKey: String
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, lon: Double): Result<WeatherInfo> {
        return try {
            val dto = api.getWeatherData(lat = lat, lon = lon, apiKey = apiKey)
            Result.success(
                WeatherInfo(
                    locationName = dto.name,
                    temperatureCelsius = dto.main.temp,
                    weatherCondition = dto.weather.firstOrNull()?.main ?: "Unknown",
                    humidityPercent = dto.main.humidity,
                    windSpeedKmH = dto.wind.speed * 3.6 // m/s to km/h
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
