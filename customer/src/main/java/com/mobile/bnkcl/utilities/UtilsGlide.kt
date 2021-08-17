package com.mobile.bnkcl.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mobile.bnkcl.R

object UtilsGlide {
    private const val radiosCorner = 8
    fun loadRounded(context: Context, uri: Uri?, into: ImageView?, view: View?) {
        val borderWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            radiosCorner.toFloat(),
            context.resources.displayMetrics
        ) as Int
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(borderWidth))
        requestOptions = requestOptions.dontAnimate()
        if (into != null) {
            Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.ic_check_on_ico)
                .error(R.drawable.ic_check_ico)
                .apply(requestOptions)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: Boolean
                    ): Boolean {
                        if (view != null) {
                            view.visibility = View.GONE
                        }
                        return false
                    }

                    override fun onResourceReady(
                        p0: Drawable?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: DataSource?,
                        p4: Boolean
                    ): Boolean {
                        if (view != null) {
                            view.visibility = View.GONE
                        }
                        return false
                    }
                })
                .into(into)
        }
    }

    fun loadRounded(context: Context, bitmap: Bitmap?, into: ImageView?, view: View?) {
        val borderWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            radiosCorner.toFloat(),
            context.resources.displayMetrics
        ) as Int
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(borderWidth))
        requestOptions = requestOptions.dontAnimate()
        if (into != null) {
            Glide.with(context)
                .load(bitmap)
                .placeholder(R.drawable.ic_check_on_ico)
                .error(R.drawable.ic_check_ico)
                .apply(requestOptions)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: Boolean
                    ): Boolean {
                        if (view != null) {
                            view.visibility = View.GONE
                        }
                        return false
                    }

                    override fun onResourceReady(
                        p0: Drawable?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: DataSource?,
                        p4: Boolean
                    ): Boolean {
                        if (view != null) {
                            view.visibility = View.GONE
                        }
                        return false
                    }
                })
                .into(into)
        }
    }

    fun loadRounded(context: Context, drawable: Int, into: ImageView?, view: View?) {
        val borderWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            radiosCorner.toFloat(),
            context.resources.displayMetrics
        ).toInt()
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(borderWidth))
        requestOptions = requestOptions.dontAnimate()
        if (into != null) {
            Glide.with(context)
                .load(drawable)
                .apply(requestOptions)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: Boolean
                    ): Boolean {
                        if (view != null) {
                            view.visibility = View.GONE
                        }
                        return false
                    }

                    override fun onResourceReady(
                        p0: Drawable?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: DataSource?,
                        p4: Boolean
                    ): Boolean {
                        if (view != null) {
                            view.visibility = View.GONE
                        }
                        return false
                    }
                })
                .into(into)
        }
    }

    fun loadCircle(context: Context, into: ImageView?, view: View?) {
//        val url = GlideUrl(
//            Conf.BASE_URL + ServiceName.sProfileImgUri, LazyHeaders.Builder()
//                .addHeader(
//                    "Authorization",
//                    "Bearer " + MemoryPreferenceDelegator.getInstance()
//                        .get(Constant.LoginInfo.LOGIN_TOKEN)
//                )
//                .build()
//        )

        val url: String = ""
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CircleCrop())
        requestOptions = requestOptions.dontAnimate()
        if (into != null) {
            Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(requestOptions)
                .error(AppCompatResources.getDrawable(context, R.drawable.ic_avatar_l))
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: Boolean
                    ): Boolean {
                        if (view != null) {
                            view.visibility = View.GONE
                        }
                        return false
                    }

                    override fun onResourceReady(
                        p0: Drawable?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: DataSource?,
                        p4: Boolean
                    ): Boolean {
                        if (view != null) {
                            view.visibility = View.GONE
                        }
                        return false
                    }
                })
                .into(into)
        }
    }

    fun loadCircle(context: Context, into: ImageView?, hideView: View?, showView: View?) {
//        val url = GlideUrl(
//            Conf.BASE_URL + ServiceName.sProfileImgUri, LazyHeaders.Builder()
//                .addHeader(
//                    "Authorization",
//                    "Bearer " + MemoryPreferenceDelegator.getInstance()
//                        .get(Constant.LoginInfo.LOGIN_TOKEN)
//                )
//                .build()
//        )
        val url: String = ""
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CircleCrop())
        requestOptions = requestOptions.dontAnimate()
        if (into != null) {
            Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(requestOptions)
                .error(AppCompatResources.getDrawable(context, R.drawable.ic_avatar_l))
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: Boolean
                    ): Boolean {
                        if (hideView != null) {
                            hideView.visibility = View.GONE
                        }
                        return false
                    }

                    override fun onResourceReady(
                        p0: Drawable?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: DataSource?,
                        p4: Boolean
                    ): Boolean {
                        if (hideView != null) {
                            hideView.visibility = View.GONE
                        }
                        return false
                    }
                })
                .into(into)
        }
    }

    fun loadCircle(context: Context?, uri: Uri?, into: ImageView?, view: View?) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CircleCrop())
        requestOptions = requestOptions.dontAnimate()
        if (context != null) {
            if (into != null) {
                Glide.with(context)
                    .load(uri)
                    .apply(requestOptions)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            p0: GlideException?,
                            p1: Any?,
                            p2: Target<Drawable>?,
                            p3: Boolean
                        ): Boolean {
                            if (view != null) {
                                view.visibility = View.GONE
                            }
                            return false
                        }

                        override fun onResourceReady(
                            p0: Drawable?,
                            p1: Any?,
                            p2: Target<Drawable>?,
                            p3: DataSource?,
                            p4: Boolean
                        ): Boolean {
                            if (view != null) {
                                view.visibility = View.GONE
                            }
                            return false
                        }
                    })
                    .into(into)
            }
        }
    }
}