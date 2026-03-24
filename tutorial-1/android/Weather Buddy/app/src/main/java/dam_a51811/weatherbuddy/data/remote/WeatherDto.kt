package dam_a51811.weatherbuddy.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("weather") val weather: List<WeatherDescriptionDto>,
    @SerializedName("main") val main: MainConditionsDto,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("name") val name: String
)

data class WeatherDescriptionDto(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class MainConditionsDto(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WindDto(
    @SerializedName("speed") val speed: Double
)
