
package com.bnkc.library.custom.cardview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CardOffsetDecoration(private val mSpacing: Int) : RecyclerView.ItemDecoration() {
   private var mPlusSpacingTop: Int = 0

   fun setPlusSpacingTop(plusSpacingTop: Int) {
      this.mPlusSpacingTop = plusSpacingTop
   }

   override fun getItemOffsets(
      outRect: Rect,
      view: View,
      parent: RecyclerView,
      state: RecyclerView.State
   ) {
      super.getItemOffsets(outRect, view, parent, state)
      outRect.set(mSpacing, mSpacing + mPlusSpacingTop, mSpacing, mSpacing)
   }
}