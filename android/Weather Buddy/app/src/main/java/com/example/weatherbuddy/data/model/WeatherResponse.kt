package com.example.weatherbuddy.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("main") val main: MainWeatherData?,
    @SerializedName("weather") val weather: List<WeatherCondition>?,
    @SerializedName("name") val locationName: String?
)

data class MainWeatherData(
    @SerializedName("temp") val temp: Double?,
    @SerializedName("feels_like") val feelsLike: Double?,
    @SerializedName("humidity") val humidity: Int?
)

data class WeatherCondition(
    @SerializedName("id") val id: Int?,
    @SerializedName("main") val mainCondition: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("icon") val icon: String?
)
