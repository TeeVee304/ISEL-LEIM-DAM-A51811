package dam_A51811.coolweatherapp.ui

/**
 * Representa os dados da previsão meteorológica para uma única hora na interface.
 *
 * @property time String de tempo formatada (ex.: "14:00").
 * @property temperature Temperatura prevista em graus Celsius.
 * @property weathercode Código meteorológico WMO para esta hora.
 * @property isDay Verdadeiro se a hora ocorre durante o dia (07:00-19:00), falso caso contrário.
 */
data class HourlyForecastItem(
    val time: String,
    val temperature: Float,
    val weathercode: Int,
    val isDay: Boolean
)

/**
 * Representa os dados da previsão meteorológica para um único dia na interface.
 *
 * @property time String de data formatada (ex.: "2026-05-02").
 * @property weathercode Código meteorológico WMO mais significativo do dia.
 * @property maxTemp Temperatura máxima prevista em graus Celsius.
 * @property minTemp Temperatura mínima prevista em graus Celsius.
 */
data class DailyForecastItem(
    val time: String,
    val weathercode: Int,
    val maxTemp: Float,
    val minTemp: Float
)

/**
 * Mantém o estado da interface (UI) em Jetpack Compose.
 *
 * @property latitude Latitude atual.
 * @property longitude Longitude atual.
 * @property temperature Temperatura atual em graus Celsius.
 * @property windspeed Velocidade atual do vento em km/h.
 * @property winddirection Direção atual do vento em graus.
 * @property weathercode Código meteorológico WMO atual.
 * @property seaLevelPressure Pressão média atual ao nível do mar em hPa.
 * @property humidity Percentagem de humidade relativa atual.
 * @property precipitation Quantidade atual de precipitação em milímetros.
 * @property maxTemp Temperatura máxima prevista para o dia de hoje.
 * @property minTemp Temperatura mínima prevista para o dia de hoje.
 * @property weatherDescription Descrição textual das condições meteorológicas atuais.
 * @property time Timestamp dos dados meteorológicos atuais.
 * @property hourlyForecast Lista de [HourlyForecastItem] que representa as próximas 24 horas.
 * @property dailyForecast Lista de [DailyForecastItem] que representa a previsão a 7 dias.
 */
data class WeatherUIState(
    val latitude: Float = 38.72f,
    val longitude: Float = -9.14f,
    val temperature: Float = 0f,
    val windspeed: Float = 0f,
    val winddirection: Int = 0,
    val weathercode: Int = 0,
    val seaLevelPressure: Float = 0f,
    val humidity: Int = 0,
    val precipitation: Float = 0f,
    val maxTemp: Float = 0f,
    val minTemp: Float = 0f,
    val weatherDescription: String = "",
    val time: String = "",
    val hourlyForecast: List<HourlyForecastItem> = emptyList(),
    val dailyForecast: List<DailyForecastItem> = emptyList()
)
