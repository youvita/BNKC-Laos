package com.bnkc.sourcemodule.ui

import android.content.Context
import android.widget.RelativeLayout
import android.graphics.RectF
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.bnkc.sourcemodule.R

class ShadowLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mRectF = RectF()
    private var mShadowColor = Color.TRANSPARENT
    private var mShadowRadius = 0f
    private var mShadowDx = 0f
    private var mShadowDy = 0f
    private var mShadowSide = ALL
    private var mShadowShape =
        SHAPE_RECTANGLE
    fun setShadowSide(t: String, r: String, b: String, l: String) {
        if (t == "T") {
            mShadowSide = TOP
        }
        if (r == "R") {
            mShadowSide = RIGHT
        }
        if (b == "B") {
            mShadowSide = BOTTOM
        }
        if (l == "L") {
            mShadowSide = LEFT
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val effect = mShadowRadius + dip2px(5f)
        var rectLeft = 0f
        var rectTop = 0f
        var rectRight = this.measuredWidth.toFloat()
        var rectBottom = this.measuredHeight.toFloat()
        var paddingLeft = 0
        var paddingTop = 0
        var paddingRight = 0
        var paddingBottom = 0
        this.width
        if (mShadowSide and LEFT == LEFT) {
            rectLeft = effect
            paddingLeft = effect.toInt()
        }
        if (mShadowSide and TOP == TOP) {
            rectTop = effect
            paddingTop = effect.toInt()
        }
        if (mShadowSide and RIGHT == RIGHT) {
            rectRight = this.measuredWidth - effect
            paddingRight = effect.toInt()
        }
        if (mShadowSide and BOTTOM == BOTTOM) {
            rectBottom = this.measuredHeight - effect
            paddingBottom = effect.toInt()
        }
        if (mShadowDy != 0.0f) {
            rectBottom -= mShadowDy
            paddingBottom += mShadowDy.toInt()
        }
        if (mShadowDx != 0.0f) {
            rectRight -= mShadowDx
            paddingRight += mShadowDx.toInt()
        }
        mRectF.left = rectLeft
        mRectF.top = rectTop
        mRectF.right = rectRight
        mRectF.bottom = rectBottom
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setUpShadowPaint()
        if (mShadowShape == SHAPE_RECTANGLE) {
            canvas.drawRect(mRectF, mPaint)
        } else if (mShadowShape == SHAPE_OVAL) {
            canvas.drawCircle(
                mRectF.centerX(),
                mRectF.centerY(),
                Math.min(mRectF.width(), mRectF.height()) / 2,
                mPaint
            )
        }
    }

    private fun init(attrs: AttributeSet?) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setWillNotDraw(false)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout)
        if (typedArray != null) {
            mShadowColor = typedArray.getColor(
                R.styleable.ShadowLayout_shadowColor,
                context.resources.getColor(android.R.color.black)
            )
            mShadowRadius =
                typedArray.getDimension(R.styleable.ShadowLayout_shadowRadius, dip2px(0f))
            mShadowDx = typedArray.getDimension(R.styleable.ShadowLayout_shadowDx, dip2px(0f))
            mShadowDy = typedArray.getDimension(R.styleable.ShadowLayout_shadowDy, dip2px(0f))
            mShadowSide = typedArray.getInt(R.styleable.ShadowLayout_shadowSide,
                ALL
            )
            mShadowShape = typedArray.getInt(R.styleable.ShadowLayout_shadowShape,
                SHAPE_RECTANGLE
            )
            typedArray.recycle()
        }
        setUpShadowPaint()
    }

    private fun setUpShadowPaint() {
        mPaint.reset()
        mPaint.isAntiAlias = true
        mPaint.color = Color.TRANSPARENT
        mPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor)
    }

    private fun dip2px(dpValue: Float): Float {
        val dm = context.resources.displayMetrics
        val scale = dm.density
        return dpValue * scale + 0.5f
    }

    companion object {
        const val ALL = 0x1111
        const val LEFT = 0x0001
        const val TOP = 0x0010
        const val RIGHT = 0x0100
        const val BOTTOM = 0x1000
        const val SHAPE_RECTANGLE = 0x0001
        const val SHAPE_OVAL = 0x0010
    }

    init {
        init(attrs)
    }
}