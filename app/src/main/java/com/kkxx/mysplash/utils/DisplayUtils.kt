package com.kkxx.mysplash.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Build
import android.widget.TextView

/**
 * Display utils.
 *
 * An utils class that make operations of display easier.
 *
 */

class DisplayUtils(context: Context) {

    private var dpi = 0

    init {
        dpi = context.resources.displayMetrics.densityDpi
    }

    fun dpToPx(dp: Int): Float {
        return if (dpi == 0) {
            0f
        } else (dp * (dpi / 160.0)).toFloat()
    }

    companion object {

        fun getStatusBarHeight(r: Resources): Int {
            var result = 0
            val resourceId = r.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = r.getDimensionPixelSize(resourceId)
            }
            return result
        }

        fun setTypeface(c: Context, t: TextView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                t.typeface = Typeface.createFromAsset(c.assets, "fonts/Courier.ttf")
            }
        }

        fun abridgeNumber(num: Int): String {
            var num = num
            if (num < 1000) {
                return num.toString()
            } else {
                num = num / 100
                return (num / 10.0).toString() + "K"
            }
        }

        fun isTabletDevice(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        }

        fun isLandscape(context: Context): Boolean {
            return context.resources
                    .configuration
                    .orientation == Configuration.ORIENTATION_LANDSCAPE
        }
    }
}
