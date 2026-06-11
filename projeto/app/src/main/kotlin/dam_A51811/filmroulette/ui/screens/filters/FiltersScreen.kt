package dam_A51811.filmroulette.ui.screens.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.data.model.Genre
import dam_A51811.filmroulette.ui.components.GlassCard
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans

// ── Preview data ───────────────────────────────────────────────────────────
private val allGenres = listOf(
    R.string.genre_scifi, R.string.genre_neonoir, R.string.genre_cyberpunk, R.string.genre_psychological_thriller,
    R.string.genre_documentary, R.string.genre_horror, R.string.genre_independent_cinema, R.string.genre_animation,
)

private data class PlatformItem(val nameResId: Int, val logoUrl: String)

private val platforms = listOf(
    PlatformItem(R.string.platform_netflix,     "https://upload.wikimedia.org/wikipedia/commons/0/08/Netflix_2015_logo.svg"),
    PlatformItem(R.string.platform_disneyplus,   "https://upload.wikimedia.org/wikipedia/commons/3/3e/Disney%2B_logo.svg"),
    PlatformItem(R.string.platform_max,         "https://upload.wikimedia.org/wikipedia/commons/1/17/HBO_Max_Logo.svg"),
    PlatformItem(R.string.platform_primevideo,  "https://upload.wikimedia.org/wikipedia/commons/1/11/Amazon_Prime_Video_logo.svg"),
)

private val quickPickMinutes = listOf(30, 90, 120, 150)

/**
 * Filters / Onboarding screen.
 *
 * Lets the user configure available time, favourite genres, and active
 * streaming platforms before spinning the roulette.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FiltersScreen(
    onStartRoulette: (Int, List<Genre>) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    var sliderMinutes by remember { mutableIntStateOf(115) }
    val selectedGenres = remember { mutableStateOf(setOf(R.string.genre_scifi, R.string.genre_cyberpunk, R.string.genre_horror)) }
    val selectedPlatforms = remember { mutableStateOf(setOf(R.string.platform_netflix, R.string.platform_disneyplus)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF131313))
            .verticalScroll(rememberScrollState())
            .padding(top = 80.dp, bottom = 120.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        // ── Hero banner ────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(24.dp)),
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1200",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1C1B1B)),
            )
            // Scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color.Transparent,
                                1f to Color.Black.copy(alpha = 0.95f),
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.banner_title),
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    lineHeight = 36.sp,
                )
                Text(
                    text = stringResource(id = R.string.banner_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.70f),
                )
            }
        }

        // ── Available Time ─────────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Default.Schedule, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = stringResource(id = R.string.time_section_title),
            trailing = {
                Text(
                    text = stringResource(id = R.string.time_minutes_format, sliderMinutes),
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primaryContainer,
                )
            },
        )

        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Slider(
                    value = sliderMinutes.toFloat(),
                    onValueChange = { sliderMinutes = it.toInt() },
                    valueRange = 30f..240f,
                    steps = 0,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor          = MaterialTheme.colorScheme.primary,
                        activeTrackColor    = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor  = Color.White.copy(alpha = 0.15f),
                    ),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    quickPickMinutes.forEach { minutes ->
                        val isSelected = sliderMinutes == minutes
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonRed.copy(alpha = 0.20f) else Color.White.copy(alpha = 0.05f))
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.60f) else Color.White.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                .clickable { sliderMinutes = minutes }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = if (minutes == 150) stringResource(id = R.string.time_quick_pick_plus) else stringResource(id = R.string.time_quick_pick_format, minutes),
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.55f),
                            )
                        }
                    }
                }
            }
        }

        // ── Favourite Genres ───────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Default.TheaterComedy, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = stringResource(id = R.string.genres_section_title),
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            allGenres.forEach { genreResId ->
                val isSelected = selectedGenres.value.contains(genreResId)
                val genreName = stringResource(id = genreResId)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) NeonRed.copy(alpha = 0.20f) else Color.White.copy(alpha = 0.05f))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.08f),
                            shape = CircleShape,
                        )
                        .clickable {
                            selectedGenres.value = if (isSelected)
                                selectedGenres.value - genreResId
                            else
                                selectedGenres.value + genreResId
                        }
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = genreName,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.60f),
                    )
                }
            }
        }

        // ── Active Platforms ───────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Default.Tv, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = stringResource(id = R.string.platforms_section_title),
        )

        // 2-column grid
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            platforms.chunked(2).forEach { rowPlatforms ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    rowPlatforms.forEach { platform ->
                        val isSelected = selectedPlatforms.value.contains(platform.nameResId)
                        PlatformCard(
                            platform = platform,
                            isSelected = isSelected,
                            onClick = {
                                selectedPlatforms.value = if (isSelected)
                                    selectedPlatforms.value - platform.nameResId
                                else
                                    selectedPlatforms.value + platform.nameResId
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    // Fill last row if odd number
                    if (rowPlatforms.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        // ── CTA ────────────────────────────────────────────────────────────
        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(NeonRed.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    }
                    Column {
                        Text(
                            text = stringResource(id = R.string.cta_ai_match),
                            fontFamily = SplineSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                        )
                        Text(
                            text = stringResource(id = R.string.cta_ai_match_subtitle),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.50f),
                        )
                    }
                }

                Button(
                    onClick = {
                        val domainGenres = selectedGenres.value.map { uiGenreResId ->
                            when (uiGenreResId) {
                                R.string.genre_scifi -> Genre.SCIENCE_FICTION
                                R.string.genre_neonoir -> Genre.CRIME
                                R.string.genre_cyberpunk -> Genre.SCIENCE_FICTION
                                R.string.genre_psychological_thriller -> Genre.THRILLER
                                R.string.genre_documentary -> Genre.DOCUMENTARY
                                R.string.genre_horror -> Genre.HORROR
                                R.string.genre_animation -> Genre.ANIMATION
                                else -> Genre.OTHER
                            }
                        }
                        onStartRoulette(sliderMinutes, domainGenres)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor   = Color.White,
                    ),
                ) {
                    Icon(Icons.Default.PlayCircle, null, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.btn_start_roulette),
                        fontFamily = SplineSans,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        letterSpacing = 3.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

// ── Sub-composables ────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    icon: @Composable () -> Unit,
    title: String,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            icon()
            Text(
                text = title,
                fontFamily = SplineSans,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
            )
        }
        trailing?.invoke()
    }
}

@Composable
private fun PlatformCard(
    platform: PlatformItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val platformName = stringResource(id = platform.nameResId)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF131313).copy(alpha = 0.70f))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp),
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Check badge
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                if (isSelected) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = stringResource(id = R.string.desc_selected),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Spacer(Modifier.size(20.dp))
                }
            }

            // Logo
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = platform.logoUrl,
                    contentDescription = platformName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(44.dp)
                        .padding(4.dp),
                )
            }

            Text(
                text = platformName,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = if (isSelected) 1f else 0.60f),
            )
        }
    }
}
