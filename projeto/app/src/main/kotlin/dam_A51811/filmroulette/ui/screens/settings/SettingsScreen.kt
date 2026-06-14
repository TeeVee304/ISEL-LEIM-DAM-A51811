package dam_A51811.filmroulette.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam_A51811.filmroulette.data.repository.LanguagePreference
import dam_A51811.filmroulette.data.repository.ThemePreference
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themePreference by viewModel.themePreference.collectAsState()
    val languagePreference by viewModel.languagePreference.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // ── Appearance ──────────────────────────────────────────────────────
        Text(
            text = "Appearance",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeOptionRow(
            text = "System Default",
            selected = themePreference == ThemePreference.SYSTEM,
            onClick = { viewModel.setThemePreference(ThemePreference.SYSTEM) }
        )
        ThemeOptionRow(
            text = "Light Mode",
            selected = themePreference == ThemePreference.LIGHT,
            onClick = { viewModel.setThemePreference(ThemePreference.LIGHT) }
        )
        ThemeOptionRow(
            text = "Dark Mode",
            selected = themePreference == ThemePreference.DARK,
            onClick = { viewModel.setThemePreference(ThemePreference.DARK) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ── Language ────────────────────────────────────────────────────────
        Text(
            text = "Language",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeOptionRow(
            text = "System Default",
            selected = languagePreference == LanguagePreference.SYSTEM,
            onClick = { viewModel.setLanguagePreference(LanguagePreference.SYSTEM) }
        )
        ThemeOptionRow(
            text = "English",
            selected = languagePreference == LanguagePreference.EN,
            onClick = { viewModel.setLanguagePreference(LanguagePreference.EN) }
        )
        ThemeOptionRow(
            text = "Português",
            selected = languagePreference == LanguagePreference.PT,
            onClick = { viewModel.setLanguagePreference(LanguagePreference.PT) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ── Account ─────────────────────────────────────────────────────────
        Text(
            text = "Account",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        TextButton(
            onClick = onLogout,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text("Log Out", color = NeonRed, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        // ── Apply button ─────────────────────────────────────────────────────
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonRed,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Apply & Leave",
                fontFamily = SplineSans,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ThemeOptionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
