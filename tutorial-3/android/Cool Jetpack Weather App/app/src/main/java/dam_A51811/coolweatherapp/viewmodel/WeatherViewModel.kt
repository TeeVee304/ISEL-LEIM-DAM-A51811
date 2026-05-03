package dam_A51811.coolweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_A51811.coolweatherapp.data.WeatherApiClient
import dam_A51811.coolweatherapp.ui.WeatherUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por gerir o estado da interface (UI) dos ecrãs de Meteorologia.
 * 
 * Interage com o [WeatherApiClient] para obter dados meteorológicos de forma assíncrona
 * e expõe um [uiState] reativo através do [StateFlow] do Kotlin, aderindo aos 
 * princípios de fluxo de dados unidirecional.
 */
class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUIState())
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    /**
     * Atualiza a latitude alvo no estado atual da interface.
     *
     * @param latitude O novo valor de latitude a pesquisar.
     */
    fun updateLatitude(latitude: Float) {
        _uiState.update { it.copy(latitude = latitude) }
    }

    /**
     * Atualiza a longitude alvo no estado atual da interface.
     *
     * @param longitude O novo valor de longitude a pesquisar.
     */
    fun updateLongitude(longitude: Float) {
        _uiState.update { it.copy(longitude = longitude) }
    }

    /**
     * Obtém os dados meteorológicos mais recentes da API com base na latitude e
     * longitude atualmente armazenadas no estado da interface.
     * 
     * Esta função é executada de forma assíncrona dentro do `viewModelScope`. Se for bem-sucedida,
     * mapeia as matrizes JSON brutas para listas de [HourlyForecastItem] e 
     * [DailyForecastItem], atualizando o [StateFlow] interno para desencadear uma recomposição da UI.
     */
    fun fetchWeather() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val data = WeatherApiClient.getWeather(currentState.latitude, currentState.longitude)
            
            if (data != null) {
                // Map hourly data
                val hourlyList = mutableListOf<dam_A51811.coolweatherapp.ui.HourlyForecastItem>()
                val maxHourly = minOf(24, data.hourly.time.size)
                for (i in 0 until maxHourly) {
                    val fullTime = data.hourly.time[i]
                    val hourString = fullTime.substring(11, 13)
                    val hourInt = hourString.toIntOrNull() ?: 12
                    val isDay = hourInt in 7..19
                    hourlyList.add(
                        dam_A51811.coolweatherapp.ui.HourlyForecastItem(
                            time = fullTime.substring(11, 16),
                            temperature = data.hourly.temperature_2m[i].toFloat(),
                            weathercode = data.hourly.weathercode[i],
                            isDay = isDay
                        )
                    )
                }

                // Map daily data
                val dailyList = mutableListOf<dam_A51811.coolweatherapp.ui.DailyForecastItem>()
                val maxDaily = minOf(7, data.daily.time.size)
                for (i in 0 until maxDaily) {
                    dailyList.add(
                        dam_A51811.coolweatherapp.ui.DailyForecastItem(
                            time = data.daily.time[i],
                            weathercode = data.daily.weathercode[i],
                            maxTemp = data.daily.temperature_2m_max[i].toFloat(),
                            minTemp = data.daily.temperature_2m_min[i].toFloat()
                        )
                    )
                }

                val currentCode = data.current_weather.weathercode
                val weatherMap = dam_A51811.coolweatherapp.data.getWeatherCodeMap()
                val description = weatherMap[currentCode]?.description ?: "Unknown"

                _uiState.update { state ->
                    state.copy(
                        temperature = data.current_weather.temperature,
                        windspeed = data.current_weather.windspeed,
                        winddirection = data.current_weather.winddirection,
                        weathercode = currentCode,
                        seaLevelPressure = data.hourly.pressure_msl.firstOrNull()?.toFloat() ?: 0f,
                        humidity = data.hourly.relativehumidity_2m.firstOrNull() ?: 0,
                        precipitation = data.hourly.precipitation.firstOrNull()?.toFloat() ?: 0f,
                        maxTemp = data.daily.temperature_2m_max.firstOrNull()?.toFloat() ?: 0f,
                        minTemp = data.daily.temperature_2m_min.firstOrNull()?.toFloat() ?: 0f,
                        weatherDescription = description,
                        time = data.current_weather.time,
                        hourlyForecast = hourlyList,
                        dailyForecast = dailyList
                    )
                }
            }
        }
    }
}
