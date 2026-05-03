package dam_A51811.coolweatherapp.data

import kotlinx.serialization.Serializable

/**
 * Representa a resposta JSON principal devolvida pela API do Open-Meteo.
 *
 * @property latitude A latitude geográfica solicitada.
 * @property longitude A longitude geográfica solicitada.
 * @property timezone O fuso horário da localização (ex: "auto").
 * @property current_weather As condições meteorológicas atuais.
 * @property hourly As matrizes de dados da previsão a 24 horas.
 * @property daily As matrizes de dados da previsão a 7 dias.
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
 * @property temperature A temperatura atual em graus Celsius.
 * @property windspeed A velocidade atual do vento em km/h.
 * @property winddirection A direção atual do vento em graus (0-360).
 * @property weathercode O código meteorológico WMO que representa o estado de tempo atual.
 * @property time A marca de tempo (timestamp) da observação meteorológica atual.
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
 * Representa a previsão meteorológica horária (matrizes de dados para cada hora).
 *
 * @property time Uma matriz de marcas de tempo ISO 8601 para cada hora prevista.
 * @property temperature_2m Uma matriz de temperaturas em graus Celsius a 2m acima do solo.
 * @property weathercode Uma matriz de códigos meteorológicos WMO.
 * @property pressure_msl Uma matriz de valores de pressão média ao nível do mar em hPa.
 * @property precipitation Uma matriz de quantidades de precipitação em milímetros.
 * @property relativehumidity_2m Uma matriz de percentagens de humidade relativa a 2m.
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
 * Representa a previsão meteorológica diária (ex: 7 dias) (matrizes de dados para cada dia).
 *
 * @property time Uma matriz de marcas de tempo ISO 8601 para cada dia previsto.
 * @property weathercode Uma matriz de códigos meteorológicos WMO indicando a condição mais severa para o dia.
 * @property temperature_2m_max Uma matriz de temperaturas máximas diárias em graus Celsius.
 * @property temperature_2m_min Uma matriz de temperaturas mínimas diárias em graus Celsius.
 */
@Serializable
data class Daily(
    var time: ArrayList<String>,
    var weathercode: ArrayList<Int>,
    var temperature_2m_max: ArrayList<Double>,
    var temperature_2m_min: ArrayList<Double>
)