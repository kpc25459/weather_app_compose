package net.dev.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.LocalDateTime
import java.time.format.TextStyle
import java.util.*

val defaultTimeZone = TimeZone.of("Europe/Warsaw")

//TODO: zmienić nazwę i dać przykład z użycia
fun localDate(dt: LocalDateTime) = "${dt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${dt.dayOfMonth} ${
    dt.month.getDisplayName(
        TextStyle.FULL, Locale.getDefault()
    )
}"

@Composable
fun fromAqiIndex(aqi: Int): String {
    return when (aqi) {
        1 -> stringResource(R.string.very_good)
        2 -> stringResource(R.string.good)
        3 -> stringResource(R.string.moderate)
        4 -> stringResource(R.string.bad)
        5 -> stringResource(R.string.very_bad)
        else -> stringResource(R.string.unknown)
    }
}

fun imageFromAqi(aqi: Int): Int = when (aqi) {
    1 -> R.drawable.good_air
    2 -> R.drawable.good_air
    3 -> R.drawable.moderate_air
    4 -> R.drawable.bad_air
    5 -> R.drawable.very_bad_air
    else ->
        R.drawable.unknown
}

@Composable
fun dayOfWeek(dayOfWeek: DayOfWeek): String = when (dayOfWeek) {
    DayOfWeek.SUNDAY -> stringResource(R.string.sunday)
    DayOfWeek.MONDAY -> stringResource(R.string.monday)
    DayOfWeek.TUESDAY -> stringResource(R.string.tuesday)
    DayOfWeek.WEDNESDAY -> stringResource(R.string.wednesday)
    DayOfWeek.THURSDAY -> stringResource(R.string.thursday)
    DayOfWeek.FRIDAY -> stringResource(R.string.friday)
    DayOfWeek.SATURDAY -> stringResource(R.string.saturday)
}

fun toHumanFromDegrees(deg: Int): String {
    if (deg > 337.5) {
        return "N";
    } else if (deg > 292.5) {
        return "NW";
    } else if (deg > 247.5) {
        return "W";
    } else if (deg > 202.5) {
        return "SW";
    } else if (deg > 157.5) {
        return "S";
    } else if (deg > 122.5) {
        return "SE";
    } else if (deg > 67.5) {
        return "E";
    } else if (deg > 22.5) {
        return "NE";
    } else {
        return "";
    }
}




fun backgroundImageFromWeather(input: String): Int {
    return when (weatherCondition(input)) {
        WeatherCondition.Thunderstorm -> R.drawable.new_thunder
        WeatherCondition.Drizzle -> R.drawable.new_rain
        WeatherCondition.Rain -> R.drawable.new_rain
        WeatherCondition.Snow -> R.drawable.new_snow
        WeatherCondition.Fog -> R.drawable.new_fog
        WeatherCondition.Clear -> R.drawable.clear_new
        WeatherCondition.Clouds -> R.drawable.clouds_new
        WeatherCondition.Unknown -> R.drawable.unknown
    }
}

private fun weatherCondition(input: String) = when (input) {
    "Thunderstorm" -> WeatherCondition.Thunderstorm
    "Drizzle" -> WeatherCondition.Drizzle
    "Rain" -> WeatherCondition.Rain
    "Snow" -> WeatherCondition.Snow
    "Mist", "Fog", "Smoke", "Haze", "Dust", "Sand", "Ash", "Squall", "Tornado" -> WeatherCondition.Fog
    "Clear" -> WeatherCondition.Clear
    "Clouds" -> WeatherCondition.Clouds
    else -> WeatherCondition.Unknown
}

enum class WeatherCondition {
    Thunderstorm, Drizzle, Rain, Snow, Fog, Clear, Clouds, Unknown
}
