package net.dev.weather

import kotlinx.datetime.LocalDateTime
import java.time.format.TextStyle
import java.util.*

//TODO: zmienić nazwę i dać przykład z użyciem
fun localDate(dt: LocalDateTime) = "${dt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${dt.dayOfMonth} ${
    dt.month.getDisplayName(
        TextStyle.FULL, Locale.getDefault()
    )
}"

//TODO: resursy
fun fromAqiIndex(aqi: Int): String {
    return when (aqi) {
        1 -> "Bardzo dobra"
        2 -> "Dobra"
        3 -> "Dostateczna"
        4 -> "Zła"
        5 -> "Bardzo zła"
        else -> "Nieznana"
    }
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

fun imageFromAqi(aqi: Int): Int = when (aqi) {
    1 -> R.drawable.good_air
    2 -> R.drawable.good_air
    3 -> R.drawable.moderate_air
    4 -> R.drawable.bad_air
    5 -> R.drawable.very_bad_air
    else ->
        R.drawable.unknown
}
