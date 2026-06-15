package dam_A51811.filmroulette.ui.screens.roulette

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.data.model.Genre
import dam_A51811.filmroulette.ui.screens.aiguide.AiGuideViewModel
import dam_A51811.filmroulette.ui.screens.roulette.RouletteUiState
import dam_A51811.filmroulette.ui.screens.roulette.RouletteViewModel
import dam_A51811.filmroulette.ui.screens.watchlist.WatchlistViewModel
import dam_A51811.filmroulette.ui.components.GlassCard
import dam_A51811.filmroulette.ui.screens.groups.GroupSessionViewModel
import dam_A51811.filmroulette.ui.screens.aiguide.AiMovieSheet
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans

/**
 * The main screen for the movie roulette feature, displaying a stack of movie recommendations.
 * Allows users to swipe left or right to interact with the movies.
 *
 * @param viewModel The ViewModel handling the roulette logic and state.
 * @param aiGuideViewModel The ViewModel for AI movie insights.
 * @param watchlistViewModel The ViewModel for managing user watchlists.
 * @param groupSessionViewModel The ViewModel for group session management, if applicable.
 * @param modifier The modifier to be applied to the layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouletteScreen(
    viewModel: RouletteViewModel,
    aiGuideViewModel: AiGuideViewModel,
    watchlistViewModel: WatchlistViewModel,
    groupSessionViewModel: GroupSessionViewModel? = null,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSynopsis by remember { mutableStateOf(false) }
    var showAiSheet by remember { mutableStateOf(false) }
    var showWatchlistSheet by remember { mutableStateOf(false) }
    var swipeOffsetX by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    val groupUiState by groupSessionViewModel?.uiState?.collectAsState() ?: remember { mutableStateOf(null) }
    val isGroupMode = groupUiState?.session?.status == dam_A51811.filmroulette.data.model.SessionStatus.SPINNING
    var groupCurrentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        if (!isGroupMode && viewModel.uiState.value is RouletteUiState.Idle) {
            viewModel.loadRecommendations(maxDuration = 240, genres = emptyList())
        }
    }

    val activeState = if (isGroupMode) {
        val deck = groupUiState?.session?.moviesDeck ?: emptyList()
        if (deck.isNotEmpty() && groupCurrentIndex < deck.size) {
            RouletteUiState.Success(deck, groupCurrentIndex)
        } else if (groupCurrentIndex >= deck.size) {
            RouletteUiState.Error(message = "Waiting for others to finish swiping...")
        } else {
            RouletteUiState.Loading
        }
    } else {
        uiState
    }

    
    val currentIndex = (activeState as? RouletteUiState.Success)?.currentIndex ?: 0
    LaunchedEffect(currentIndex) { swipeOffsetX = 0f }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NeonRed.copy(alpha = 0.12f),
                            Color.Transparent,
                        ),
                        radius = 900f,
                    )
                )
        )

        when (val state = activeState) {
            is RouletteUiState.Idle,
            is RouletteUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonRed)
                }
            }
            is RouletteUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.error_title),
                                fontFamily = SplineSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                            val errorMessage = when {
                                state.messageResId != null -> stringResource(id = state.messageResId)
                                state.message != null -> state.message
                                else -> stringResource(id = R.string.error_unexpected)
                            }
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f),
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = { viewModel.loadRecommendations(240, emptyList()) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.btn_retry),
                                    fontFamily = SplineSans,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            is RouletteUiState.Success -> {
                val movie = state.movies[state.currentIndex]

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(top = 64.dp, bottom = 120.dp, start = 24.dp, end = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 3f)
                            .graphicsLayer {
                                
                                rotationZ = (swipeOffsetX / 30f).coerceIn(-15f, 15f)
                                translationX = swipeOffsetX * 0.4f
                            }
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                            .pointerInput(state.currentIndex) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        val threshold = 150f
                                        when {
                                            swipeOffsetX > threshold -> {
                                                
                                                showSynopsis = false
                                                if (isGroupMode) {
                                                    groupSessionViewModel?.swipeRight(movie)
                                                    groupCurrentIndex++
                                                } else {
                                                    viewModel.nextMovie()
                                                }
                                                watchlistViewModel.addMovieToLikedList(movie.id)
                                            }
                                            swipeOffsetX < -threshold -> {
                                                
                                                showSynopsis = false
                                                if (isGroupMode) {
                                                    groupCurrentIndex++
                                                } else {
                                                    viewModel.nextMovie()
                                                }
                                            }
                                        }
                                        swipeOffsetX = 0f
                                    },
                                    onDragCancel = { swipeOffsetX = 0f },
                                    onHorizontalDrag = { _, dragAmount ->
                                        swipeOffsetX += dragAmount
                                    }
                                )
                            }
                            .pointerInput(Unit) {
                                detectTapGestures { showSynopsis = !showSynopsis }
                            },
                    ) {
                        AsyncImage(
                            model = movie.imgUrl,
                            contentDescription = movie.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )

                        Row(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            movie.genres.take(3).forEach { genre ->
                                GenreChip(genre)
                            }
                        }

                        val synopsisAlpha by animateFloatAsState(
                            targetValue = if (showSynopsis) 1f else 0f,
                            animationSpec = tween(300),
                            label = "synopsis_alpha",
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(synopsisAlpha)
                                .background(
                                    Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            0.0f to Color.Transparent,
                                            0.4f to Color.Black.copy(alpha = 0.5f),
                                            1.0f to Color.Black.copy(alpha = 0.95f),
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.BottomStart,
                        ) {
                            Text(
                                text = movie.synopsys,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.padding(20.dp),
                            )
                        }

                        
                        if (!movie.originalLanguage.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(12.dp)
                            ) {
                                LanguageFlagChip(movie.originalLanguage)
                            }
                        }

                        
                        IconButton(
                            onClick = { showWatchlistSheet = true },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.BookmarkAdd,
                                contentDescription = "Add to Watchlist",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = movie.title,
                        fontFamily = SplineSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (!movie.releaseDate.isNullOrBlank()) {
                            Text(
                                text = movie.releaseDate.take(4),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f),
                            )
                            MetaDot()
                        }
                        Text(
                            text = stringResource(id = R.string.duration_mins, movie.duration),
                            style = MaterialTheme.typography.labelLarge,

                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f),
                        )
                        MetaDot()
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(14.dp),
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(
                            text = String.format("%.1f", movie.avgRating),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    
                    OutlinedButton(
                        onClick = { showAiSheet = true },
                        shape  = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFF9C6FFF).copy(alpha = 0.60f)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF9C6FFF),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            stringResource(R.string.roulette_ai_insights),
                            fontFamily = SplineSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.5.sp,
                            color = Color(0xFF9C6FFF)
                        )
                    }

                    Spacer(Modifier.height(36.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ReactionButton(
                            onClick = {
                                showSynopsis = false
                                if (isGroupMode) {
                                    groupCurrentIndex++
                                } else {
                                    viewModel.nextMovie()
                                }
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = stringResource(id = R.string.desc_dislike),
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(32.dp),
                                )
                            },
                        )

                        Spacer(Modifier.width(32.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.KeyboardDoubleArrowLeft,
                                contentDescription = stringResource(id = R.string.desc_swipe),
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.40f),
                                modifier = Modifier.size(18.dp),
                            )
                            Text(
                                text = stringResource(id = R.string.label_swipe),
                                fontFamily = SplineSans,
                                fontWeight = FontWeight.Black,
                                fontSize = 7.sp,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.40f),
                            )
                        }

                        Spacer(Modifier.width(32.dp))

                        ReactionButton(
                            onClick = {
                                showSynopsis = false
                                if (isGroupMode) {
                                    groupSessionViewModel?.swipeRight(movie)
                                    groupCurrentIndex++
                                } else {
                                    viewModel.nextMovie()
                                }
                            },
                            icon = {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = stringResource(id = R.string.desc_like),
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(32.dp),
                                )
                            },
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.poster_synopsis),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.40f),
                    )
                }

                if (state.currentIndex > 0) {
                    IconButton(
                        onClick = { viewModel.previousMovie() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 16.dp, start = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Previous movie",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

    
    if (showAiSheet) {
        val successState = uiState
        val movieTitle = if (successState is RouletteUiState.Success)
            successState.movies[successState.currentIndex].title else ""
        if (movieTitle.isNotEmpty()) {
            AiMovieSheet(
                movieTitle   = movieTitle,
                viewModel    = aiGuideViewModel,
                onDismiss    = { showAiSheet = false }
            )
        }
    }

    
    if (showWatchlistSheet) {
        val successState = uiState
        val movie = if (successState is RouletteUiState.Success)
            successState.movies[successState.currentIndex] else null
            
        if (movie != null) {
            val watchlists by watchlistViewModel.watchlists.collectAsState()
            val sheetState = rememberModalBottomSheetState()
            
            ModalBottomSheet(
                onDismissRequest = { showWatchlistSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_add_to_watchlist),
                        fontFamily = dam_A51811.filmroulette.ui.theme.SplineSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    if (watchlists.isEmpty()) {
                        Text(
                            text = stringResource(R.string.empty_watchlists),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        watchlists.forEach { list ->
                            androidx.compose.foundation.layout.Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        watchlistViewModel.addMovieToList(list.id, movie.id)
                                        showWatchlistSheet = false
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.Bookmarks,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = list.name,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    
    if (groupUiState?.session?.status == dam_A51811.filmroulette.data.model.SessionStatus.MATCHED) {
        val matchedMovie = groupUiState?.session?.matchedMovie
        if (matchedMovie != null) {
            MatchOverlay(movie = matchedMovie, onDismiss = {  })
        }
    }
}

/**
 * An overlay displayed when a group session results in a movie match.
 *
 * @param movie The movie that was successfully matched by the group.
 * @param onDismiss A callback invoked when the overlay is dismissed.
 */
@Composable
fun MatchOverlay(movie: dam_A51811.filmroulette.data.model.Movie, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = dam_A51811.filmroulette.ui.theme.NeonRed,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.title_its_a_match),
                fontFamily = SplineSans,
                fontWeight = FontWeight.Black,
                fontSize = 36.sp,
                color = dam_A51811.filmroulette.ui.theme.NeonRed,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.match_everyone_swiped),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, dam_A51811.filmroulette.ui.theme.NeonRed, RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = movie.imgUrl,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = movie.title,
                fontFamily = SplineSans,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * A UI component displaying a single genre as a styled chip.
 *
 * @param genre The genre to display.
 */
@Composable
private fun GenreChip(genre: Genre) {
    val labelResId = when(genre) {
        Genre.ACTION -> R.string.genre_action
        Genre.ADVENTURE -> R.string.genre_adventure
        Genre.ANIMATION -> R.string.genre_animation
        Genre.COMEDY -> R.string.genre_comedy
        Genre.CRIME -> R.string.genre_crime
        Genre.DOCUMENTARY -> R.string.genre_documentary
        Genre.DRAMA -> R.string.genre_drama
        Genre.FAMILY -> R.string.genre_family
        Genre.FANTASY -> R.string.genre_fantasy
        Genre.HISTORY -> R.string.genre_history
        Genre.HORROR -> R.string.genre_horror
        Genre.MUSIC -> R.string.genre_music
        Genre.MYSTERY -> R.string.genre_mystery
        Genre.ROMANCE -> R.string.genre_romance
        Genre.SCIENCE_FICTION -> R.string.genre_scifi
        Genre.THRILLER -> R.string.genre_thriller
        Genre.TV_MOVIE -> R.string.genre_tv_movie
        Genre.WAR -> R.string.genre_war
        Genre.WESTERN -> R.string.genre_western
        Genre.OTHER -> null
    }
    val label = labelResId?.let { stringResource(id = it) } ?: genre.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(NeonRed.copy(alpha = 0.40f))
            .border(0.5.dp, NeonRed.copy(alpha = 0.60f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

/**
 * A simple UI component representing a separator dot for metadata fields.
 */
@Composable
private fun MetaDot() {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(4.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f)),
    )
}

/**
 * A circular button used for interacting with the currently displayed movie (e.g., like or dislike).
 *
 * @param onClick The callback invoked when the button is clicked.
 * @param icon The composable defining the icon inside the button.
 */
@Composable
private fun ReactionButton(onClick: () -> Unit, icon: @Composable () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.90f else 1f,
        animationSpec = tween(120),
        label = "btn_scale",
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f), CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                    },
                )
            },
    ) {
        icon()
    }
}

/**
 * A UI component displaying the primary language of a movie along with its corresponding flag emoji.
 *
 * @param languageCode The ISO 639-1 language code.
 */
@Composable
private fun LanguageFlagChip(languageCode: String) {
    val flag = when (languageCode.lowercase()) {
        "en" -> "🇬🇧"
        "pt" -> "🇵🇹"
        "fr" -> "🇫🇷"
        "ja" -> "🇯🇵"
        "es" -> "🇪🇸"
        "ko" -> "🇰🇷"
        "de" -> "🇩🇪"
        "it" -> "🇮🇹"
        "zh" -> "🇨🇳"
        "hi" -> "🇮🇳"
        "ru" -> "🇷🇺"
        "ar" -> "🇸🇦"
        "bn" -> "🇧🇩"
        "ta" -> "🇮🇳"
        "te" -> "🇮🇳"
        else -> "🌐"
    }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .border(0.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${languageCode.uppercase()} $flag",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
