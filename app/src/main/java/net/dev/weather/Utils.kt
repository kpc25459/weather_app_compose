package net.dev.weather

import kotlinx.datetime.LocalDateTime
import java.time.format.TextStyle
import java.util.*

fun localDate(dt: LocalDateTime) = "${dt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${dt.dayOfMonth} ${
    dt.month.getDisplayName(
        TextStyle.FULL, Locale.getDefault()
    )
}"

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