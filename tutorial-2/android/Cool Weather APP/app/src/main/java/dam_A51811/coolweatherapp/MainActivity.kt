package dam_A51811.coolweatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Calendar

/**
 * Thread principal da aplicação.
 * Responsável por gerir a interface do utilizador, solicitar permissões de localização
 * e observar os dados do [ViewModel]. */
class MainActivity : AppCompatActivity() {
    private val LATITUDE = 38.72
    private val LONGITUDE = -9.14

    // NOTA: Utilizamos o 'lateinit' para garantir que as variáveis nunca são nulas
    private lateinit var viewModel: ViewModel
    /* Obtém a localização GPS do utilizador.
       NOTA: FusedLocationProviderClient é a API recomendada pela Google porque combina
       os vários sinais do telemóvel (GPS, Wi-Fi, Bluetooth e redes móveis). */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Invocado quando a thread é criada.
     * Inicializa os componentes da interface, o ViewModel e a gestão da localização.
     * @param savedInstanceState (default)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupObservers()
        requestLocation()
    }

    /**
     * Configura os Observers para as variáveis LiveData presentes no ViewModel.
     * Atualiza os elementos da interface automaticamente sempre que novos dados
     * são recebidos da API.
     */
    private fun setupObservers() {
        val temperatureTextView = findViewById<TextView>(R.id.temperature)
        val windTextView = findViewById<TextView>(R.id.wind_value)
        val windDirectionTextView = findViewById<TextView>(R.id.wind_direction)
        val pressureTextView = findViewById<TextView>(R.id.pressure_value)
        val weatherDescTextView = findViewById<TextView>(R.id.weather_description)
        val maxMinTextView = findViewById<TextView>(R.id.max_min_temp)
        val precipitationTextView = findViewById<TextView>(R.id.precipitation_value)
        val humidityTextView = findViewById<TextView>(R.id.humidity_value)

        val hourlyRecycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.hourly_forecast_recyclerview)
        val dailyRecycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.weekly_forecast_recyclerview)

        viewModel.weatherData.observe(this) { data ->
            temperatureTextView.text = "${data.current_weather.temperature} °C"
            windTextView.text = "${data.current_weather.windspeed} km/h"
            windDirectionTextView.text = "${getString(R.string.wind_direction)} ${data.current_weather.winddirection}°"
            pressureTextView.text = "${data.hourly.pressure_msl[0]} hPa"
            precipitationTextView.text = "${data.hourly.precipitation[0]} mm"
            humidityTextView.text = "${data.hourly.relativehumidity_2m[0]} %"
            maxMinTextView.text = "Max: ${data.daily.temperature_2m_max[0]}° Min: ${data.daily.temperature_2m_min[0]}°"

            hourlyRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
            dailyRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
            hourlyRecycler.adapter = HourlyAdapter(data.hourly)
            dailyRecycler.adapter = DailyAdapter(data.daily)
        }
        // Descrição do tempo (ex.: "Céu Limpo")
        viewModel.weatherDescription.observe(this) { description ->
            weatherDescTextView.text = description
        }
    }

    /** Verifica se a aplicação tem as permissões necessárias para aceder à localização do dispositivo. */
    private fun requestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocationAndWeather()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                100 // Valor arbitrário. Utilizou-se '100' para identificar este tipo de pedido.
            )
        }
    }

    /**
     * Função chamada automaticamente após o utilizador interagir com o pop-up de
     * permissão de acesso à localização.
     * @param requestCode O código utilizado no requestPermissions ('100').
     * @param permissions Lista de permissões solicitadas.
     * @param grantResults Lista de resultados correspondentes às permissões solicitadas
     * (PERMISSION_GRANTED ou PERMISSION_DENIED).
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            // Se a permissão foi concedida
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndWeather()
            } else {
                // Se a permissão foi recusada, avisa o utilizador e usa Lisboa por default
                Toast.makeText(this, "Permissão de GPS recusada. A utilizar Lisboa.", Toast.LENGTH_LONG).show()
                val locationTextView = findViewById<TextView>(R.id.location)
                locationTextView.text = "Lisbon (Default)"
                fetchWeatherForLocation(LATITUDE, LONGITUDE)
            }
        }
    }

    /** Obtém as coordenadas GPS exatas do telemóvel e pede os dados da meteorologia. */
    @SuppressLint("MissingPermission") // de outra forma não funcionaria (??)
    private fun getLocationAndWeather() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val locationTextView = findViewById<TextView>(R.id.location)

            if (location != null) {
                locationTextView.text = "Lat: ${String.format("%.2f", location.latitude)}, Lon: ${String.format("%.2f", location.longitude)}"
                fetchWeatherForLocation(location.latitude, location.longitude)
            } else {
                locationTextView.text = "Lat: ${LATITUDE}, Lon: ${LONGITUDE}"
                fetchWeatherForLocation(LATITUDE, LONGITUDE)
            }
        }
    }

    /**
     * Prepara as variáveis de interface com base na hora atual e solicita
     * ao [ViewModel] que efetue a transferência dos dados meteorológicos.
     * @param lat Latitude.
     * @param long Longitude.
     */
    private fun fetchWeatherForLocation(lat: Double, long: Double) {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val isDay = hour in 7..19 // Considera-se dia entre as 7h e as 19h

        // Alterar fundo consoante o ciclo de dia/noite
        val backgroundLayout = findViewById<androidx.coordinatorlayout.widget.CoordinatorLayout>(R.id.background)
        if (isDay) {
            backgroundLayout.setBackgroundResource(R.drawable.sunny_bg)
        } else {
            backgroundLayout.setBackgroundResource(R.drawable.night_bg)
        }

        viewModel.fetchWeatherData(lat, long, isDay)
    }
}