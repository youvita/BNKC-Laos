package com.mobile.bnkcl.utilities.blurview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView


class RoundedImageView : androidx.appcompat.widget.AppCompatImageView {
    var cornerRadius = 0f
    private var rectF: RectF
    private var porterDuffXfermode: PorterDuffXfermode

    constructor(context: Context?) : super(context!!, null) {
        rectF = RectF()
        porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    constructor(context: Context?, attributes: AttributeSet?) : super(context!!, attributes) {
        rectF = RectF()
        porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    protected override fun onDraw(canvas: Canvas) {
        val myDrawable: Drawable = getDrawable()
        if (myDrawable != null && myDrawable is BitmapDrawable && cornerRadius > 0) {
            rectF.set(myDrawable.getBounds())
            val prevCount: Int = canvas.saveLayer(rectF, null, Canvas.ALL_SAVE_FLAG)
            getImageMatrix().mapRect(rectF)
            val paint: Paint = myDrawable.paint
            paint.setAntiAlias(true)
            paint.setColor(DEFAULT_COLOR)
            val prevMode: Xfermode = paint.getXfermode()
            canvas.drawARGB(DEFAULT_RGB, DEFAULT_RGB, DEFAULT_RGB, DEFAULT_RGB)
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
            paint.setXfermode(porterDuffXfermode)
            super.onDraw(canvas)
            paint.setXfermode(prevMode)
            canvas.restoreToCount(prevCount)
        } else {
            super.onDraw(canvas)
        }
    }

    companion object {
        const val DEFAULT_COLOR = -0x1000000
        const val DEFAULT_RGB = 0
    }
}