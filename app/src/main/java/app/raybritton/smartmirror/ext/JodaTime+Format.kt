package app.raybritton.smartmirror.ext

import org.joda.time.DateTime

fun DateTime.isToday(): Boolean {
    val today = DateTime.now()
    return (today.dayOfYear == dayOfYear &&
            today.year == year)
}


fun DateTime.isYesterday(): Boolean {
    val today = DateTime.now().minusDays(1)
    return (today.dayOfYear == dayOfYear &&
            today.year == year)
}