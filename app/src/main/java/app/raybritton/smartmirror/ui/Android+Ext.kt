package app.raybritton.smartmirror.ui

import android.widget.TextView
import androidx.annotation.ColorRes

fun TextView.setTextColorRes(@ColorRes colorId: Int) {
    setTextColor(context.getColor(colorId))
}