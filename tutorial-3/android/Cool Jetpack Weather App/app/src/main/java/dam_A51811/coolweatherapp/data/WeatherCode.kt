package dam_A51811.coolweatherapp.data

/**
 * Enum que representa os códigos meteorológicos padrão da WMO (World Meteorological Organization).
 *
 * Cada código mapeia para uma descrição textual padronizada e o nome base associado para a imagem
 * (Nota: Os nomes das imagens neste enum poderão ser legados. Atualmente, a aplicação privilegia
 * a leitura das descrições localizadas e imagens diretamente do ficheiro arrays.xml para a UI em Compose).
 *
 * @property code O código numérico meteorológico da WMO.
 * @property description A descrição textual da condição meteorológica.
 * @property image O nome base em texto do recurso (drawable) que representa a condição meteorológica.
 */
enum class WMO_WeatherCode(val code: Int, val description: String, val image: String) {
    CLEAR_SKY(0, "Clear sky", "sunny_"),
    MAINLY_CLEAR(1, "Mainly clear", "partly_cloudy_"),
    PARTLY_CLOUDY(2, "Partly cloudy", "partly_cloudy_"),
    OVERCAST(3, "Overcast", "cloudy"),
    FOG(45, "Fog", "fog"),
    DEPOSITING_RIME_FOG(48, "Depositing rime fog", "fog"),
    DRIZZLE_LIGHT(51, "Drizzle: Light", "rainy"),
    DRIZZLE_MODERATE(53, "Drizzle: Moderate", "rainy"),
    DRIZZLE_DENSE(55, "Drizzle: Dense intensity", "rainy"),
    FREEZING_DRIZZLE_LIGHT(56, "Freezing Drizzle: Light", "rainy"),
    FREEZING_DRIZZLE_DENSE(57, "Freezing Drizzle: Dense intensity", "rainy"),
    RAIN_SLIGHT(61, "Rain: Slight", "rainy"),
    RAIN_MODERATE(63, "Rain: Moderate", "rainy"),
    RAIN_HEAVY(65, "Rain: Heavy intensity", "rainy"),
    FREEZING_RAIN_LIGHT(66, "Freezing Rain: Light", "rainy"),
    FREEZING_RAIN_HEAVY(67, "Freezing Rain: Heavy intensity", "rainy"),
    SNOW_FALL_SLIGHT(71, "Snow fall: Slight", "snowy"),
    SNOW_FALL_MODERATE(73, "Snow fall: Moderate", "snowy"),
    SNOW_FALL_HEAVY(75, "Snow fall: Heavy intensity", "snowy"),
    SNOW_GRAINS(77, "Snow grains", "snowy"),
    RAIN_SHOWERS_SLIGHT(80, "Rain showers: Slight", "rainy"),
    RAIN_SHOWERS_MODERATE(81, "Rain showers: Moderate", "rainy"),
    RAIN_SHOWERS_VIOLENT(82, "Rain showers: Violent", "rainy"),
    SNOW_SHOWERS_SLIGHT(85, "Snow showers slight", "snowy"),
    SNOW_SHOWERS_HEAVY(86, "Snow showers heavy", "snowy"),
    THUNDERSTORM(95, "Thunderstorm: Slight or moderate", "thunderstorm"),
    THUNDERSTORM_SLIGHT_HAIL(96, "Thunderstorm with slight hail", "thunderstorm"),
    THUNDERSTORM_HEAVY_HAIL(99, "Thunderstorm with heavy hail", "thunderstorm")
}

/**
 * Gera um mapa dos códigos meteorológicos da WMO para as suas respetivas instâncias no enum [WMO_WeatherCode].
 *
 * @return Um [Map] onde a chave é o código inteiro da WMO e o valor é a entrada correspondente do enum.
 */
fun getWeatherCodeMap(): Map<Int, WMO_WeatherCode> {
    return WMO_WeatherCode.values().associateBy { it.code }
}
