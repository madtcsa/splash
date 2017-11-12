package com.kkxx.mysplash.view

import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.support.annotation.IntRange
import android.support.annotation.Size
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View

import com.kkxx.mysplash.R
import com.kkxx.mysplash.utils.DisplayUtils


/**
 * Freedom image view.
 *
 * This ImageView can measure size according to the preset.
 *
 */

class FreedomImageView : AppCompatImageView {

    private var topPaint: Paint? = null
    private var bottomPaint: Paint? = null

    // measure size according the width and height. (proportion)
    private var width = 1f
    private var height = 0.6f

    private var notFree = false // if set false, there will be no different between this view and a ImageView.
    private var coverMode = false // if set true, it means this ImageView is a cover in PhotoActivity.
    private var showShadow = false

    private var textPosition: Int = 0

    val size: FloatArray
        @Size(2)
        get() = floatArrayOf(width, height)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.initialize(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.initialize(context, attrs, defStyleAttr, 0)
    }

    private fun initialize(c: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        val a = c.obtainStyledAttributes(attrs, R.styleable.FreedomImageView, defStyleAttr, defStyleRes)
        this.notFree = a.getBoolean(R.styleable.FreedomImageView_fiv_not_free, false)
        this.coverMode = a.getBoolean(R.styleable.FreedomImageView_fiv_cover_mode, false)
        this.textPosition = a.getInt(R.styleable.FreedomImageView_fiv_shadow_position, POSITION_NONE)
        a.recycle()

        this.topPaint = Paint()
        this.bottomPaint = Paint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (width >= 0 && height >= 0) {
            if (notFree) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            } else {
                val size = getMeasureSize(
                        context,
                        View.MeasureSpec.getSize(widthMeasureSpec), width, height, coverMode)
                setMeasuredDimension(size[0], size[1])
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        setPaintStyle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (showShadow) {
            when (textPosition) {
                POSITION_TOP -> {
                    canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), topPaint!!)
                }
                POSITION_BOTTOM -> {
                    canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), bottomPaint!!)
                }
                POSITION_BOTH -> {
                    canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), topPaint!!)
                    canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), bottomPaint!!)
                }
            }
        }
    }

    fun setSize(w: Int, h: Int) {
        if (!notFree) {
            width = w.toFloat()
            height = h.toFloat()
            if (measuredWidth != 0) {
                requestLayout()
            }
        }
    }

    fun setShowShadow(show: Boolean) {
        this.showShadow = show
        invalidate()
    }

    private fun setPaintStyle() {
        topPaint!!.shader = LinearGradient(
                0f, 0f,
                0f, DisplayUtils(context).dpToPx(128).toInt().toFloat(),
                intArrayOf(Color.argb((255 * 0.25).toInt(), 0, 0, 0), Color.argb((255 * 0.1).toInt(), 0, 0, 0), Color.argb((255 * 0.03).toInt(), 0, 0, 0), Color.argb(0, 0, 0, 0)),
                null,
                Shader.TileMode.CLAMP)

        bottomPaint!!.shader = LinearGradient(
                0f, measuredHeight.toFloat(),
                0f, (measuredHeight - DisplayUtils(context).dpToPx(72).toInt()).toFloat(),
                intArrayOf(Color.argb((255 * 0.25).toInt(), 0, 0, 0), Color.argb((255 * 0.1).toInt(), 0, 0, 0), Color.argb((255 * 0.03).toInt(), 0, 0, 0), Color.argb(0, 0, 0, 0)), null,
                Shader.TileMode.CLAMP)
    }

    companion object {
        private val POSITION_NONE = 0
        private val POSITION_TOP = 1
        private val POSITION_BOTTOM = -1
        private val POSITION_BOTH = 2

        @Size(2)
        fun getMeasureSize(c: Context,
                           measureWidth: Int, w: Float, h: Float, coverMode: Boolean): IntArray {
            if (coverMode) {
                val screenWidth = c.resources.displayMetrics.widthPixels
                val screenHeight = c.resources.displayMetrics.heightPixels
                if (DisplayUtils.isLandscape(c)) {
                    return intArrayOf(measureWidth, screenHeight)
                }

                val limitHeight: Float
                if (c.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    limitHeight = screenHeight.toFloat()
                } else {
                    limitHeight = (screenHeight - c.resources.getDimensionPixelSize(R.dimen.photo_info_base_view_height)).toFloat()
                }

                if (1.0 * h / w * screenWidth <= limitHeight) {
                    return intArrayOf(
                            // (int) (limitHeight * w / h),
                            measureWidth, limitHeight.toInt())
                }
            }
            return intArrayOf(measureWidth, (measureWidth * h / w).toInt())
        }
    }

    /*
    @Size(4) // l, t, r, b.
    public static int[] getLayoutArea(Context c, int[] parentSizes, int[] childSizes) {
        int deltaWidth = childSizes[0] - parentSizes[0];
        int deltaHeight = childSizes[1]
                - (c.getResources()
                .getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE ?
                c.getResources().getDisplayMetrics().heightPixels : childSizes[1]);
        return new int[] {
                (int) (-deltaWidth / 2.0),
                (int) (-deltaHeight / 2.0),
                (int) (childSizes[0] - deltaWidth / 2.0),
                (int) (childSizes[1] - deltaHeight / 2.0)};
    }
*/
}
