package app.raybritton.smartmirror.ui.mirror

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import java.util.*

class SceneryView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    enum class Mode {
        NOTHING,
        RAIN,
        HEAVY_RAIN,
        SNOW,
        THUNDER
    }

    var mode: Mode = Mode.NOTHING
    var dayOfChristmas: Int? = null
    var shouldDraw = true

    private var animFrame = 0



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (shouldDraw) {
            dayOfChristmas?.let { drawChristmas(canvas, it) }
            when (mode) {
                Mode.RAIN -> drawRain(canvas)
                Mode.HEAVY_RAIN -> drawHeavyRain(canvas)
                Mode.SNOW -> drawSnow(canvas)
                Mode.THUNDER -> drawThunder(canvas)
                else -> {
                }
            }

            animFrame++
        }
        postInvalidate()
    }

    private fun drawRain(canvas: Canvas) {

    }

    private fun drawHeavyRain(canvas: Canvas) {

    }

    private fun drawSnow(canvas: Canvas) {

    }

    private fun drawThunder(canvas: Canvas) {

    }

    private fun drawChristmas(canvas: Canvas, day: Int) {

    }
}