package dam_a51811.weatherbuddy.presentation.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import dam_a51811.weatherbuddy.R
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val latitude = arguments?.getFloat("latitude")?.toDouble() ?: 0.0
        val longitude = arguments?.getFloat("longitude")?.toDouble() ?: 0.0
        
        val toolbar: MaterialToolbar = view.findViewById(R.id.weather_toolbar)
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.fetchWeather(latitude, longitude)

        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        val contentLayout: LinearLayout = view.findViewById(R.id.content_layout)
        val errorText: TextView = view.findViewById(R.id.tv_error)
        
        val tvLocation: TextView = view.findViewById(R.id.tv_location_name)
        val tvTemp: TextView = view.findViewById(R.id.tv_temperature)
        val tvCondition: TextView = view.findViewById(R.id.tv_condition)
        val tvHumidity: TextView = view.findViewById(R.id.tv_humidity)
        val tvWind: TextView = view.findViewById(R.id.tv_wind)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is WeatherUiState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        contentLayout.visibility = View.GONE
                        errorText.visibility = View.GONE
                    }
                    is WeatherUiState.Success -> {
                        progressBar.visibility = View.GONE
                        contentLayout.visibility = View.VISIBLE
                        errorText.visibility = View.GONE
                        
                        val data = state.weather
                        tvLocation.text = data.locationName
                        tvTemp.text = "%.1f°C".format(data.temperatureCelsius)
                        tvCondition.text = data.weatherCondition
                        tvHumidity.text = "Humidity: ${data.humidityPercent}%"
                        tvWind.text = "Wind: %.1f km/h".format(data.windSpeedKmH)
                    }
                    is WeatherUiState.Error -> {
                        progressBar.visibility = View.GONE
                        contentLayout.visibility = View.GONE
                        errorText.visibility = View.VISIBLE
                        errorText.text = state.message
                    }
                }
            }
        }
    }
}
