package dam_A51811.coolweatherapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam_A51811.coolweatherapp.data.WMO_WeatherCode
import dam_A51811.coolweatherapp.data.getWeatherCodeMap
import dam_A51811.coolweatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Locale

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.layout.ContentScale
import dam_A51811.coolweatherapp.R

/**
 * Contentor principal da interface.
 *
 * Observa o [WeatherUIState] fornecido pelo [WeatherViewModel],
 * desencadeia automaticamente o download dos dados aquando do seu carregamento,
 * e resolve as strings localizadas e os ícones para o estado atual.
 *
 * @param weatherViewModel O [WeatherViewModel] que fornece o estado reativo.
 */
@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {
    val weatherUIState by weatherViewModel.uiState.collectAsState() // "Observa" estado da UI ao vivo
    val configuration = LocalConfiguration.current                  // Obtém orientação de tela
    val context = LocalContext.current                              // Obtém contexto da aplicação

    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeather()
    }

    val day = true
    val apiCode = weatherUIState.weathercode

    val codes = context.resources.getIntArray(R.array.weather_codes)
    val images = context.resources.getStringArray(R.array.weather_images)
    val descriptions = context.resources.getStringArray(R.array.weather_descriptions)

    val index = codes.indexOf(apiCode)
    var wImage = if (index != -1) images[index] else "clear_"
    val currentDescription = if (index != -1) descriptions[index] else "Unknown"

    val hourString = weatherUIState.time.substringAfter("T", "12:00").substringBefore(":")
    val hourInt = hourString.toIntOrNull() ?: 12
    val isDay = hourInt in 7..19

    if (wImage.endsWith("_")) {
        wImage += if (isDay) "day" else "night"
    }

    val wIcon = context.resources.getIdentifier(wImage, "drawable", context.packageName)
    val finalIcon = if (wIcon != 0) wIcon else android.R.drawable.ic_menu_help

    val bgRes = if (isDay) R.drawable.sunny_bg else R.drawable.night_bg

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = bgRes), // background dinâmico (!!)
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LandscapeWeatherUI(
                state = weatherUIState.copy(weatherDescription = currentDescription),
                wIcon = finalIcon,
                onLatitudeChange = { newValue ->
                    newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) }
                },
                onLongitudeChange = { newValue ->
                    newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) }
                },
                onUpdateButtonClick = { weatherViewModel.fetchWeather() }
            )
        } else {
            PortraitWeatherUI(
                state = weatherUIState.copy(weatherDescription = currentDescription),
                wIcon = finalIcon,
                onLatitudeChange = { newValue ->
                    newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) }
                },
                onLongitudeChange = { newValue ->
                    newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) }
                },
                onUpdateButtonClick = { weatherViewModel.fetchWeather() }
            )
        }
    }
}

/**
 * Layout Portrait da aplicação.
 *
 * @param state Estado atual da UI.
 * @param wIcon ID do recurso drawable resolvido para o ícone meteorológico atual.
 * @param onLatitudeChange Callback invocado quando o campo da latitude é alterado.
 * @param onLongitudeChange Callback invocado quando o campo da longitude é alterado.
 * @param onUpdateButtonClick Callback invocado quando o botão 'Update' é clicado.
 */
@Composable
fun PortraitWeatherUI(
    state: WeatherUIState,
    wIcon: Int,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            CurrentWeatherHeader(state = state, wIcon = wIcon)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            WeatherInputSection(
                latitude = state.latitude.toString(),
                longitude = state.longitude.toString(),
                onLatitudeChange = onLatitudeChange,
                onLongitudeChange = onLongitudeChange,
                onUpdateButtonClick = onUpdateButtonClick
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text("Hourly Forecast", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            HourlyForecastRow(state.hourlyForecast)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text("7-Day Forecast", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            DailyForecastColumn(state.dailyForecast)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            WeatherDetailsGrid(state = state)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Layout Landscape da aplicação.
 *
 * @param state Estado atual da UI.
 * @param wIcon ID do recurso drawable resolvido para o ícone meteorológico atual.
 * @param onLatitudeChange Callback invocado quando o campo da latitude é alterado.
 * @param onLongitudeChange Callback invocado quando o campo da longitude é alterado.
 * @param onUpdateButtonClick Callback invocado quando o botão 'Update' é clicado.
 */
@Composable
fun LandscapeWeatherUI(
    state: WeatherUIState,
    wIcon: Int,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            item {
                CurrentWeatherHeader(state = state, wIcon = wIcon)
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                WeatherDetailsGrid(state = state)
            }
        }
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                WeatherInputSection(
                    latitude = state.latitude.toString(),
                    longitude = state.longitude.toString(),
                    onLatitudeChange = onLatitudeChange,
                    onLongitudeChange = onLongitudeChange,
                    onUpdateButtonClick = onUpdateButtonClick
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Text("Hourly Forecast", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                HourlyForecastRow(state.hourlyForecast)
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                Text("7-Day Forecast", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                DailyForecastColumn(state.dailyForecast)
            }
        }
    }
}

/**
 * Renderiza a secção de input permitindo ao utilizador modificar a latitude e a longitude.
 *
 * @param latitude Valor atual da latitude.
 * @param longitude Valor atual da longitude.
 * @param onLatitudeChange Callback para as alterações de latitude.
 * @param onLongitudeChange Callback para as alterações de longitude.
 * @param onUpdateButtonClick Callback para a ação de atualizar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherInputSection(
    latitude: String,
    longitude: String,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit
) {
    val colors = TextFieldDefaults.outlinedTextFieldColors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.LightGray,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.LightGray
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = latitude,
            onValueChange = onLatitudeChange,
            label = { Text("Lat") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            colors = colors
        )
        OutlinedTextField(
            value = longitude,
            onValueChange = onLongitudeChange,
            label = { Text("Lon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            colors = colors
        )
        Button(
            onClick = onUpdateButtonClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text("Update", color = Color.White)
        }
    }
}

/**
 * Mostra o cabeçalho principal do tempo atual, incluindo a hora, o ícone, a temperatura e a descrição.
 *
 * @param state Estado atual do tempo.
 * @param wIcon ID do recurso drawable resolvido para o tempo atual.
 */
@Composable
fun CurrentWeatherHeader(state: WeatherUIState, wIcon: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = state.time.substringAfter("T", ""), color = Color.LightGray, fontSize = 16.sp)
        Image(
            painter = painterResource(id = wIcon),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(100.dp)
        )
        Text(text = "${state.temperature}°", color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Thin)
        Text(text = state.weatherDescription, color = Color.White, fontSize = 24.sp)
        Text(text = "H:${state.maxTemp}° L:${state.minTemp}°", color = Color.White, fontSize = 18.sp)
    }
}

/**
 * Linha de scroll horizontal que mostra a previsão para as próximas 24 horas.
 *
 * @param hourlyForecast Lista de objetos [HourlyForecastItem].
 */
@Composable
fun HourlyForecastRow(hourlyForecast: List<HourlyForecastItem>) {
    val context = LocalContext.current
    val codes = context.resources.getIntArray(R.array.weather_codes)
    val images = context.resources.getStringArray(R.array.weather_images)

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(hourlyForecast) { item ->
            val index = codes.indexOf(item.weathercode)
            var wImage = if (index != -1) images[index] else "clear_"
            
            if (wImage.endsWith("_")) {
                wImage += if (item.isDay) "day" else "night"
            }
            val iconRes = context.resources.getIdentifier(wImage, "drawable", context.packageName)
            val finalIcon = if (iconRes != 0) iconRes else android.R.drawable.ic_menu_help

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = item.time, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    painter = painterResource(id = finalIcon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${item.temperature}°", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Coluna vertical que exibe a previsão a 7 dias.
 * Efetua o parsing das strings de datas brutas ISO 8601 para nomes de dias formatados.
 *
 * @param dailyForecast Lista de objetos [DailyForecastItem].
 */
@Composable
fun DailyForecastColumn(dailyForecast: List<DailyForecastItem>) {
    val context = LocalContext.current
    val codes = context.resources.getIntArray(R.array.weather_codes)
    val images = context.resources.getStringArray(R.array.weather_images)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        dailyForecast.forEach { item ->
            val index = codes.indexOf(item.weathercode)
            var wImage = if (index != -1) images[index] else "clear_"
            if (wImage.endsWith("_")) wImage += "day" // defaults to day icon for daily
            
            val iconRes = context.resources.getIdentifier(wImage, "drawable", context.packageName)
            val finalIcon = if (iconRes != 0) iconRes else android.R.drawable.ic_menu_help

            var dayName = item.time
            try {
                val parseFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = parseFormat.parse(item.time)
                val displayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                dayName = displayFormat.format(date!!).replaceFirstChar { it.uppercase() }
            } catch (e: Exception) { }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = dayName, color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = finalIcon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "${item.minTemp}°", color = Color.LightGray, fontSize = 16.sp, modifier = Modifier.padding(end = 16.dp))
                    Text(text = "${item.maxTemp}°", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Grelha 2x2 que contém a Precipitação, o Vento, a Pressão e a Humidade.
 *
 * @param state Estado atual do tempo.
 */
@Composable
fun WeatherDetailsGrid(state: WeatherUIState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WeatherDetailCard(modifier = Modifier.weight(1f), title = "Precipitation", value = "${state.precipitation} mm")
            WeatherDetailCard(modifier = Modifier.weight(1f), title = "Wind", value = "${state.windspeed} km/h", subtitle = "${state.winddirection}°")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WeatherDetailCard(modifier = Modifier.weight(1f), title = "Pressure", value = "${state.seaLevelPressure} hPa")
            WeatherDetailCard(modifier = Modifier.weight(1f), title = "Humidity", value = "${state.humidity}%")
        }
    }
}

/**
 * Componente de cartão reutilizável.
 *
 * @param modifier Modifier a ser aplicado ao cartão.
 * @param title Título da métrica (ex.: "Humidade").
 * @param value Valor primário a apresentar.
 * @param subtitle Valor secundário opcional (ex.: direção do vento).
 */
@Composable
fun WeatherDetailCard(modifier: Modifier = Modifier, title: String, value: String, subtitle: String? = null) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(16.dp)
            .height(100.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title.uppercase(), color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color.White, fontSize = 20.sp)
        if (subtitle != null) {
            Text(text = subtitle, color = Color.White, fontSize = 14.sp)
        }
    }
}
