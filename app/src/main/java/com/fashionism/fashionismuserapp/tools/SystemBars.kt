package com.fashionism.fashionismuserapp.tools

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fashionism.fashionismuserapp.R

/**
 * Android 15+ always draws apps behind the status and navigation bars. Applied to every activity:
 * turn edge-to-edge on for all versions, then pad the content back out of the bars (and the keyboard)
 * and paint the status bar area, so every screen looks the same as before on every Android version.
 */
object EdgeToEdgeCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val transparent = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT) // dark icons, as before
        (activity as? ComponentActivity)?.enableEdgeToEdge(transparent, transparent)
    }

    // Runs after onCreate, so the screen's content view exists
    override fun onActivityStarted(activity: Activity) {
        val content = activity.findViewById<View>(android.R.id.content)
        if (content.getTag(R.id.system_bars_applied) != null) return
        content.setTag(R.id.system_bars_applied, true)
        ViewCompat.setOnApplyWindowInsetsListener(content) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            val keyboard = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(bars.left, bars.top, bars.right, maxOf(bars.bottom, keyboard.bottom))
            val statusBarColor = view.getTag(R.id.status_bar_color) as? Int ?: Color.WHITE
            view.background = LayerDrawable(arrayOf(ColorDrawable(Color.WHITE), ColorDrawable(statusBarColor))).apply {
                setLayerGravity(1, Gravity.TOP or Gravity.FILL_HORIZONTAL)
                setLayerHeight(1, bars.top)
            }
            WindowInsetsCompat.CONSUMED
        }
        ViewCompat.requestApplyInsets(content)
    }

    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}

/** Colors the strip behind the status bar (the window's own status bar color is ignored edge-to-edge). */
fun Activity.setStatusBarBackground(@ColorInt color: Int) {
    val content = findViewById<View>(android.R.id.content)
    content.setTag(R.id.status_bar_color, color)
    ViewCompat.requestApplyInsets(content)
}
