package dam_a51811.weatherbuddy.domain.model

data class WeatherInfo(
    val locationName: String,       // ex.: Lumiar
    val temperatureCelsius: Double, // ex.: 14.8ºC
    val weatherCondition: String,   // ex.: Clouds
    val humidityPercent: Int,       // ex.: 85%
    val windSpeedKmH: Double        // ex.: 27.4 km/h
)
