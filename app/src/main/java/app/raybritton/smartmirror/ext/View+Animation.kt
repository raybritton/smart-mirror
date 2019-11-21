package app.raybritton.smartmirror.ext

import android.animation.ObjectAnimator
import android.view.View

fun View.createPulseAnimation(): ObjectAnimator {
    val animation = android.animation.ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    animation.repeatMode = android.animation.ValueAnimator.REVERSE
    animation.repeatCount = android.animation.ValueAnimator.INFINITE
    animation.duration = 1000L
    return animation
}