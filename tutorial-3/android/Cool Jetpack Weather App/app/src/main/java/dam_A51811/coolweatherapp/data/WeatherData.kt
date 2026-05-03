package dam_A51811.coolweatherapp.data

import kotlinx.serialization.Serializable

/**
 * Representa a resposta JSON devolvida pela API do Open-Meteo.
 *
 * @property latitude Latitude geográfica solicitada.
 * @property longitude Longitude geográfica solicitada.
 * @property timezone Fuso horário da localização (ex.: "auto").
 * @property current_weather Condições meteorológicas atuais.
 * @property hourly Matrizes de dados da previsão a 24 horas.
 * @property daily Matrizes de dados da previsão a 7 dias.
 */
@Serializable
data class WeatherData(
    var latitude: String,
    var longitude: String,
    var timezone: String,
    var current_weather: CurrentWeather,
    var hourly: Hourly,
    var daily: Daily
)

/**
 * Representa o estado atual do tempo fornecido pela API Open-Meteo.
 *
 * @property temperature Temperatura atual em graus Celsius.
 * @property windspeed Velocidade atual do vento em km/h.
 * @property winddirection Direção atual do vento em graus.
 * @property weathercode Código WMO que representa o estado de tempo atual.
 * @property time Timestamp da observação meteorológica atual.
 */
@Serializable
data class CurrentWeather(
    var temperature: Float,
    var windspeed: Float,
    var winddirection: Int,
    var weathercode: Int,
    var time: String
)

// HOURLY FORECAST
/**
 * Previsão meteorológica horária.
 *
 * @property time Matriz de timestamps ISO 8601 para cada hora prevista.
 * @property temperature_2m Matriz de temperaturas em graus Celsius a 2m acima do solo.
 * @property weathercode Matriz de códigos WMO.
 * @property pressure_msl Matriz de valores de pressão média ao nível do mar em hPa.
 * @property precipitation Matriz de quantidades de precipitação em milímetros.
 * @property relativehumidity_2m Matriz de percentagens de humidade relativa a 2m.
 */
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
/**
 * Previsão meteorológica diária (ex.: 7 dias).
 *
 * @property time Matriz de timestamps ISO 8601 para cada dia previsto.
 * @property weathercode Matriz de códigos WMO indicando a condição mais severa para o dia.
 * @property temperature_2m_max Matriz de temperaturas máximas diárias em graus Celsius.
 * @property temperature_2m_min Matriz de temperaturas mínimas diárias em graus Celsius.
 */
@Serializable
data class Daily(
    var time: ArrayList<String>,
    var weathercode: ArrayList<Int>,
    var temperature_2m_max: ArrayList<Double>,
    var temperature_2m_min: ArrayList<Double>
)