package dam_A51811.filmroulette.ui.screens.aiguide

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.res.stringResource
import dam_A51811.filmroulette.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam_A51811.filmroulette.ui.screens.aiguide.AiGuideUiState
import dam_A51811.filmroulette.ui.screens.aiguide.AiGuideViewModel
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans


/**
 * Displays a bottom sheet containing AI-generated insights for a specific movie.
 *
 * @param movieTitle The title of the movie to describe.
 * @param viewModel The ViewModel handling AI operations.
 * @param onDismiss Callback invoked when the bottom sheet is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiMovieSheet(
    movieTitle: String,
    viewModel: AiGuideViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val state by viewModel.describeState.collectAsState()

    
    LaunchedEffect(movieTitle) {
        viewModel.describeMovie(movieTitle)
    }

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.resetDescribe()
            onDismiss()
        },
        sheetState       = sheetState,
        containerColor   = Color(0xFF1A1A2E),
        scrimColor       = Color.Black.copy(alpha = 0.60f),
        shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .animateContentSize()
        ) {
            
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(2.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(20.dp))

            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFF9C6FFF),
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = stringResource(R.string.roulette_ai_insights),
                        fontFamily = SplineSans,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        letterSpacing = 3.sp,
                        color = Color(0xFF9C6FFF)
                    )
                    Text(
                        text = movieTitle,
                        fontFamily = SplineSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.08f)))

            Spacer(Modifier.height(20.dp))

            when (val s = state) {
                is AiGuideUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(color = Color(0xFF9C6FFF), strokeWidth = 2.dp)
                        Text(
                            stringResource(R.string.consulting_ai),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.45f)
                        )
                    }
                }

                is AiGuideUiState.Success -> {
                    Text(
                        text      = s.result,
                        style     = MaterialTheme.typography.bodyMedium,
                        color     = Color.White.copy(alpha = 0.88f),
                        lineHeight = 22.sp
                    )
                }

                is AiGuideUiState.Error -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            stringResource(R.string.error_load_ai_insights),
                            style = MaterialTheme.typography.bodyMedium,
                            color = NeonRed
                        )
                        Text(s.message, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.55f))
                        TextButton(onClick = { viewModel.describeMovie(movieTitle) }) {
                            Text(stringResource(R.string.btn_retry), color = Color(0xFF9C6FFF))
                        }
                    }
                }

                else -> {}
            }
        }
    }
}
