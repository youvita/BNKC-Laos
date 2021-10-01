package com.mobile.bnkcl.utilities.blurview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import com.mobile.bnkcl.R
import com.mobile.bnkcl.utilities.blurview.BlockingBlurController.Companion.TRANSPARENT


class BlurView : FrameLayout {
    var blurController: BlurController = NoOpController()

    @ColorInt
    private var overlayColor = 0
    private var blurRadius = 0f
    private var corner = 0f
    private var cornerTopLeft = 0f
    private var cornerTopRight = 0f
    private var cornerBottomLeft = 0f
    private var cornerBottomRight = 0f
    private val corners = FloatArray(8)

    constructor(context: Context?) : super(context!!) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BlurView, defStyleAttr, 0)
        overlayColor = a.getColor(R.styleable.BlurView_blurOverlayColor, TRANSPARENT)
        corner = a.getDimension(R.styleable.BlurView_corner, 0f)
        blurRadius = a.getFloat(R.styleable.BlurView_blurRadius, 0f)
        cornerTopLeft = a.getDimension(R.styleable.BlurView_cornerTopLeft, 0f)
        cornerTopRight = a.getDimension(R.styleable.BlurView_cornerTopRight, 0f)
        cornerBottomLeft = a.getDimension(R.styleable.BlurView_cornerBottomLeft, 0f)
        cornerBottomRight = a.getDimension(R.styleable.BlurView_cornerBottomRight, 0f)
        for (i in corners.indices) {
            when {
                i < 2 -> {
                    corners[i] = cornerTopLeft
                }
                i < 4 -> {
                    corners[i] = cornerTopRight
                }
                i < 6 -> {
                    corners[i] = cornerBottomRight
                }
                else -> {
                    corners[i] = cornerBottomLeft
                }
            }
        }
//        doBlurRadius()
        a.recycle()
    }

    override fun draw(canvas: Canvas?) {
        var shouldDraw = false
        for (c in corners) {
            if (c != 0f) {
                shouldDraw = blurController.drawRectRound(corners, this.width, this.height, canvas)
                break
            } else {
                shouldDraw = if (corner == 0f) {
                    blurController.draw(canvas)
                } else {
                    blurController.drawRectRound(corner, this.width, this.height, canvas)
                }
            }
        }
        if (shouldDraw) {
            Log.d(">>>>>", "Start drawing BlurView...")
            super.draw(canvas)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blurController.updateBlurViewSize()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        blurController.setBlurAutoUpdate(false)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!isHardwareAccelerated) {
            Log.e(TAG, "BlurView can't be used in not hardware-accelerated window!")
        } else {
            blurController.setBlurAutoUpdate(true)
        }
    }

    /**
     * @param rootView root to start blur from.
     * Can be Activity's root content layout (android.R.id.content)
     * or (preferably) some of your layouts. The lower amount of Views are in the root, the better for performance.
     * @return [BlurView] to setup needed params.
     */
    fun setupWith(@NonNull rootView: ViewGroup?): BlurViewFacade {
        val blurController: BlurController = BlockingBlurController(
            this,
            rootView!!, overlayColor
        )
        this.blurController.destroy()
        this.blurController = blurController
        return blurController
    }
    // Setters duplicated to be able to conveniently change these settings outside of setupWith chain
    /**
     * @see BlurViewFacade.setBlurRadius
     */
    private fun doBlurRadius(): BlurViewFacade? {
        return blurController.setBlurRadius(blurRadius)
    }

    fun setBlurRadius(radius: Float) {
        blurRadius = radius
        doBlurRadius()
    }

    /**
     * @see BlurViewFacade.setOverlayColor
     */
    fun setOverlayColor(@ColorInt overlayColor: Int): BlurViewFacade? {
        this.overlayColor = overlayColor
        return blurController.setOverlayColor(overlayColor)
    }

    /**
     * @see BlurViewFacade.setBlurAutoUpdate
     */
    fun setBlurAutoUpdate(enabled: Boolean): BlurViewFacade? {
        return blurController.setBlurAutoUpdate(enabled)
    }

    /**
     * @see BlurViewFacade.setBlurEnabled
     */
    fun setBlurEnabled(enabled: Boolean): BlurViewFacade? {
        return blurController.setBlurEnabled(enabled)
    }

    companion object {
        private val TAG = BlurView::class.java.simpleName
    }
}