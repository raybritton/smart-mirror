package app.raybritton.smartmirror.ui.mirror

import android.content.Context
import android.graphics.*
import android.os.Debug
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import app.raybritton.smartmirror.data.models.TwoDayWeather

class WeatherGraphView : View {
    private val todayDryColor = Color.parseColor("#989fa6")
    private val todayWetColor = Color.parseColor("#3394f5")
    private val yesterdayDryColor = Color.parseColor("#555a60")
    private val yesterdayWetColor = Color.parseColor("#244670")

    private val yesterdayPaint by lazy {
        Paint().apply {
            strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            strokeMiter
        }
    }
    private val todayPaint by lazy {
        Paint(yesterdayPaint).apply {
        }
    }

    private var pxPerHour = 0
    private var pxPerDegree = 0
    private var minTemp = 0
    private var maxTemp = 0
    private var yesterdayPath = Path()
    private var todayPath = Path()

    private val hourLabelPaint by lazy {
        TextPaint().apply {
            color = Color.WHITE
            typeface = Typeface.MONOSPACE
            style = Paint.Style.FILL_AND_STROKE
            textAlign = Paint.Align.CENTER
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14f,
                resources.displayMetrics
            )
        }
    }

    private val tempLabelPaint by lazy {
        TextPaint(hourLabelPaint).apply {
            textAlign = Paint.Align.LEFT
        }
    }

    private lateinit var weatherData: TwoDayWeather

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setOnClickListener {
            updateUi()
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        updateUi()
    }

    fun setData(data: TwoDayWeather) {
        this.weatherData = data
        updateUi()
    }

    private fun updateUi() {
        if (measuredWidth == 0 || measuredHeight == 0 || !this::weatherData.isInitialized) return

        minTemp = weatherData.minTemp()
        maxTemp = weatherData.maxTemp()

        pxPerHour = ((measuredWidth * 0.9) / 24).toInt()
        pxPerDegree = (measuredHeight * 0.9 / (maxTemp - minTemp).coerceAtLeast(1)).toInt()

        yesterdayPath.reset()
        yesterdayPath.moveTo(
            measuredWidth * 0.1f,
            (measuredHeight * 0.9f) * percentBetween(minTemp, maxTemp, weatherData.yesterday[0].temp)
        )
        val colors = IntArray(24)
        weatherData.yesterday.forEachIndexed { i, datum ->
            yesterdayPath.lineTo(
                measuredWidth * 0.1f + (i * pxPerHour),
                (measuredHeight * 0.9f) * percentBetween(minTemp, maxTemp, weatherData.yesterday[i].temp)
            )
            colors[i] =
                if (weatherData.yesterday[i].raining) yesterdayWetColor else yesterdayDryColor
        }
        yesterdayPaint.shader = LinearGradient(
            0f,
            0f,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            colors,
            null,
            Shader.TileMode.CLAMP
        )

        todayPath.reset()
        todayPath.moveTo(
            measuredWidth * 0.1f,
            (measuredHeight * 0.9f) * percentBetween(minTemp, maxTemp, weatherData.today[0].temp)
        )
        weatherData.today.forEachIndexed { i, datum ->
            todayPath.lineTo(
                measuredWidth * 0.1f + (i * pxPerHour),
                (measuredHeight * 0.9f) * percentBetween(minTemp, maxTemp, weatherData.today[i].temp)
            )
            colors[i] =
                if (weatherData.today[i].raining) todayWetColor else todayDryColor
        }
        todayPaint.shader = LinearGradient(
            0f,
            0f,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            colors,
            null,
            Shader.TileMode.CLAMP
        )
        invalidate()
    }

    private fun percentBetween(min: Int, max: Int, value: Int): Float {
        return (max.toFloat() - value.toFloat()) / (max.toFloat() - min.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        (0..23).forEachIndexed { i, hour ->
            if (hour % 3 == 0) {
                canvas.drawText(
                    hour.toString().padStart(2, ' '),
                    ((measuredWidth * 0.1f) + (i * pxPerHour)),
                    (measuredHeight * 0.95f),
                    hourLabelPaint
                )
            }
        }
        canvas.drawText(maxTemp.toString(), 0f, measuredHeight * 0.08f, tempLabelPaint)
        canvas.drawText(minTemp.toString(), 0f, measuredHeight * 0.92f, tempLabelPaint)

        canvas.drawPath(yesterdayPath, yesterdayPaint)
        canvas.drawPath(todayPath, todayPaint)
    }
}