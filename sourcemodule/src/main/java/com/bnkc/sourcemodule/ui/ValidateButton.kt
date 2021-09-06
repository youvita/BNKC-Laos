package com.bnkc.sourcemodule.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.databinding.CommValidateButtonBinding
import java.util.*
import kotlin.collections.ArrayList

class ValidateButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context,attrs, defStyleAttr) {

    var binding : CommValidateButtonBinding =
        CommValidateButtonBinding.inflate(LayoutInflater.from(context), this, true);

    var status = false
    var textAllCaps = false
    var text = ""
    var background : Int = 0
    var textColor : Int = 0

    var _textAllCaps = MutableLiveData<Boolean>(false)
    var value= MutableLiveData<String>("")
    var active = MutableLiveData<Boolean>(false)


    init {
        binding.model=this
        value.observeForever {
            binding.tvCheckButton.text = it
        }
        active.observeForever {
            Log.d("nng", "active set from java :: $it")
            if (it) {
                setActiveButton(true)
            } else {
                if (background != 0 || textColor != 0) {
                    setNormalButton(background, textColor)
                } else {
                    setActiveButton(false)
                }
            }
        }
        _textAllCaps.observeForever {
            textAllCaps = it
        }

        getAttributes(attrs)
    }

    fun setActiveButton(active : Boolean){
        if (active) {
            binding.tvCheckButton.setTextColor(ContextCompat.getColor(context, R.color.color_ffffff))
            binding.llCheckButton.background = ContextCompat.getDrawable(context, R.drawable.selector_d7191f_8b0304)
        } else {
            binding.tvCheckButton.setTextColor(ContextCompat.getColor(context, R.color.color_90a4ae))
            binding.llCheckButton.background = ContextCompat.getDrawable(context, R.drawable.round_solid_e1e5ec_8)
        }
    }

    fun getAttributes(attrs: AttributeSet?){
        val a : TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ValidateButton)
        for (i in 0 until a.indexCount){
            when(val attr = a.getIndex(i)){
                R.styleable.ValidateButton_buttonText -> text = a.getString(attr).toString()
                R.styleable.ValidateButton_is_active -> status = a.getBoolean(attr, false)
                R.styleable.ValidateButton_textAllCaps -> textAllCaps = a.getBoolean(attr, false)
                R.styleable.ValidateButton_buttonBackground -> background = a.getResourceId(attr, 0)
                R.styleable.ValidateButton_buttonTextColor -> textColor = a.getInt(attr,0)
            }
        }
        a.recycle()
    }

    fun setNormalButton(background: Int, textColor: Int) {
        binding.llCheckButton.setBackgroundResource(background)
        binding.tvCheckButton.setTextColor(textColor)
    }

    fun setFailButton() {
        binding.llCheckButton.background =
            ContextCompat.getDrawable(context, R.drawable.selector_d7191f_ffffee)
        binding.tvCheckButton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
    }

    fun setSuccessButton() {
        binding.tvCheckButton.setTextColor(ContextCompat.getColor(context, R.color.color_ffffff))
        binding.llCheckButton.background = ContextCompat.getDrawable(context, R.drawable.selector_d7191f_8b0304)
    }

    fun setTextAllCap(textAllCap: Boolean) {
        _textAllCaps.postValue(textAllCap)
    }


    fun isActive(): Boolean {
        return status
    }

    fun setLabelButton(text: String) {
        binding.tvCheckButton.text = if (textAllCaps) text.toUpperCase() else text
    }

    fun setCheckButtonBackGround(isEnable: Boolean) {
        if (isEnable) {
            binding.llCheckButton.background =
                ContextCompat.getDrawable(context, R.drawable.selector_d7191f_8b0304)
            setOnClickListener(null)
        } else {
            binding.llCheckButton.background =
                ContextCompat.getDrawable(context, R.drawable.round_solid_e1e5ec_8)
        }
    }

    fun setButtonBackGround(bg: Int) {
        binding.llCheckButton.setBackgroundResource(bg)
    }

    open fun setCheckButtonTextColor(isEnable: Boolean) {
        if (isEnable) {
            binding.tvCheckButton.setTextColor(ContextCompat.getColor(context, R.color.color_ffffff))
        } else {
            binding.tvCheckButton.setTextColor(ContextCompat.getColor(context, R.color.color_90a4ae))
        }
    }

    fun isEnableInUpdate(isUpdate: Boolean, vararg params: String) { //a,b,c
        val arrayList = ArrayList<String>()
        if (isUpdate) {
            val updateList = ArrayList<String>()

            for (param in params) {
                updateList.add(param)
                if (param != "") {
                    arrayList.add("T")
                } else {
                    arrayList.add("F")
                }
            }
            for (i in arrayList.indices) {
                if (arrayList[i] == "T") {
                    setActive(true)
                }
                if (arrayList[i] == "F") {
                    setActive(false)
                    return
                }
            }
        } else {
            for (param in params) {
                if (param != "") {
                    arrayList.add("T")
                } else {
                    arrayList.add("F")
                }
            }
            for (i in arrayList.indices) {
                if (arrayList[i] == "T") {
                    setActive(true)
                }
                if (arrayList[i] == "F") {
                    setActive(false)
                    return
                }
            }
        }
    }

    fun isEnable(vararg params: String) { //a,b,c
        val arrayList = ArrayList<String>()
        for (param in params) {
            if (param != "") {
                arrayList.add("T")
            } else {
                arrayList.add("F")
            }
        }
        for (i in arrayList.indices) {
            if (arrayList[i] == "T") {
                setActive(true)
            }
            if (arrayList[i] == "F") {
                setActive(false)
                return
            }
        }
    }

    fun setActive(isActive: Boolean) {
        active.postValue(isActive)
    }

    fun setValue(text : String){
        tag = text
        value.postValue(text)
    }
}