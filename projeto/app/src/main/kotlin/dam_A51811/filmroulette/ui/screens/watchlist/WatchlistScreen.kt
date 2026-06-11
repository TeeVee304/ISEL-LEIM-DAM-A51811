package dam_A51811.filmroulette.ui.screens.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.ui.theme.SplineSans

/** Placeholder screen for the Watchlist tab. */
@Composable
fun WatchlistScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF131313))
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Outlined.Bookmarks,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.18f),
            modifier = Modifier.size(80.dp),
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.nav_watchlist),
            fontFamily = SplineSans,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White.copy(alpha = 0.35f),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.coming_soon),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.25f),
        )
    }
}
