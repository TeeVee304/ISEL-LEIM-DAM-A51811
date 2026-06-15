package dam_A51811.filmroulette.ui.screens.aiguide

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dam_A51811.filmroulette.R
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam_A51811.filmroulette.ui.screens.aiguide.AiGuideUiState
import dam_A51811.filmroulette.ui.screens.aiguide.AiGuideViewModel
import dam_A51811.filmroulette.ui.components.GlassCard
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans





/**
 * Displays the AI Guide screen, allowing the user to search for movies by description or get smart recommendations based on mood.
 *
 * @param viewModel The view model managing the state and logic for the AI guide.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun AiGuideScreen(
    viewModel: AiGuideViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Movie Describer", "Smart Picks")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF6C00FF).copy(alpha = 0.10f), Color.Transparent),
                        radius = 900f
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {

            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFF9C6FFF),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.title_ai_guide),
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    letterSpacing = 4.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.subtitle_powered_by_gemini),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9C6FFF).copy(alpha = 0.80f),
                    letterSpacing = 1.sp
                )
            }

            
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor   = Color.Transparent,
                contentColor     = MaterialTheme.colorScheme.onSurface,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color    = Color(0xFF9C6FFF)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick  = { selectedTab = index },
                        text = {
                            Text(
                                text       = title,
                                fontFamily = SplineSans,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize   = 12.sp,
                                letterSpacing = 1.sp,
                                color = if (selectedTab == index) Color(0xFF9C6FFF) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.50f)
                            )
                        }
                    )
                }
            }

            
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    (fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 8 })
                        .togetherWith(fadeOut(tween(200)))
                },
                label = "tab_content"
            ) { tab ->
                when (tab) {
                    0 -> MovieDescriberTab(viewModel)
                    1 -> SmartPicksTab(viewModel)
                }
            }
        }
    }
}





/**
 * Displays the Movie Describer tab, enabling the user to query the AI for movie descriptions.
 *
 * @param viewModel The view model managing the AI guide state.
 */
@Composable
private fun MovieDescriberTab(viewModel: AiGuideViewModel) {
    val state by viewModel.describeState.collectAsState()
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.desc_movie_title_prompt),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        AiTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = "e.g. Interstellar, Blade Runner…",
            leadingIcon = { Icon(Icons.Filled.Search, null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f)) }
        )

        AiButton(
            text = stringResource(R.string.btn_describe_movie),
            enabled = query.isNotBlank() && state !is AiGuideUiState.Loading,
            onClick = { viewModel.describeMovie(query) }
        )

        AiResultPanel(state = state, onReset = { viewModel.resetDescribe(); query = "" })
    }
}





/**
 * Displays the Smart Picks tab, enabling the user to input a mood to receive AI-driven movie recommendations.
 *
 * @param viewModel The view model managing the AI guide state.
 */
@Composable
private fun SmartPicksTab(viewModel: AiGuideViewModel) {
    val state by viewModel.recommendState.collectAsState()
    var mood by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.desc_mood_prompt),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        AiTextField(
            value = mood,
            onValueChange = { mood = it },
            placeholder = "e.g. Rainy night, sci-fi with a subtle romance…",
            minLines = 3
        )

        AiButton(
            text = stringResource(R.string.btn_get_recommendations),
            enabled = mood.isNotBlank() && state !is AiGuideUiState.Loading,
            onClick = { viewModel.recommendMovies(mood) }
        )

        AiResultPanel(state = state, onReset = { viewModel.resetRecommend(); mood = "" })
    }
}





@Composable
private fun AiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        modifier      = Modifier.fillMaxWidth(),
        placeholder   = {
            Text(placeholder, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f), fontSize = 14.sp)
        },
        leadingIcon   = leadingIcon,
        minLines      = minLines,
        shape         = RoundedCornerShape(14.dp),
        colors        = OutlinedTextFieldDefaults.colors(
            focusedTextColor      = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor    = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor    = Color(0xFF9C6FFF),
            unfocusedBorderColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f),
            cursorColor           = Color(0xFF9C6FFF),
            focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
            unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
        )
    )
}

@Composable
private fun AiButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape  = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor         = Color(0xFF6C00FF),
            disabledContainerColor = Color(0xFF6C00FF).copy(alpha = 0.35f)
        )
    ) {
        Text(
            text       = text,
            fontFamily = SplineSans,
            fontWeight = FontWeight.Black,
            fontSize   = 13.sp,
            letterSpacing = 2.sp,
            color      = Color.White
        )
    }
}

@Composable
private fun AiResultPanel(state: AiGuideUiState, onReset: () -> Unit) {
    AnimatedVisibility(
        visible = state !is AiGuideUiState.Idle,
        enter   = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 4 }
    ) {
        when (state) {
            is AiGuideUiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CircularProgressIndicator(color = Color(0xFF9C6FFF), strokeWidth = 2.dp)
                        Text(
                            "Consulting the AI…",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                        )
                    }
                }
            }

            is AiGuideUiState.Success -> {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFF9C6FFF),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    stringResource(R.string.title_ai_response),
                                    fontFamily = SplineSans,
                                    fontWeight = FontWeight.Black,
                                    fontSize   = 10.sp,
                                    letterSpacing = 2.sp,
                                    color = Color(0xFF9C6FFF)
                                )
                            }
                            
                            androidx.compose.material3.TextButton(onClick = onReset) {
                                Text(stringResource(R.string.btn_clear), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f), fontSize = 12.sp)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                        )

                        Text(
                            text  = state.result,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            is AiGuideUiState.Error -> {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            stringResource(R.string.title_ai_error),
                            fontFamily = SplineSans,
                            fontWeight = FontWeight.Black,
                            fontSize   = 10.sp,
                            letterSpacing = 2.sp,
                            color = NeonRed
                        )
                        Text(
                            state.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                        androidx.compose.material3.TextButton(onClick = onReset) {
                            Text(stringResource(R.string.btn_dismiss), color = NeonRed.copy(alpha = 0.80f), fontSize = 12.sp)
                        }
                    }
                }
            }

            else -> {}
        }
    }
}
