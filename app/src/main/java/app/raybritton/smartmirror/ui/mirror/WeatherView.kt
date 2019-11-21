package app.raybritton.smartmirror.ui.mirror

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.data.Util
import app.raybritton.smartmirror.data.WeatherManager
import kotlinx.android.synthetic.main.view_weather.view.*

class WeatherView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.view_weather, this)
    }

}