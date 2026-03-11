package com.example.weatherbuddy.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherDetailsFragment : Fragment() {

    private val viewModel: WeatherDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.weatherbuddy.R.layout.fragment_weather_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lat = arguments?.getFloat("lat")?.toDouble() ?: 0.0
        val lon = arguments?.getFloat("lon")?.toDouble() ?: 0.0

        if (savedInstanceState == null) {
            viewModel.fetchWeather(lat, lon)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is WeatherUiState.Loading -> {
                            // Show loading UI
                        }
                        is WeatherUiState.Success -> {
                            val temp = state.weather.main?.temp
                            val desc = state.weather.weather?.firstOrNull()?.description
                            Toast.makeText(context, "Temp: $temp C, $desc", Toast.LENGTH_LONG).show()
                        }
                        is WeatherUiState.Error -> {
                            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
