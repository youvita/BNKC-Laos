package com.mobile.bnkcl.com.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.AppCompatEditText

class BnkEditText : AppCompatEditText, IKeyBoard {
    constructor(context: Context?) : super(context!!) {
        setKeyboardListener(this)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        setKeyboardListener(this)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        setKeyboardListener(this)
    }

    private fun setKeyboardListener(keyboardListener: IKeyBoard) {
        val parentView = rootView
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            private var alreadyOpen = false
            private val defaultKeyboardHeightDP = 100
            private val EstimatedKeyboardDP =
                defaultKeyboardHeightDP + 48
            private val rect = Rect()
            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    EstimatedKeyboardDP.toFloat(),
                    parentView.resources.displayMetrics
                )
                    .toInt()
                parentView.getWindowVisibleDisplayFrame(rect)
                val heightDiff = parentView.rootView.height - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight
                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...")
                    return
                }
                alreadyOpen = isShown
                keyboardListener.onKeyBoardHidden(isShown)
            }
        })
    }

    override fun onKeyBoardHidden(isHidden: Boolean) {
        if (!isHidden) {
            clearFocus()
        }
        this.isCursorVisible = isHidden
    }
}