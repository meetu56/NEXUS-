package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CosmicColorScheme = darkColorScheme(
  primary = CyberCyan,
  onPrimary = ObsidianDark,
  primaryContainer = CyberBlue,
  onPrimaryContainer = TextPrimary,
  secondary = CyberBlue,
  onSecondary = TextPrimary,
  secondaryContainer = CyberIndigo,
  onSecondaryContainer = TextPrimary,
  tertiary = CyberMagenta,
  onTertiary = ObsidianDark,
  background = ObsidianDark,
  onBackground = TextPrimary,
  surface = SpaceNavy,
  onSurface = TextPrimary,
  surfaceVariant = SpaceCard,
  onSurfaceVariant = TextSecondary,
  outline = GridLines,
  error = CyberOrange
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force sci-fi dark skin
  dynamicColor: Boolean = false, // Keep sci-fi static colors
  content: @Composable () -> Unit,
) {
  // Always use the immersive CosmicColorScheme for the personal operating system experience
  MaterialTheme(
    colorScheme = CosmicColorScheme,
    typography = Typography,
    content = content
  )
}
