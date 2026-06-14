package dam_A51811.filmroulette.ui.screens.filters

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.data.model.Genre
import dam_A51811.filmroulette.ui.components.GlassCard
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans

// ── Data ────────────────────────────────────────────────────────────────────

private data class LanguageOption(val code: String, val label: String)

private val allLanguages = listOf(
    LanguageOption("en", "🇬🇧 English"),
    LanguageOption("pt", "🇵🇹 Portuguese"),
    LanguageOption("fr", "🇫🇷 French"),
    LanguageOption("ja", "🇯🇵 Japanese"),
    LanguageOption("es", "🇪🇸 Spanish"),
    LanguageOption("ko", "🇰🇷 Korean"),
    LanguageOption("de", "🇩🇪 German"),
    LanguageOption("it", "🇮🇹 Italian"),
    LanguageOption("zh", "🇨🇳 Chinese"),
    LanguageOption("hi", "🇮🇳 Hindi"),
    LanguageOption("ru", "🇷🇺 Russian"),
    LanguageOption("ar", "🇸🇦 Arabic"),
    LanguageOption("bn", "🇧🇩 Bengali"),
    LanguageOption("ta", "🇮🇳 Tamil"),
    LanguageOption("te", "🇮🇳 Telugu")
)

private val defaultLanguageCodes = setOf("en", "pt", "fr", "ja", "es")

private data class DecadeOption(val label: String, val gte: String, val lte: String)

private val allDecades = listOf(
    DecadeOption("30s",    "1930-01-01", "1939-12-31"),
    DecadeOption("40s",    "1940-01-01", "1949-12-31"),
    DecadeOption("50s",    "1950-01-01", "1959-12-31"),
    DecadeOption("60s",    "1960-01-01", "1969-12-31"),
    DecadeOption("70s",    "1970-01-01", "1979-12-31"),
    DecadeOption("80s",    "1980-01-01", "1989-12-31"),
    DecadeOption("90s",    "1990-01-01", "1999-12-31"),
    DecadeOption("2000s",  "2000-01-01", "2009-12-31"),
    DecadeOption("2010s",  "2010-01-01", "2019-12-31"),
    DecadeOption("Recent", "2020-01-01", "2099-12-31"),
)

private val allGenreItems = listOf(
    Genre.ACTION       to "Action",
    Genre.ADVENTURE    to "Adventure",
    Genre.ANIMATION    to "Animation",
    Genre.COMEDY       to "Comedy",
    Genre.CRIME        to "Crime",
    Genre.DOCUMENTARY  to "Documentary",
    Genre.DRAMA        to "Drama",
    Genre.FAMILY       to "Family",
    Genre.FANTASY      to "Fantasy",
    Genre.HISTORY      to "History",
    Genre.HORROR       to "Horror",
    Genre.MUSIC        to "Music",
    Genre.MYSTERY      to "Mystery",
    Genre.ROMANCE      to "Romance",
    Genre.SCIENCE_FICTION to "Sci-Fi",
    Genre.THRILLER     to "Thriller",
    Genre.TV_MOVIE     to "TV Movie",
    Genre.WAR          to "War",
    Genre.WESTERN      to "Western",
)

private data class PlatformItem(val nameResId: Int, val logoUrl: String, val providerId: Int)

private val platforms = listOf(
    PlatformItem(R.string.platform_netflix,    "https://upload.wikimedia.org/wikipedia/commons/0/08/Netflix_2015_logo.svg", 8),
    PlatformItem(R.string.platform_disneyplus, "https://upload.wikimedia.org/wikipedia/commons/3/3e/Disney%2B_logo.svg", 337),
    PlatformItem(R.string.platform_max,        "https://upload.wikimedia.org/wikipedia/commons/1/17/HBO_Max_Logo.svg", 1899),
    PlatformItem(R.string.platform_primevideo, "https://upload.wikimedia.org/wikipedia/commons/1/11/Amazon_Prime_Video_logo.svg", 119),
)

private val quickPickMinutes = listOf(60, 90, 120, 180, 241)

/**
 * Filters / Onboarding screen.
 *
 * Lets the user configure available time, favourite genres, active
 * streaming platforms, language and decade era before spinning the roulette.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FiltersScreen(
    onStartRoulette: (Int, List<Genre>, List<String>, String?, String?, String?) -> Unit = { _, _, _, _, _, _ -> },
    modifier: Modifier = Modifier,
) {
    var sliderMinutes by remember { mutableIntStateOf(120) }
    val selectedGenres = remember { mutableStateOf<Set<Genre>>(emptySet()) }
    val selectedPlatforms = remember { mutableStateOf<Set<Int>>(emptySet()) }
    val selectedLanguages = remember { mutableStateOf(defaultLanguageCodes) }
    var languageDropdownExpanded by remember { mutableStateOf(false) }
    val selectedDecades = remember { mutableStateOf<Set<String>>(setOf("80s", "90s", "2000s", "2010s", "Recent")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(top = 80.dp, bottom = 120.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {


        // ── Available Time ─────────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Default.Schedule, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = stringResource(id = R.string.time_section_title),
            trailing = {
                Text(
                    text = if (sliderMinutes >= 241) "240+ min" else stringResource(id = R.string.time_minutes_format, sliderMinutes),
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
                    valueRange = 30f..241f,
                    steps = 0,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor          = MaterialTheme.colorScheme.primary,
                        activeTrackColor    = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
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
                                .background(if (isSelected) NeonRed.copy(alpha = 0.20f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.60f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                .clickable { sliderMinutes = minutes }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = when (minutes) {
                                    241 -> "240+"
                                    else -> stringResource(id = R.string.time_quick_pick_format, minutes)
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                            )
                        }
                    }
                }
            }
        }

        // ── Favourite Genres ───────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Default.TheaterComedy, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = "Genres",
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            allGenreItems.forEach { (genre, label) ->
                val isSelected = selectedGenres.value.contains(genre)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) NeonRed.copy(alpha = 0.20f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            shape = CircleShape,
                        )
                        .clickable {
                            selectedGenres.value = if (isSelected)
                                selectedGenres.value - genre
                            else
                                selectedGenres.value + genre
                        }
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
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

        // ── Language filter ───────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Default.Language, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = "Language",
            trailing = {
                Text(
                    text = "${selectedLanguages.value.size} selected",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        )

        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(modifier = Modifier.padding(4.dp)) {
                // Dropdown header button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { languageDropdownExpanded = !languageDropdownExpanded }
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val previewText = if (selectedLanguages.value.isEmpty()) {
                        "No language filter"
                    } else {
                        // Strip the flag emoji (two regional-indicator chars) and the space after it
                        allLanguages.filter { it.code in selectedLanguages.value }
                            .joinToString(", ") { it.label.replace(Regex("^[\\uD83C][\\uDDE6-\\uDDFF]{2}\\s*"), "") }
                    }
                    Text(
                        text = previewText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (languageDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                // Expandable checklist
                AnimatedVisibility(visible = languageDropdownExpanded) {
                    Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                        allLanguages.forEach { lang ->
                            val isChecked = lang.code in selectedLanguages.value
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedLanguages.value = if (isChecked)
                                            selectedLanguages.value - lang.code
                                        else
                                            selectedLanguages.value + lang.code
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = NeonRed,
                                        uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                                        checkmarkColor = Color.White
                                    )
                                )
                                Text(
                                    text = lang.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isChecked) Color.White else Color.White.copy(alpha = 0.55f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Decade / Era filter ────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Default.Timeline, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = "Era",
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            allDecades.forEach { decade ->
                val isSelected = selectedDecades.value.contains(decade.label)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) NeonRed.copy(alpha = 0.20f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            shape = CircleShape,
                        )
                        .clickable {
                            selectedDecades.value = if (isSelected)
                                selectedDecades.value - decade.label
                            else
                                selectedDecades.value + decade.label
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = decade.label,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                    )
                }
            }
        }

        // ── CTA ────────────────────────────────────────────────────────────
        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(
                    onClick = {
                        // Compute date range from selected decades (union)
                        val dateGte = if (selectedDecades.value.isEmpty()) null else
                            allDecades.filter { it.label in selectedDecades.value }.minOfOrNull { it.gte }
                        val dateLte = if (selectedDecades.value.isEmpty()) null else
                            allDecades.filter { it.label in selectedDecades.value }.maxOfOrNull { it.lte }

                        // Map selected platform res IDs back to provider IDs, joined by |
                        val providerIds = if (selectedPlatforms.value.isEmpty()) null else {
                            platforms.filter { it.nameResId in selectedPlatforms.value }
                                .joinToString("|") { it.providerId.toString() }
                        }

                        onStartRoulette(
                            // 241 is the sentinel for "no limit" — pass Int.MAX_VALUE so the repo sends no runtime cap
                            if (sliderMinutes >= 241) Int.MAX_VALUE else sliderMinutes,
                            selectedGenres.value.toList(),
                            selectedLanguages.value.toList(),
                            dateGte,
                            dateLte,
                            providerIds
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonRed,
                        contentColor   = Color.White,
                    ),
                ) {
                    Icon(Icons.Default.PlayCircle, null, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Apply & Leave",
                        fontFamily = SplineSans,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        letterSpacing = 2.sp,
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
                color = MaterialTheme.colorScheme.onBackground,
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
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.70f))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
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
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isSelected) 1f else 0.60f),
            )
        }
    }
}
