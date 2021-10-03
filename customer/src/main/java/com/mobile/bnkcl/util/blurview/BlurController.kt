package com.mobile.bnkcl.util.blurview

import android.graphics.Canvas

interface BlurController : BlurViewFacade {
    /**
     * Draws blurred content on given canvas
     *
     * @return true if BlurView should proceed with drawing itself and its children
     */
    fun draw(canvas: Canvas?): Boolean
    fun drawRectRound(corner: Float, w: Int, h: Int, canvas: Canvas?): Boolean
    fun drawRectRound(corner: FloatArray?, w: Int, h: Int, canvas: Canvas?): Boolean

    /**
     * Must be used to notify Controller when BlurView's size has changed
     */
    fun updateBlurViewSize()

    /**
     * Frees allocated resources
     */
    fun destroy()

    companion object {
        const val DEFAULT_SCALE_FACTOR = 8f
        const val DEFAULT_BLUR_RADIUS = 16f
    }
}