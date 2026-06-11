package dam_A51811.filmroulette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dam_A51811.filmroulette.ui.navigation.FilmRouletteApp
import dam_A51811.filmroulette.ui.theme.FilmRouletteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmRouletteTheme {
                FilmRouletteApp()
            }
        }
    }
}