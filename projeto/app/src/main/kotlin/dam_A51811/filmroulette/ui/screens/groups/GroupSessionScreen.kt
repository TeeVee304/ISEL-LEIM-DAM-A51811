package dam_A51811.filmroulette.ui.screens.groups

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.ui.components.GlassCard
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans

// ── Preview data ───────────────────────────────────────────────────────────
private data class Member(
    val name: String?,
    val nameResId: Int? = null,
    val statusResId: Int,
    val avatarUrl: String?,
    val isHost: Boolean = false,
)

private val previewMembers = listOf(
    Member(name = null, nameResId = R.string.group_session_member_you, statusResId = R.string.group_session_status_host, avatarUrl = null, isHost = true),
    Member(name = "Alex K.", statusResId = R.string.group_session_status_ready, avatarUrl = "https://randomuser.me/api/portraits/women/44.jpg"),
    Member(name = "Marcus", statusResId = R.string.group_session_status_filtering, avatarUrl = "https://randomuser.me/api/portraits/men/32.jpg"),
)

/**
 * Group Session screen — lets users host a synchronized filtering session
 * and invite friends via a session code or link.
 */
@Composable
fun GroupSessionScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF131313))
            .verticalScroll(rememberScrollState())
            .padding(top = 80.dp, bottom = 120.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        // ── Header ─────────────────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.group_session_title),
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White,
                )
                LiveBadge()
            }
            Text(
                text = stringResource(id = R.string.group_session_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.55f),
            )
        }

        // ── Invite Panel ───────────────────────────────────────────────────
        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.group_session_invite_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )

                // Session code row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0E0E0E))
                            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "RLT - 8821",
                                fontFamily = SplineSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                letterSpacing = 3.sp,
                                color = Color.White,
                            )
                            IconButton(onClick = { /* copy */ }, modifier = Modifier.size(24.dp)) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = stringResource(id = R.string.desc_copy_code),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { /* share */ },
                        modifier = Modifier.size(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = Color.White,
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    ) {
                        Icon(Icons.Default.Share, contentDescription = stringResource(id = R.string.desc_share))
                    }
                }

                // OR divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.10f))
                    Text(
                        text = stringResource(id = R.string.group_session_or_share_link),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.40f),
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.10f))
                }

                // Link button
                OutlinedButton(
                    onClick = { /* copy link */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                ) {
                    Icon(Icons.Default.Link, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.group_session_copy_link), style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        // ── Friends Grid ───────────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.group_session_joined_title),
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                )
                Text(
                    text = stringResource(id = R.string.group_session_active_members_format, previewMembers.size + 1),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.45f),
                )
            }

            // 2-column grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val gridItems: List<Member?> = previewMembers + listOf(null) // null = waiting slot
                gridItems.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        row.forEach { member ->
                            if (member != null) {
                                MemberCard(member = member, modifier = Modifier.weight(1f))
                            } else {
                                WaitingCard(modifier = Modifier.weight(1f))
                            }
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }

        // ── Combine Filters CTA ────────────────────────────────────────────
        Button(
            onClick = { /* combine */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = Color.White,
            ),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(Icons.Filled.AutoAwesome, null, modifier = Modifier.size(22.dp))
                    Text(
                        text = stringResource(id = R.string.group_session_combine),
                        fontFamily = SplineSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                }
                Text(
                    text = stringResource(id = R.string.group_session_syncing_preferences_format, previewMembers.size + 1),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.75f),
                )
            }
        }

        Text(
            text = stringResource(id = R.string.group_session_combine_subtitle),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.40f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// ── Sub-composables ────────────────────────────────────────────────────────

@Composable
private fun LiveBadge() {
    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dot_alpha",
    )

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.07f))
            .border(1.dp, Color.White.copy(alpha = 0.10f), CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha)),
        )
        Text(
            text = stringResource(id = R.string.group_session_live),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun MemberCard(member: Member, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .padding(14.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Avatar
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center,
            ) {
                val displayName = member.name ?: stringResource(id = member.nameResId ?: R.string.group_session_member_you)
                val displayStatus = stringResource(id = member.statusResId)
                if (member.avatarUrl != null) {
                    AsyncImage(
                        model = member.avatarUrl,
                        contentDescription = displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(
                                width = if (member.isHost) 2.dp else 1.dp,
                                color = if (member.isHost) NeonRed else Color.White.copy(alpha = 0.20f),
                                shape = CircleShape,
                            ),
                    )
                } else {
                    // Fallback avatar (initials placeholder)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(NeonRed.copy(alpha = 0.25f))
                            .border(2.dp, NeonRed, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = displayName.first().toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                    }
                }
            }

            Column {
                val displayName = member.name ?: stringResource(id = member.nameResId ?: R.string.group_session_member_you)
                val displayStatus = stringResource(id = member.statusResId)
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                )
                Text(
                    text = displayStatus,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (member.isHost) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.50f),
                )
            }
        }

        // Star watermark for host
        if (member.isHost) {
            Icon(
                Icons.Filled.Stars,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.07f),
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.TopEnd),
            )
        }
    }
}

@Composable
private fun WaitingCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(14.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.20f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Default.PersonAdd,
                    contentDescription = stringResource(id = R.string.desc_add_person),
                    tint = Color.White.copy(alpha = 0.40f),
                    modifier = Modifier.size(22.dp),
                )
            }
            Text(
                text = stringResource(id = R.string.group_session_waiting),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.35f),
            )
        }
    }
}
