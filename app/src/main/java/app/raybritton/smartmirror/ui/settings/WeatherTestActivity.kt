package app.raybritton.smartmirror.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.data.models.WeatherDetail
import kotlinx.android.synthetic.main.activity_weather_test.*

class WeatherTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_test)

        window.decorView.keepScreenOn = true

        val imageSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics).toInt()

        makeWeathers()
            .forEach {
                val layout = LinearLayout(this)
                layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layout.orientation = LinearLayout.VERTICAL

                val icon = ImageView(this)
                icon.layoutParams = LinearLayout.LayoutParams(imageSize, imageSize)
                icon.setImageResource(it.second.getIcon())
                layout.addView(icon)

                val text = TextView(this)
                text.minLines = 3
                text.gravity = Gravity.CENTER
                text.setTextColor(getColor(R.color.mirror_text_primary))
                text.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                text.text = "${it.first}\n${it.second.getPriority()}"
                layout.addView(text)

                test_weather_icons.addView(layout)
        }
    }

    private fun makeWeathers(): List<Pair<String, WeatherDetail>> {
        return listOf(
            "light rain",
            "rain",
            "heavy rain",
            "thunderstorm",
            "sleet",
            "hail",
            "snow",
            "fog",
            "cloudy",
            "partly-cloudy-day",
            "partly-cloudy-night",
            "clear-day",
            "clear-night"
        ).mapIndexed { index, type ->
            val label = when (index) {
                in 0..2 -> "rain"
                else -> type
            }
            val precip = when (index) {
                in 0..2 -> index * 0.35
                in 4..6 -> 1.0
                else -> 0.0
            }
            val precipType = when (index) {
                in 0..2 -> "rain"
                in 4..5 -> "rain"
                6 -> "snow"
                else -> null
            }
            (type to WeatherDetail(
                System.currentTimeMillis(),
                type,
                label,
                precip,
                precipType,
                5.0,
                10.0
            ))
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, WeatherTestActivity::class.java))
        }
    }
}
