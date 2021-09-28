package com.mobile.bnkcl.utilities.blurview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View


class BlurKit {
    fun blur(src: Bitmap, radius: Int): Bitmap {
        val input: Allocation = Allocation.createFromBitmap(rs, src)
        val output: Allocation = Allocation.createTyped(rs, input.getType())
        val script: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(radius.toFloat())
        script.setInput(input)
        script.forEach(output)
        output.copyTo(src)
        return src
    }

    fun blur(src: View, radius: Int): Bitmap {
        val bitmap = getBitmapForView(src)
        return blur(bitmap, radius)
    }

    fun fastBlur(src: View, radius: Int, downscaleFactor: Float): Bitmap {
        val bitmap = getBitmapForView(src, downscaleFactor)
        return blur(bitmap, radius)
    }

    private fun getBitmapForView(src: View, downscaleFactor: Float): Bitmap {
        val bitmap = Bitmap.createBitmap(
            (src.getWidth() * downscaleFactor) as Int,
            (src.getHeight() * downscaleFactor) as Int,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val matrix = Matrix()
        matrix.preScale(downscaleFactor, downscaleFactor)
        canvas.setMatrix(matrix)
        src.draw(canvas)
        return bitmap
    }

    private fun getBitmapForView(src: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            src.getWidth(),
            src.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        src.draw(canvas)
        return bitmap
    }

    companion object {
        private const val FULL_SCALE = 1f
        private var instance: BlurKit? = null
        private var rs: RenderScript? = null
        fun init(context: Context) {
            if (instance != null) {
                return
            }
            instance = BlurKit()
            rs = RenderScript.create(context.getApplicationContext())
        }

        fun getInstance(): BlurKit? {
            if (instance == null) {
                throw RuntimeException("BlurKit not initialized!")
            }
            return instance
        }
    }
}