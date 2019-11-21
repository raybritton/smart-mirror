package app.raybritton.smartmirror.ui.mirror

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import app.raybritton.smartmirror.BuildConfig

class PixelRefreshView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun refresh() {
        if (BuildConfig.DEBUG) return
        slowRefresh {
            slowRefresh {
                this.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    private fun slowRefresh(onComplete: () -> Unit) {
        setBackgroundColor(Color.BLACK)
        postDelayed({ this.setBackgroundColor(Color.RED) },
            SLOW_REFRESH_DURATION
        )
        postDelayed({ this.setBackgroundColor(Color.GREEN) }, SLOW_REFRESH_DURATION * 2)
        postDelayed({ this.setBackgroundColor(Color.BLUE) }, SLOW_REFRESH_DURATION * 3)
        postDelayed({ this.setBackgroundColor(Color.WHITE) }, SLOW_REFRESH_DURATION * 4)
        postDelayed(onComplete, SLOW_REFRESH_DURATION * 5)
    }

    companion object {
        private const val SLOW_REFRESH_DURATION = 500L
    }
}