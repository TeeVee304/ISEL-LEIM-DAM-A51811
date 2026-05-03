package dam_A51811.coolweatherapp.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Singleton responsável pela gestão de pedidos HTTP para a API Open-Meteo.
 */
object WeatherApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true       // Formato JSON
                isLenient = true         // Aceitar JSON desformatado
                ignoreUnknownKeys = true // Ignorar propriedades desconhecidas
            })
        }
    }

    /**
     * Obtém os dados meteorológicos da API Open-Meteo para uma dada localização.
     *
     * @param lat Latitude da localização pretendida.
     * @param lon Longitude da localização pretendida.
     * @return Objeto [WeatherData] que contém a previsão caso o pedido seja bem-sucedido,
     *         ou `null` se o pedido de rede ou o parsing do JSON falharem.
     */
    suspend fun getWeather(lat: Float, lon: Float): WeatherData? {
        val reqString = buildString {
            append("https://api.open-meteo.com/v1/forecast?")
            append("latitude=${lat}&longitude=${lon}&")
            append("current_weather=true&")
            append("hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m,precipitation,relativehumidity_2m&")
            append("daily=weathercode,temperature_2m_max,temperature_2m_min&")
            append("timezone=auto")
        }
        System.out.println("Getting URL: $reqString")
        return try {
            client.get(reqString).body() // Parses JSON into WeatherData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
