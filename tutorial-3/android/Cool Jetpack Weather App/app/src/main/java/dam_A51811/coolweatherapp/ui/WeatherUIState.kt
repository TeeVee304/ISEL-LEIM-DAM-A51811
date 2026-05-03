package dam_A51811.coolweatherapp.ui

/**
 * Representa os dados da previsão meteorológica para uma única hora na interface.
 *
 * @property time A string de tempo formatada (ex: "14:00").
 * @property temperature A temperatura prevista em graus Celsius.
 * @property weathercode O código meteorológico WMO para esta hora.
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
 * @property time A string de data (ex: "2026-05-02").
 * @property weathercode O código meteorológico WMO mais significativo do dia.
 * @property maxTemp A temperatura máxima prevista em graus Celsius.
 * @property minTemp A temperatura mínima prevista em graus Celsius.
 */
data class DailyForecastItem(
    val time: String,
    val weathercode: Int,
    val maxTemp: Float,
    val minTemp: Float
)

/**
 * Mantém todo o estado observável da interface (UI) para os ecrãs de meteorologia em Jetpack Compose.
 *
 * @property latitude A latitude atual utilizada para os pedidos meteorológicos.
 * @property longitude A longitude atual utilizada para os pedidos meteorológicos.
 * @property temperature A temperatura atual em graus Celsius.
 * @property windspeed A velocidade atual do vento em km/h.
 * @property winddirection A direção atual do vento em graus (0-360).
 * @property weathercode O código meteorológico WMO atual.
 * @property seaLevelPressure A pressão média atual ao nível do mar em hPa.
 * @property humidity A percentagem de humidade relativa atual.
 * @property precipitation A quantidade atual de precipitação em milímetros.
 * @property maxTemp A temperatura máxima prevista para o dia de hoje.
 * @property minTemp A temperatura mínima prevista para o dia de hoje.
 * @property weatherDescription A descrição textual das condições meteorológicas atuais.
 * @property time A marca de tempo (timestamp) dos dados meteorológicos atuais.
 * @property hourlyForecast Uma lista de [HourlyForecastItem] que representa as próximas 24 horas.
 * @property dailyForecast Uma lista de [DailyForecastItem] que representa a previsão a 7 dias.
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
