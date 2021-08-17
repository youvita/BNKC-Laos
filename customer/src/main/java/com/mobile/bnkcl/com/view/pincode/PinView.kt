package com.mobile.bnkcl.com.view.pincode

import android.content.Context
import android.content.res.TypedArray
import android.database.DatabaseUtils
import android.graphics.Color
import android.media.Image
import android.os.Handler
import android.os.Vibrator
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import com.mobile.bnkcl.R
import com.mobile.bnkcl.utilities.UtilsSize
import kotlin.properties.Delegates


class PinView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

     var mPinView : PinCodeView? = null
     var mPinMessage: TextView? = null
     var mResetPin:TextView? = null
     var title:TextView? = null
     val reEnterPassword : Boolean? = false
     var mCodeValidation = ""
     var mCode = ""


    var setOnCompletedListener: (pinCode: String) -> Unit = {}
    // function type variable that is invoked when pin code is completed

    var setOnErrorListener: (msg: Int) -> Unit = {}

    var setOnClosePinListener: () -> Unit = {}

    var setOnPinKeyClickListener : (keyPressed : String) -> Unit = {}
    // function type variable that is invoked when a pin key clicked

    private lateinit var attributes: TypedArray

    //Delegate initialization to listen to currentPinCode changes
    private var currentPinCode by Delegates.observable("") { _, _, newValue ->
        val drawable = DrawableCompat.wrap(resources.getDrawable(R.drawable.oval, null))
        DrawableCompat.setTint(
            drawable, attributes.getColor(
                R.styleable.PinView_dotProgressColor,
                Color.BLACK
            )
        )
        if (newValue.length >= 0) initPinCodeProgress()

    }


    init {
        val view = inflate(context, R.layout.pin_view, this)

        attributes = context.obtainStyledAttributes(attrs, R.styleable.PinView)

//        .text = attributes.getString(R.styleable.PinView_titleName)
//
//        //TypedValue.COMPLEX_UNIT_PX is the correct way to display actual dimension programmatically
//        tv_title_toolbar.setTextSize(
//            TypedValue.COMPLEX_UNIT_PX,
//            attributes.getDimensionPixelSize(R.styleable.PinView_titleTextSize, 32).toFloat()
//        )
//
//        tv_title_toolbar.setTextColor(
//            attributes.getColor(
//                R.styleable.PinView_titleTextColor,
//                Color.BLACK
//            )
//        )

        initPinCodeProgress()

        mPinView = view.findViewById<PinCodeView>(R.id.pin_view)
        val buttonDelete = view.findViewById<LinearLayout>(R.id.button_delete)
        val button0 = view.findViewById<TextView>(R.id.button_0)
        val button1 = view.findViewById<TextView>(R.id.button_1)
        val button2 = view.findViewById<TextView>(R.id.button_2)
        val button3 = view.findViewById<TextView>(R.id.button_3)
        val button4 = view.findViewById<TextView>(R.id.button_4)
        val button5 = view.findViewById<TextView>(R.id.button_5)
        val button6 = view.findViewById<TextView>(R.id.button_6)
        val button7 = view.findViewById<TextView>(R.id.button_7)
        val button8 = view.findViewById<TextView>(R.id.button_8)
        val button9 = view.findViewById<TextView>(R.id.button_9)

        val closeButton = view.findViewById<ImageView>(R.id.iv_close)

        title = view.findViewById<TextView>(R.id.tv_title_toolbar)
        mPinMessage = view.findViewById<TextView>(R.id.tv_pin_smg)
        mResetPin = view.findViewById<TextView>(R.id.tv_pin_action)

        setupView()

        val w: Int = UtilsSize.getScreenWidth(getContext()) / 5
        val first = FrameLayout.LayoutParams(w, w)
        first.gravity = Gravity.END
        button1.layoutParams = first

        val middle = FrameLayout.LayoutParams(w, w)
        middle.gravity = Gravity.CENTER
        button2.layoutParams = middle

        val last = FrameLayout.LayoutParams(w, w)
        last.gravity = Gravity.START
        button3.layoutParams = last

        button4.layoutParams = first
        button5.layoutParams = middle
        button6.layoutParams = last
        button7.layoutParams = first
        button8.layoutParams = middle
        button9.layoutParams = last

        button0.layoutParams = middle
        buttonDelete.layoutParams = last

        closeButton.setOnClickListener {
            setOnClosePinListener()
        }

        button0.setOnClickListener(OnClickListener {
            val number = button0.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())
        })
        button1.setOnClickListener(OnClickListener {
            val number = button1.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())

        })
        button2.setOnClickListener(OnClickListener {
            val number = button2.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())

        })
        button3.setOnClickListener(OnClickListener {
            val number = button3.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())

        })
        button4.setOnClickListener(OnClickListener {
            val number = button4.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())

        })

        button5.setOnClickListener(OnClickListener {
            val number = button5.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())

        })

        button6.setOnClickListener(OnClickListener {
            val number = button6.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())

        })
        button7.setOnClickListener(OnClickListener {
            val number = button7.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())

        })
        button8.setOnClickListener(OnClickListener {
            val number = button8.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())

        })
        button9.setOnClickListener(OnClickListener {
            val number = button9.text.toString()
            val codeLength: Int = mPinView!!.input(number)
            appendNumber(number.toInt())

        })

        buttonDelete.setOnClickListener(OnClickListener {
            val codeLength: Int = mPinView!!.delete()
            deleteLastPin()
            setOnPinKeyClickListener("delete")
        })

        buttonDelete.setOnLongClickListener(OnLongClickListener {
            mPinView!!.clearCode()
            deleteLastPin()
            true
        })
    }

    fun setupView(){

    }

    fun errorAction() {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v?.vibrate(400)
        val animShake = AnimationUtils.loadAnimation(context, R.anim.shake_pf)
        mPinView!!.startAnimation(animShake)
    }

    private fun initPinCodeProgress() {

        val drawable = DrawableCompat.wrap(resources.getDrawable(R.drawable.oval, null))
        DrawableCompat.setTint(
            drawable, attributes.getColor(
                R.styleable.PinView_dotUnProgressColor,
                Color.LTGRAY
            )
        )

    }

    fun deleteLastPin() {
        if (currentPinCode.isNotEmpty())
            currentPinCode = currentPinCode.dropLast(1)
    }

    fun clearPin() {
        currentPinCode = ""
        mPinView!!.clearCode()
    }

    private fun appendNumber(number: Int) {
        if (reEnterPassword!!){ //Two-time pin input
            if (currentPinCode.length < 3)
            {
                currentPinCode = currentPinCode.plus(number)
                setOnPinKeyClickListener(number.toString())
            }
            else if (currentPinCode.length == 3) {
                mCode = mPinView!!.code
                if (TextUtils.isEmpty(mCodeValidation)) {
                    Handler().postDelayed(
                            {
                                mCodeValidation = mCode
                                clearPin()
                            }, 200
                    )
                    return
                }
                if (!TextUtils.isEmpty(mCodeValidation) && mCode == mCodeValidation) {
                    setOnCompletedListener(mCode)
                } else {
                    clearPin()
                    errorAction()
                    setOnErrorListener(NOT_MATCH_PASSWORD)
                }

            }
        }else{ //Input pin
            if (currentPinCode.length < 3)
            {
                currentPinCode = currentPinCode.plus(number)
                setOnPinKeyClickListener(number.toString())
            }
            else if (currentPinCode.length == 3) {
                currentPinCode = currentPinCode.plus(number)
                setOnCompletedListener(currentPinCode)
            }
        }

    }

}
