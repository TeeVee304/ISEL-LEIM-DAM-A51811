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
import androidx.compose.runtime.collectAsState
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.data.model.Genre
import dam_A51811.filmroulette.data.model.SharedFilters
import dam_A51811.filmroulette.ui.components.GlassCard
import dam_A51811.filmroulette.ui.screens.groups.GroupSessionViewModel
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans



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

private data class DecadeOption(val id: String, val labelResId: Int?, val gte: String, val lte: String)

private val allDecades = listOf(
    DecadeOption("30s",    null, "1930-01-01", "1939-12-31"),
    DecadeOption("40s",    null, "1940-01-01", "1949-12-31"),
    DecadeOption("50s",    null, "1950-01-01", "1959-12-31"),
    DecadeOption("60s",    null, "1960-01-01", "1969-12-31"),
    DecadeOption("70s",    null, "1970-01-01", "1979-12-31"),
    DecadeOption("80s",    null, "1980-01-01", "1989-12-31"),
    DecadeOption("90s",    null, "1990-01-01", "1999-12-31"),
    DecadeOption("2000s",  null, "2000-01-01", "2009-12-31"),
    DecadeOption("2010s",  null, "2010-01-01", "2019-12-31"),
    DecadeOption("Recent", R.string.decade_recent, "2020-01-01", "2099-12-31"),
)

private val allGenreItems = listOf(
    Genre.ACTION       to R.string.genre_action,
    Genre.ADVENTURE    to R.string.genre_adventure,
    Genre.ANIMATION    to R.string.genre_animation,
    Genre.COMEDY       to R.string.genre_comedy,
    Genre.CRIME        to R.string.genre_crime,
    Genre.DOCUMENTARY  to R.string.genre_documentary,
    Genre.DRAMA        to R.string.genre_drama,
    Genre.FAMILY       to R.string.genre_family,
    Genre.FANTASY      to R.string.genre_fantasy,
    Genre.HISTORY      to R.string.genre_history,
    Genre.HORROR       to R.string.genre_horror,
    Genre.MUSIC        to R.string.genre_music,
    Genre.MYSTERY      to R.string.genre_mystery,
    Genre.ROMANCE      to R.string.genre_romance,
    Genre.SCIENCE_FICTION to R.string.genre_scifi,
    Genre.THRILLER     to R.string.genre_thriller,
    Genre.TV_MOVIE     to R.string.genre_tv_movie,
    Genre.WAR          to R.string.genre_war,
    Genre.WESTERN      to R.string.genre_western,
)

private data class PlatformItem(val nameResId: Int, val logoUrl: String, val providerId: Int)

private val platforms = listOf(
    PlatformItem(R.string.platform_netflix,    "https://image.tmdb.org/t/p/w500/wwemzKWzjKYJFfCeiB57q3r4Bcm.png", 8),
    PlatformItem(R.string.platform_disneyplus, "https://image.tmdb.org/t/p/w200/97yvRBw1GzX7fXprcF80er19ot.jpg", 337),
    PlatformItem(R.string.platform_max,        "https://image.tmdb.org/t/p/w200/fksCUZ9QDWZMUwL2LgMtLckROUN.jpg", 1899),
    PlatformItem(R.string.platform_primevideo, "https://image.tmdb.org/t/p/w200/pvske1MyAoymrs5bguRfVqYiM9a.jpg", 119),
)

private val quickPickMinutes = listOf(60, 90, 120, 180, 241)


/**
 * Displays the filters screen, allowing users to configure filtering criteria for the film roulette.
 *
 * @param onStartRoulette Callback invoked to apply the selected filters and proceed. Provides the maximum runtime in minutes, a list of selected [Genre]s, a list of selected language codes, the earliest release date, the latest release date, and a pipe-separated string of provider IDs.
 * @param groupSessionViewModel The [GroupSessionViewModel] managing the shared session state, if a group session is active.
 * @param modifier The [Modifier] to be applied to this composable.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FiltersScreen(
    onStartRoulette: (Int, List<Genre>, List<String>, String?, String?, String?) -> Unit = { _, _, _, _, _, _ -> },
    groupSessionViewModel: GroupSessionViewModel? = null,
    modifier: Modifier = Modifier,
) {
    val uiState by groupSessionViewModel?.uiState?.collectAsState() ?: remember { mutableStateOf(null) }
    val isGroupActive = uiState?.session != null

    var localSliderMinutes by remember { mutableIntStateOf(120) }
    val localSelectedGenres = remember { mutableStateOf<Set<Genre>>(emptySet()) }
    val localSelectedPlatforms = remember { mutableStateOf<Set<Int>>(emptySet()) }
    val localSelectedLanguages = remember { mutableStateOf(defaultLanguageCodes) }
    var languageDropdownExpanded by remember { mutableStateOf(false) }
    val localSelectedDecades = remember { mutableStateOf<Set<String>>(setOf("80s", "90s", "2000s", "2010s", "Recent")) }

    val sliderMinutes = if (isGroupActive) uiState!!.session!!.sharedFilters.maxMinutes else localSliderMinutes
    val selectedGenres = if (isGroupActive) uiState!!.session!!.sharedFilters.selectedGenres.map { Genre.valueOf(it) }.toSet() else localSelectedGenres.value
    val selectedPlatforms = if (isGroupActive) uiState!!.session!!.sharedFilters.selectedPlatforms.toSet() else localSelectedPlatforms.value
    val selectedLanguages = if (isGroupActive) uiState!!.session!!.sharedFilters.selectedLanguages.toSet() else localSelectedLanguages.value
    val selectedDecades = if (isGroupActive) uiState!!.session!!.sharedFilters.selectedDecades.toSet() else localSelectedDecades.value

    fun updateSharedFilter(block: (SharedFilters) -> SharedFilters) {
        if (isGroupActive) {
            groupSessionViewModel?.updateSharedFilters(block(uiState!!.session!!.sharedFilters))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(top = 80.dp, bottom = 120.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {


        
        SectionHeader(
            icon = { Icon(Icons.Default.Schedule, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = stringResource(id = R.string.time_title),
            trailing = {
                Text(
                    text = if (sliderMinutes >= 241) "240+ min" else stringResource(id = R.string.time_minutes, sliderMinutes),
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
                    onValueChange = { newValue ->
                        if (isGroupActive) {
                            updateSharedFilter { it.copy(maxMinutes = newValue.toInt()) }
                        } else {
                            localSliderMinutes = newValue.toInt()
                        }
                    },
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
                                .clickable {
                                    if (isGroupActive) {
                                        updateSharedFilter { it.copy(maxMinutes = minutes) }
                                    } else {
                                        localSliderMinutes = minutes
                                    }
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = when (minutes) {
                                    241 -> "240+"
                                    else -> stringResource(id = R.string.time_quick_pick, minutes)
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                            )
                        }
                    }
                }
            }
        }

        
        SectionHeader(
            icon = { Icon(Icons.Default.TheaterComedy, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = stringResource(R.string.filter_title_genres),
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            allGenreItems.forEach { (genre, label) ->
                val isSelected = selectedGenres.contains(genre)
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
                            if (isGroupActive) {
                                val newGenres = if (isSelected) selectedGenres - genre else selectedGenres + genre
                                updateSharedFilter { it.copy(selectedGenres = newGenres.map { g -> g.name }) }
                            } else {
                                localSelectedGenres.value = if (isSelected) localSelectedGenres.value - genre else localSelectedGenres.value + genre
                            }
                        }
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = stringResource(id = label),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                    )
                }
            }
        }

        
        SectionHeader(
            icon = { Icon(Icons.Default.Tv, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = stringResource(id = R.string.platforms_title),
        )

        
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            platforms.chunked(2).forEach { rowPlatforms ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    rowPlatforms.forEach { platform ->
                        val isSelected = selectedPlatforms.contains(platform.nameResId)
                        PlatformCard(
                            platform = platform,
                            isSelected = isSelected,
                            onClick = {
                                if (isGroupActive) {
                                    val newPlatforms = if (isSelected) selectedPlatforms - platform.nameResId else selectedPlatforms + platform.nameResId
                                    updateSharedFilter { it.copy(selectedPlatforms = newPlatforms.toList()) }
                                } else {
                                    localSelectedPlatforms.value = if (isSelected) localSelectedPlatforms.value - platform.nameResId else localSelectedPlatforms.value + platform.nameResId
                                }
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    
                    if (rowPlatforms.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        
        SectionHeader(
            icon = { Icon(Icons.Default.Language, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = stringResource(R.string.filter_title_language),
            trailing = {
                Text(
                    text = stringResource(R.string.filter_selected_count, selectedLanguages.size),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        )

        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(modifier = Modifier.padding(4.dp)) {
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { languageDropdownExpanded = !languageDropdownExpanded }
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val previewText = if (selectedLanguages.isEmpty()) {
                        stringResource(R.string.filter_no_language)
                    } else {
                        
                        allLanguages.filter { it.code in selectedLanguages }
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
                
                AnimatedVisibility(visible = languageDropdownExpanded) {
                    Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                        allLanguages.forEach { lang ->
                            val isChecked = lang.code in selectedLanguages
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isGroupActive) {
                                            val newLangs = if (isChecked) selectedLanguages - lang.code else selectedLanguages + lang.code
                                            updateSharedFilter { it.copy(selectedLanguages = newLangs.toList()) }
                                        } else {
                                            localSelectedLanguages.value = if (isChecked) localSelectedLanguages.value - lang.code else localSelectedLanguages.value + lang.code
                                        }
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

        
        SectionHeader(
            icon = { Icon(Icons.Default.Timeline, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) },
            title = stringResource(R.string.filter_title_era),
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            allDecades.forEach { decade ->
                val isSelected = selectedDecades.contains(decade.id)
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
                            if (isGroupActive) {
                                val newDecades = if (isSelected) selectedDecades - decade.id else selectedDecades + decade.id
                                updateSharedFilter { it.copy(selectedDecades = newDecades.toList()) }
                            } else {
                                localSelectedDecades.value = if (isSelected) localSelectedDecades.value - decade.id else localSelectedDecades.value + decade.id
                            }
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = if (decade.labelResId != null) stringResource(decade.labelResId) else decade.id,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                    )
                }
            }
        }

        
        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(
                    onClick = {
                        
                        val dateGte = if (selectedDecades.isEmpty()) null else
                            allDecades.filter { it.id in selectedDecades }.minOfOrNull { it.gte }
                        val dateLte = if (selectedDecades.isEmpty()) null else
                            allDecades.filter { it.id in selectedDecades }.maxOfOrNull { it.lte }

                        
                        val providerIds = if (selectedPlatforms.isEmpty()) null else {
                            platforms.filter { it.nameResId in selectedPlatforms }
                                .joinToString("|") { it.providerId.toString() }
                        }

                        onStartRoulette(
                            
                            if (sliderMinutes >= 241) Int.MAX_VALUE else sliderMinutes,
                            selectedGenres.toList(),
                            selectedLanguages.toList(),
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
                        text = stringResource(R.string.btn_apply_and_leave),
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
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
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
