package dam_A51811.filmroulette.ui.screens.roulette

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.data.model.Genre
import dam_A51811.filmroulette.data.ui.roulette.RouletteUiState
import dam_A51811.filmroulette.data.ui.roulette.RouletteViewModel
import dam_A51811.filmroulette.ui.components.GlassCard
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans

@Composable
fun RouletteScreen(
    viewModel: RouletteViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSynopsis by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (viewModel.uiState.value is RouletteUiState.Loading) {
            viewModel.loadRecommendations(240, emptyList())
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF131313)),
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

        when (val state = uiState) {
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
                                color = Color.White.copy(alpha = 0.70f),
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 80.dp, bottom = 120.dp, start = 24.dp, end = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(NeonRed.copy(alpha = 0.60f), Color.Transparent),
                                )
                            )
                    )

                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
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
                    }

                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = movie.title,
                        fontFamily = SplineSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        letterSpacing = 1.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.duration_mins_format, movie.duration),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.55f),
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

                    Spacer(Modifier.height(36.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ReactionButton(
                            onClick = {
                                showSynopsis = false
                                viewModel.nextMovie()
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
                                tint = Color.White.copy(alpha = 0.30f),
                                modifier = Modifier.size(18.dp),
                            )
                            Text(
                                text = stringResource(id = R.string.label_swipe),
                                fontFamily = SplineSans,
                                fontWeight = FontWeight.Black,
                                fontSize = 7.sp,
                                letterSpacing = 2.sp,
                                color = Color.White.copy(alpha = 0.25f),
                            )
                        }

                        Spacer(Modifier.width(32.dp))

                        ReactionButton(
                            onClick = {
                                showSynopsis = false
                                viewModel.nextMovie()
                            },
                            icon = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = stringResource(id = R.string.desc_like),
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(32.dp),
                                )
                            },
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.tap_poster_synopsis),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.25f),
                    )
                }
            }
        }
    }
}

@Composable
private fun GenreChip(genre: Genre) {
    val label = genre.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(NeonRed.copy(alpha = 0.15f))
            .border(0.5.dp, NeonRed.copy(alpha = 0.40f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun MetaDot() {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(4.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.20f)),
    )
}

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
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
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
