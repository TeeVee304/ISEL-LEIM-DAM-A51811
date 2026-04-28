package dam_A51811.coolweatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    var latitude: String,
    var longitude: String,
    var timezone: String,
    var current_weather: CurrentWeather,
    var hourly: Hourly,
    var daily: Daily
)

@Serializable
data class CurrentWeather(
    var temperature: Float,
    var windspeed: Float,
    var winddirection: Int,
    var weathercode: Int,
    var time: String
)

// HOURLY FORECAST
@Serializable
data class Hourly(
    var time: ArrayList<String>,
    var temperature_2m: ArrayList<Double>, // novo tipo <Double>
    var weathercode: ArrayList<Int>,      // novo tipo <Int>
    var pressure_msl: ArrayList<Double>,
    var precipitation: ArrayList<Double>,
    var relativehumidity_2m: ArrayList<Int>
)

// 10-DAY FORECAST
@Serializable
data class Daily(
    var time: ArrayList<String>,
    var weathercode: ArrayList<Int>,
    var temperature_2m_max: ArrayList<Double>,
    var temperature_2m_min: ArrayList<Double>
)