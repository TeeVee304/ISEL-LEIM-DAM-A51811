package dam_A51811.filmroulette.ui.screens.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.data.model.MovieList
import dam_A51811.filmroulette.data.model.Visibility
import dam_A51811.filmroulette.ui.screens.watchlist.WatchlistViewModel
import dam_A51811.filmroulette.ui.theme.SplineSans

/**
 * Displays the primary watchlist screen, showing all created watchlists and providing options
 * to create new watchlists or delete existing ones.
 *
 * @param viewModel The ViewModel responsible for providing and managing watchlist data.
 * @param modifier The modifier to be applied to the layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    modifier: Modifier = Modifier
) {
    val watchlists by viewModel.watchlists.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedWatchlist by remember { mutableStateOf<MovieList?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.desc_create_watchlist))
            }
        }
    ) { paddingValues ->
        if (watchlists.isEmpty()) {
            EmptyWatchlistState(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(watchlists, key = { it.id }) { watchlist ->
                    WatchlistCard(
                        watchlist = watchlist,
                        viewModel = viewModel,
                        onClick = { selectedWatchlist = watchlist },
                        onDeleteClick = { viewModel.deleteList(watchlist.id) }
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateWatchlistDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, visibility ->
                viewModel.createList(name, visibility)
                showCreateDialog = false
            }
        )
    }

    selectedWatchlist?.let { list ->
        WatchlistDetailSheet(
            watchlist = list,
            viewModel = viewModel,
            onDismiss = { selectedWatchlist = null }
        )
    }
}

/**
 * Displays an individual card for a single watchlist, including its name, visibility,
 * and the number of movies contained within it.
 *
 * @param watchlist The data model representing the watchlist.
 * @param viewModel The ViewModel used to retrieve the movies for the list.
 * @param onDeleteClick Callback invoked when the user chooses to delete this watchlist.
 */
@Composable
fun WatchlistCard(
    watchlist: MovieList,
    viewModel: WatchlistViewModel,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val movies by viewModel.getMoviesForList(watchlist.id).collectAsState(initial = emptyList())
    val coverImageUrl = movies.firstOrNull()?.imgUrl
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
    ) {
        if (coverImageUrl != null) {
            AsyncImage(
                model = coverImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Bookmarks,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                )
            }
        }

        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (watchlist.name == "Liked") stringResource(R.string.watchlist_liked_name) else watchlist.name,
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = when (watchlist.visibility) {
                        Visibility.PRIVATE -> Icons.Default.Lock
                        Visibility.FRIENDS_ONLY -> Icons.Default.People
                        Visibility.PUBLIC -> Icons.Default.Public
                    }
                    val visibilityText = when (watchlist.visibility) {
                        Visibility.PRIVATE -> stringResource(R.string.visibility_private)
                        Visibility.FRIENDS_ONLY -> stringResource(R.string.visibility_friends_only)
                        Visibility.PUBLIC -> stringResource(R.string.visibility_public)
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = visibilityText,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.watchlist_movies_count, movies.size),
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            if (watchlist.name != "Liked") {
                IconButton(onClick = { showDeleteConfirm = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.desc_delete_watchlist),
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(stringResource(R.string.watchlist_delete_title)) },
            text = { Text(stringResource(R.string.watchlist_delete_msg, watchlist.name)) },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.btn_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(stringResource(R.string.btn_cancel))
                }
            }
        )
    }
}

/**
 * Displays an empty state UI when there are no watchlists available.
 *
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun EmptyWatchlistState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Outlined.Bookmarks,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.18f),
            modifier = Modifier.size(80.dp),
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.no_watchlist_title),
            fontFamily = SplineSans,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.no_watchlist_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
        )
    }
}

/**
 * Displays a dialog allowing the user to create a new watchlist, specifying its name
 * and visibility level.
 *
 * @param onDismiss Callback invoked when the dialog is dismissed or canceled.
 * @param onCreate Callback invoked when the user confirms the creation, providing the name and visibility.
 */
@Composable
fun CreateWatchlistDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, visibility: Visibility) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedVisibility by remember { mutableStateOf(Visibility.PRIVATE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.title_new_watchlist), fontFamily = SplineSans, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.label_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(stringResource(R.string.label_visibility), fontWeight = FontWeight.SemiBold)
                
                Visibility.values().forEach { visibility ->
                    val visibilityLabel = when (visibility) {
                        Visibility.PRIVATE -> stringResource(R.string.visibility_private)
                        Visibility.FRIENDS_ONLY -> stringResource(R.string.visibility_friends_only)
                        Visibility.PUBLIC -> stringResource(R.string.visibility_public)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedVisibility = visibility }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedVisibility == visibility,
                            onClick = { selectedVisibility = visibility }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(visibilityLabel)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(name, selectedVisibility) },
                enabled = name.isNotBlank()
            ) {
                Text(stringResource(R.string.btn_create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
            }
        }
    )
}

/**
 * Displays a bottom sheet containing the list of movies within a selected watchlist.
 *
 * @param watchlist The watchlist to display movies for.
 * @param viewModel The ViewModel to provide the movies flow.
 * @param onDismiss Callback when the bottom sheet is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistDetailSheet(
    watchlist: MovieList,
    viewModel: WatchlistViewModel,
    onDismiss: () -> Unit
) {
    val movies by viewModel.getMoviesForList(watchlist.id).collectAsState(initial = emptyList())
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.watchlist_detail_title, 
                    if (watchlist.name == "Liked") stringResource(R.string.watchlist_liked_name) else watchlist.name
                ),
                fontFamily = SplineSans,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (movies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.watchlist_empty_movies),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movies, key = { it.id }) { movie ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = movie.imgUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp, 75.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = movie.title,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${movie.releaseDate?.take(4) ?: "N/A"} • ${movie.duration}m",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                            IconButton(onClick = { viewModel.removeMovieFromList(watchlist.id, movie.id) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.desc_remove_movie),
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
