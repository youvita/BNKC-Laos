package com.mobile.bnkcl.utilities.blurview

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.renderscript.ScriptIntrinsicBlur
import androidx.annotation.NonNull
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript

class SupportRenderScriptBlur(context: Context?) : BlurAlgorithm {
    private val renderScript: RenderScript = RenderScript.create(context)
    private val blurScript: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(
        renderScript, Element.U8_4(
            renderScript
        )
    )
    private var outAllocation: Allocation? = null
    private var lastBitmapWidth = -1
    private var lastBitmapHeight = -1
    private fun canReuseAllocation(bitmap: Bitmap?): Boolean {
        return bitmap!!.height == lastBitmapHeight && bitmap.width == lastBitmapWidth
    }

    /**
     * @param bitmap     bitmap to blur
     * @param blurRadius blur radius (1..25)
     * @return blurred bitmap
     */
    override fun blur(bitmap: Bitmap?, blurRadius: Float): Bitmap? {
        Log.d(
            ">>>>>",
            "blur ::: $blurRadius"
        )
        //Allocation will use the same backing array of pixels as bitmap if created with USAGE_SHARED flag
        val inAllocation: Allocation = Allocation.createFromBitmap(renderScript, bitmap)
        if (!canReuseAllocation(bitmap)) {
            if (outAllocation != null) {
                outAllocation?.destroy()
            }
            outAllocation = Allocation.createTyped(renderScript, inAllocation.type)
            lastBitmapWidth = bitmap!!.width
            lastBitmapHeight = bitmap.height
        }
        blurScript.setRadius(blurRadius)
        blurScript.setInput(inAllocation)
        //do not use inAllocation in forEach. it will cause visual artifacts on blurred Bitmap
        blurScript.forEach(outAllocation)
        outAllocation?.copyTo(bitmap)
        inAllocation.destroy()
        return bitmap
    }

    override fun destroy() {
        blurScript.destroy()
        renderScript.destroy()
        if (outAllocation != null) {
            outAllocation?.destroy()
        }
    }

    override fun canModifyBitmap(): Boolean {
        return true
    }

    @get:NonNull
    override val supportedBitmapConfig: Bitmap.Config
        get() = Bitmap.Config.ARGB_8888

}