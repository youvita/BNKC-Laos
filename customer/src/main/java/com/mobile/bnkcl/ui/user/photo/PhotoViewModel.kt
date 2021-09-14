package com.mobile.bnkcl.ui.user.photo

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import com.bnkc.sourcemodule.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Useful management photo view
 */
@HiltViewModel
class PhotoViewModel @Inject constructor() : BaseViewModel() {

    /**
     * setting option camera & gallery
     */
    enum class Setting {
        Camera, Gallery
    }

    /**
     * observe photo setting, camera & gallery
     */
    val photoSettingLiveData: MutableLiveData<Setting> = MutableLiveData()

    /**
     * index photo start from 1..N
     */
    private var mIndexPhotos = 0

    /**
     * get photo maximum width of parent view
     * size 4 is margin left = 2 and right = 2
     */
    fun getPhotoMaxWidth(view: View): Int {
        return (view.width - getDP(4)) / 3
    }

    fun getPhotoMaxWidth(width: Int): Int {
        return (width - getDP(4)) / 3
    }

    /**
     * get single photo maximum width of parent view
     * To find max width = parent width - (padding left, right)
     */
    fun getSinglePhotoMaxWidth(view: View): Int {
        return view.width - getDP(32)
    }

    /**
     * remove index photo from photo container
     */
    fun removeIndexPhoto() {
        mIndexPhotos--
    }

    fun clearIndexPhoto() {
        mIndexPhotos = 0
    }

    /**
     * get photo layout params and set margin to view
     * set margin depending on the max photos.
     * the last view will not set margin right.
     */
    fun getLayoutParamView(photo_width: Int, max_photos: Int): LinearLayout.LayoutParams {
        mIndexPhotos++
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(photo_width, ViewGroup.LayoutParams.MATCH_PARENT)
        if (mIndexPhotos < max_photos) {
            layoutParams.setMargins(0, 0, getDP(2), 0)
        }
        return layoutParams
    }

    /**
     * camera binding click
     */
    fun onCamera() {
        photoSettingLiveData.value = Setting.Camera
    }

    /**
     * gallery binding click
     */
    fun onGallery() {
        photoSettingLiveData.value = Setting.Gallery
    }

    private fun getDP(s: Int): Int {
        var size = s
        size = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            size.toFloat(),
            context.resources.displayMetrics
        )
            .toInt()
        return size
    }
}