package dam_A51811.coolweatherapp

data class WeatherData(
    var latitude: String,
    var longitude: String,
    var timezone: String,
    var current_weather: CurrentWeather,
    var hourly: Hourly,
    var daily: Daily
)

data class CurrentWeather(
    var temperature: Float,
    var windspeed: Float,
    var winddirection: Int,
    var weathercode: Int,
    var time: String
)

// HOURLY FORECAST
data class Hourly(
    var time: ArrayList<String>,
    var temperature_h: ArrayList<Double>, // novo tipo <Double>
    var weathercode: ArrayList<Int>,       // novo tipo <Int>
    var pressure: ArrayList<Double>,
    var precipitation: ArrayList<Double>,
    var humidity: ArrayList<Int>
)

// 10-DAY FORECAST
data class Daily(
    var time: ArrayList<String>,
    var weathercode: ArrayList<Int>,
    var temperature_max: ArrayList<Double>,
    var temperature_min: ArrayList<Double>
)