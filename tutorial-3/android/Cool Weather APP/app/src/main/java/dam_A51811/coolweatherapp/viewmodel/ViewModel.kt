package dam_A51811.coolweatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import dam_A51811.coolweatherapp.R
import dam_A51811.coolweatherapp.data.WeatherData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import kotlin.concurrent.thread

/**
 * Estende [AndroidViewModel] (em vez de [ViewModel]) para permitir o acesso ao contexto
 * da aplicação, que será necessário para ler os recursos XML.
 *
 * @param application A instância da aplicação utilizada para aceder aos recursos.
 */
class ViewModel(application: Application) : AndroidViewModel(application) {
    /* Armazena os dados meteorológicos em bruto recebidos da API. */
    private val _weatherData = MutableLiveData<WeatherData>()
    /* Snapshot de _weatherData que será lida pela Activity. */
    val weatherData: LiveData<WeatherData> = _weatherData
    /* Armazena a descrição textual do estado do tempo. */
    private val _weatherDescription = MutableLiveData<String>()
    /* Snapshot de _weatherDescription que será lida pela Activity. */
    val weatherDescription: LiveData<String> = _weatherDescription
    /* Armazena o nome do recurso de imagem correspondente ao estado do tempo. */
    private val _weatherImageName = MutableLiveData<String>()
    /* Armazena o nome do ficheiro (drawable) que deve ser apresentado no ecrã. */
    val weatherImageName: LiveData<String> = _weatherImageName

    /**
     * Efetua o pedido de rede para obter os dados meteorológicos da API Open-Meteo.
     * NOTA: É executada numa thread secundária para evitar o bloqueio da Main Thread (UI).
     *
     * @param lat Latitude.
     * @param long Longitude.
     * @param isDay Indica se é dia (true) ou noite (false) na localização.
     */
    fun fetchWeatherData(lat: Double, long: Double, isDay: Boolean) {
        thread {
            val request = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$long&current_weather=true&hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m,winddirection_10m,precipitation,relativehumidity_2m&daily=weathercode,temperature_2m_max,temperature_2m_min&timezone=auto"
            val url = URL(request)

            url.openStream().use { stream ->
                val data = Gson().fromJson(InputStreamReader(stream, "UTF-8"), WeatherData::class.java)

                _weatherData.postValue(data) // postValue() envia os dados processados para a Main Thread

                processWeatherCode(data.current_weather.weathercode, isDay)
            }
        }
    }

    /**
     * Mapeia o código WMO fornecido pela API para as respetivas descrições e imagens (arrays.xml).
     *
     * @param apiCode Código WMO retornado pela API.
     * @param isDay Flag para definir qual das versões do ícone será utilizada - dia (true) ou noite(false).
     */
    private fun processWeatherCode(apiCode: Int, isDay: Boolean) {
        val context = getApplication<Application>().applicationContext

        val codes = context.resources.getIntArray(R.array.weather_codes)
        val descriptions = context.resources.getStringArray(R.array.weather_descriptions)
        val images = context.resources.getStringArray(R.array.weather_images)

        val index = codes.indexOf(apiCode)

        // Atualiza a descrição
        _weatherDescription.postValue(descriptions[index])

        var imgName = images[index]
        if (imgName.endsWith("_")) {
            imgName += if (isDay) "day" else "night"
        }
        _weatherImageName.postValue(imgName)
    }
}