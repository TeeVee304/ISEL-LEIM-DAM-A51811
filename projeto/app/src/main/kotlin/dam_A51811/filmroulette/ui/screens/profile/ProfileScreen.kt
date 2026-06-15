package dam_A51811.filmroulette.ui.screens.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.data.model.User
import dam_A51811.filmroulette.ui.components.GlassCard
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A composable screen that displays the user's profile, manages friend requests,
 * and allows for account deletion or profile editing.
 *
 * @param viewModel The view model responsible for providing profile and friend request data.
 * @param modifier The modifier to apply to the screen's layout.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val friendsList by viewModel.friendsList.collectAsState()
    val receivedRequests by viewModel.receivedRequests.collectAsState()
    val sentRequests by viewModel.sentRequests.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    var emailInput by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    var newImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> newImageUri = uri }
    )

    // Messages are now cleared in onValueChange when the user types
    
    LaunchedEffect(successMessage) {
        if (successMessage == "success") {
            emailInput = ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(top = 80.dp, bottom = 120.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        
        currentUser?.let { user ->
            GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 24.dp) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .clickable { showEditProfileDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (user.avatarUrl != null) {
                            coil.compose.AsyncImage(
                                model = user.avatarUrl,
                                contentDescription = user.username,
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(3.dp, NeonRed, CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(NeonRed.copy(alpha = 0.20f))
                                    .border(3.dp, NeonRed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.username.firstOrNull()?.toString()?.uppercase() ?: "?",
                                    fontFamily = SplineSans,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 36.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { showEditProfileDialog = true }.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = user.username,
                                fontFamily = SplineSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                                contentDescription = stringResource(R.string.profile_title),
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f)
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.profile_member_since,
                                formatDate(user.registryDate)
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.40f)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                    OutlinedButton(
                        onClick = { viewModel.logout() },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed.copy(alpha = 0.60f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonRed)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.profile_sign_out),
                            fontFamily = SplineSans,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        
        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.friends_add_title),
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { 
                            emailInput = it
                            viewModel.clearMessages()
                        },
                        placeholder = { Text(stringResource(R.string.friends_add_hint), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonRed,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = NeonRed
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            if (emailInput.isNotBlank()) {
                                viewModel.sendFriendRequest(emailInput)
                            }
                        },
                        modifier = Modifier.size(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonRed,
                            contentColor = Color.White
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        enabled = emailInput.isNotBlank()
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(R.string.friends_add_button), modifier = Modifier.size(18.dp))
                    }
                }

                
                errorMessage?.let { error ->
                    val errorText = when (error) {
                        "self_error" -> stringResource(R.string.friends_error_self)
                        "not_found" -> stringResource(R.string.friends_error_not_found)
                        "already_friends" -> stringResource(R.string.friends_error_already_friends)
                        "already_requested" -> stringResource(R.string.friends_error_already_requested)
                        else -> error
                    }
                    Text(
                        text = errorText,
                        color = NeonRed,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }

                successMessage?.let { success ->
                    val successText = when (success) {
                        "success" -> stringResource(R.string.friends_success_sent)
                        else -> success
                    }
                    Text(
                        text = successText,
                        color = Color.Green,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        
        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = NeonRed
                        )
                    },
                    divider = {
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = "${stringResource(R.string.friends_req_received)} (${receivedRequests.size})",
                                fontFamily = SplineSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (selectedTab == 0) NeonRed else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = "${stringResource(R.string.friends_req_sent)} (${sentRequests.size})",
                                fontFamily = SplineSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (selectedTab == 1) NeonRed else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
                            )
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (selectedTab == 0) {
                        
                        if (receivedRequests.isEmpty()) {
                            Text(
                                text = stringResource(R.string.friends_req_empty_received),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                            )
                        } else {
                            receivedRequests.forEach { requester ->
                                RequestRow(
                                    user = requester,
                                    isReceived = true,
                                    onAccept = { viewModel.acceptFriendRequest(requester.id) },
                                    onDecline = { viewModel.declineFriendRequest(requester.id) }
                                )
                            }
                        }
                    } else {
                        
                        if (sentRequests.isEmpty()) {
                            Text(
                                text = stringResource(R.string.friends_req_empty_sent),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                            )
                        } else {
                            sentRequests.forEach { receiver ->
                                RequestRow(
                                    user = receiver,
                                    isReceived = false,
                                    onCancel = { viewModel.cancelFriendRequest(receiver.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        
        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.friends_title),
                        fontFamily = SplineSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.friends_total_count, friendsList.size),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
                    )
                }

                if (friendsList.isEmpty()) {
                    Text(
                        text = stringResource(R.string.friends_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                    )
                } else {
                    friendsList.forEach { friend ->
                        FriendItemRow(
                            friend = friend,
                            onRemove = { viewModel.removeFriend(friend.id) }
                        )
                    }
                }
            }
        }

        
        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = { showDeleteConfirmationDialog = true },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.profile_delete_account), modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.profile_delete_account),
                fontFamily = dam_A51811.filmroulette.ui.theme.SplineSans,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (showEditProfileDialog && currentUser != null) {
        var newUsername by remember { mutableStateOf(currentUser!!.username) }

        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { 
                Text(
                    text = stringResource(R.string.profile_title),
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.Bold
                ) 
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text(stringResource(R.string.profile_username)) }
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { 
                            photoPickerLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            ) 
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.profile_select_image))
                    }
                    if (newImageUri != null) {
                        Text(stringResource(R.string.msg_image_selected), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        viewModel.updateProfileWithImage(
                            newUsername = newUsername.takeIf { it.isNotBlank() },
                            newImageUri = newImageUri
                        )
                        showEditProfileDialog = false
                        newImageUri = null
                    }
                ) {
                    Text(stringResource(R.string.profile_save), color = NeonRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { 
                        showEditProfileDialog = false 
                        newImageUri = null
                    }
                ) {
                    Text(stringResource(R.string.profile_cancel), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }

    if (showDeleteConfirmationDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.profile_del_title),
                    fontFamily = dam_A51811.filmroulette.ui.theme.SplineSans,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.profile_del_msg),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        viewModel.deleteAccount()
                        showDeleteConfirmationDialog = false
                    }
                ) {
                    Text(stringResource(R.string.profile_del_yes), color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showDeleteConfirmationDialog = false }
                ) {
                    Text(stringResource(R.string.profile_cancel), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }
}

@Composable
private fun RequestRow(
    user: User,
    isReceived: Boolean,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.50f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (isReceived) {
                IconButton(
                    onClick = onAccept,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Green.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.Default.Check, contentDescription = stringResource(R.string.friends_accept), tint = Color.Green, modifier = Modifier.size(18.dp))
                }
                IconButton(
                    onClick = onDecline,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NeonRed.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.friends_decline), tint = NeonRed, modifier = Modifier.size(18.dp))
                }
            } else {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f))
                ) {
                    Text(
                        text = stringResource(R.string.friends_cancel),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun FriendItemRow(
    friend: User,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NeonRed.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = friend.username.firstOrNull()?.toString()?.uppercase() ?: "?",
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = NeonRed
                )
            }
            Column {
                Text(
                    text = friend.username,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = friend.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.50f)
                )
            }
        }

        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(NeonRed.copy(alpha = 0.05f))
        ) {
            Icon(Icons.Default.PersonRemove, contentDescription = stringResource(R.string.friends_remove), tint = NeonRed.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}
