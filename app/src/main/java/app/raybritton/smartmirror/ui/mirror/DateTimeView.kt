package app.raybritton.smartmirror.ui.mirror

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.util.AttributeSet
import android.widget.FrameLayout
import app.raybritton.smartmirror.R
import kotlinx.android.synthetic.main.view_datetime.view.*
import java.text.SimpleDateFormat
import java.util.*

class DateTimeView : FrameLayout {
    private val timeFormatter by lazy { DateFormat.getTimeFormat(context) }
    @SuppressLint("SimpleDateFormat")
    private val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy")

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.view_datetime, this)

        update()
    }

    private fun update() {
        val now = Date()

        datetime_time.text = timeFormatter.format(now)
        datetime_date.text = dateFormatter.format(now)

        postDelayed({update()}, THIRTY_SECONDS)
    }

    companion object {
        private const val THIRTY_SECONDS = 15 * 1000L
    }
}