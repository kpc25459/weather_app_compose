package net.dev.weather

import kotlinx.datetime.LocalDateTime
import java.time.format.TextStyle
import java.util.*

fun localDate(dt: LocalDateTime) = "${dt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${dt.dayOfMonth} ${
    dt.month.getDisplayName(
        TextStyle.FULL, Locale.getDefault()
    )
}"