package net.dev.weather.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import net.dev.weather.R

private val FiraSans = FontFamily(
    Font(R.font.fira_sans_light, FontWeight.Light),
    Font(R.font.fira_sans, FontWeight.Normal),
    Font(R.font.fira_sans_medium, FontWeight.Medium),
    Font(R.font.fira_sans_semibold, FontWeight.SemiBold),
)

// Set of Material typography styles to start with
val Typography = Typography(

    h1 = TextStyle(
        fontFamily = FiraSans,
        fontSize = 64.sp,
        letterSpacing = 0.sp,
        color = Color.White,
    ),
    h2 = TextStyle(
        fontFamily = FiraSans,
        fontSize = 40.sp,
        letterSpacing = 0.sp,
        color = Color.White,
    ),
    h3 = TextStyle(
        fontFamily = FiraSans,
        fontSize = 32.sp,
        letterSpacing = 0.sp,
        color = Color.White,
    ),

    body1 = TextStyle(
        fontFamily = FiraSans,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        color = Color(0xFF222D48),
    ),
    body2 = TextStyle(
        fontFamily = FiraSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        color = Color(0xFF475467),
    ),


    caption = TextStyle(
        fontFamily = FiraSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.sp,
        color = Color(0xFF475467)
    )

)
