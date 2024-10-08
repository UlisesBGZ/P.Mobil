package com.example.todolistapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

// Paleta de colores oscuros mejorada con un fondo menos negro
private val DarkColorPalette = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,  // Texto blanco puro en el modo oscuro
    secondary = ButtonBackgroundDark,  // Fondo más claro para botones
    onSecondary = ButtonTextDark,      // Texto blanco en los botones
    background = PrimaryDark,          // Fondo gris oscuro
    surface = SurfaceDark,
    onSurface = OnSurfaceDark   // Texto claro para mayor visibilidad en superficies oscuras
)

// Paleta de colores claros
private val LightColorPalette = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = Color.Black,    // Texto negro sobre el color primario claro
    secondary = SecondaryLight,
    onSecondary = Color.Black,
    background = BackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight  // Texto más oscuro para mayor contraste en modo claro
)

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun ToDoListAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = CustomTypography,
        shapes = Shapes,
        content = content
    )
}
