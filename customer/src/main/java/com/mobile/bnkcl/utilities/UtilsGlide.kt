package com.mobile.bnkcl.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mobile.bnkcl.R
import java.io.ByteArrayOutputStream


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
        val url = GlideUrl(
            RunTimeDataStore.BaseUrl.value.plus(Constants.IMAGE_URL),
            LazyHeaders.Builder()
                .addHeader(
                    "Authorization",
                    "Bearer " + RunTimeDataStore.LoginToken.value
                )
                .build()
        )

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

                        /**
                         * convert drawable to bitmap
                         * compress and encodeToString
                         */
                        val sharedPrefer = CredentialSharedPrefer(context)
                        val bitmap: Bitmap = drawableToBitmap(p0!!)!!

                        val stream = ByteArrayOutputStream()
                        bitmap.compress(
                            Bitmap.CompressFormat.PNG,
                            100,
                            stream
                        )
                        val b: ByteArray = stream.toByteArray()

                        val encoded: String = Base64.encodeToString(b, Base64.DEFAULT)
                        sharedPrefer.putPrefer(Constants.IMAGE_BITMAP, encoded)

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
        val url = GlideUrl(
            RunTimeDataStore.BaseUrl.value.plus(Constants.IMAGE_URL),
            LazyHeaders.Builder()
                .addHeader(
                    "Authorization",
                    "Bearer " + RunTimeDataStore.LoginToken.value
                )
                .build()
        )
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
                        return false
                    }
                })
                .into(into)

        }
    }


    /**
     * convert drawable to bitmap
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
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