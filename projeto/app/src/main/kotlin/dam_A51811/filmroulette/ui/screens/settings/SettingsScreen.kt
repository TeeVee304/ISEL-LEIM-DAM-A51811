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
import androidx.compose.ui.res.stringResource
import dam_A51811.filmroulette.R
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

/**
 * Displays the settings screen, allowing the user to configure the app's appearance,
 * language preferences, and account actions such as logging out.
 *
 * @param viewModel The ViewModel responsible for managing settings state and preferences.
 * @param onLogout Callback invoked when the user selects the logout option.
 * @param onBack Callback invoked when the user applies settings and exits the screen.
 * @param modifier The modifier to be applied to the layout.
 */
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
        
        Text(
            text = stringResource(R.string.title_appearance),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeOptionRow(
            text = stringResource(R.string.setting_system_default),
            selected = themePreference == ThemePreference.SYSTEM,
            onClick = { viewModel.setThemePreference(ThemePreference.SYSTEM) }
        )
        ThemeOptionRow(
            text = stringResource(R.string.setting_light_mode),
            selected = themePreference == ThemePreference.LIGHT,
            onClick = { viewModel.setThemePreference(ThemePreference.LIGHT) }
        )
        ThemeOptionRow(
            text = stringResource(R.string.setting_dark_mode),
            selected = themePreference == ThemePreference.DARK,
            onClick = { viewModel.setThemePreference(ThemePreference.DARK) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        
        Text(
            text = stringResource(R.string.title_language),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeOptionRow(
            text = stringResource(R.string.setting_system_default),
            selected = languagePreference == LanguagePreference.SYSTEM,
            onClick = { viewModel.setLanguagePreference(LanguagePreference.SYSTEM) }
        )
        ThemeOptionRow(
            text = stringResource(R.string.lang_english),
            selected = languagePreference == LanguagePreference.EN,
            onClick = { viewModel.setLanguagePreference(LanguagePreference.EN) }
        )
        ThemeOptionRow(
            text = stringResource(R.string.lang_portuguese),
            selected = languagePreference == LanguagePreference.PT,
            onClick = { viewModel.setLanguagePreference(LanguagePreference.PT) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        
        Text(
            text = stringResource(R.string.title_account),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        TextButton(
            onClick = onLogout,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(stringResource(R.string.btn_log_out), color = NeonRed, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        
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
                text = stringResource(R.string.btn_apply_and_leave),
                fontFamily = SplineSans,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Displays a selectable row for configuring a specific settings option.
 *
 * @param text The display text for the option.
 * @param selected True if the option is currently selected, false otherwise.
 * @param onClick Callback invoked when the row is clicked.
 */
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
