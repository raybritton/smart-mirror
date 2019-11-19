package app.raybritton.smartmirror.data

import kotlin.math.roundToInt

object Util {
    fun kmphToMph(speed: Int): Int {
        return (speed.toDouble() / 1.609).roundToInt()
    }
}