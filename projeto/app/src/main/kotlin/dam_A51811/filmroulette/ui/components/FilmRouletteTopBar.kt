package dam_A51811.filmroulette.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans

/**
 * The app-wide top bar rendered on every screen.
 *
 * Styled to match the HTML mockup: dark translucent background, bold italic red logo,
 * hamburger icon on the left, and a circular user avatar on the right.
 *
 * @param avatarUrl URL of the current user's avatar image (optional).
 * @param onMenuClick Callback for the hamburger icon.
 */
@Composable
fun FilmRouletteTopBar(
    avatarUrl: String? = null,
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(elevation = 8.dp, ambientColor = Color.Black, spotColor = Color.Black)
            .background(Color.Black.copy(alpha = 0.70f))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // ── Left: hamburger ───────────────────────────────────────────────
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(id = R.string.desc_menu),
                tint = Color.White.copy(alpha = 0.70f),
            )
        }

        // ── Centre: logo ──────────────────────────────────────────────────
        Text(
            text = stringResource(id = R.string.app_logo),
            fontFamily = SplineSans,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 22.sp,
            letterSpacing = 3.sp,
            color = NeonRed,
        )

        // ── Right: avatar ─────────────────────────────────────────────────
        Spacer(modifier = Modifier.width(8.dp))
        if (avatarUrl != null) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = stringResource(id = R.string.desc_profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.10f)),
            )
        } else {
            // Placeholder circle when no avatar is available
            Spacer(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.10f)),
            )
        }
    }
}
