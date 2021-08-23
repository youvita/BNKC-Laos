package com.mobile.bnkcl.utilities

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.res.ResourcesCompat
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.mobile.bnkcl.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FormatUtil {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer
    /**
     * from 2020-09-03T11:24:42.024Z to 03-09-20
     * from 20201009 to 09/10/20
     */
companion object{

//    @JvmStatic
    fun getDateFormat(inputDate: String?, action: Int): String? {
        var inputDate = inputDate
        var stDate = ""
        val date: Date
        try {
            val formatter: DateFormat
            val newFormatter: DateFormat
            when (action) {
                1 -> {
                    formatter =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    date = formatter.parse(inputDate)
                    newFormatter = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                }
                2 -> {
                    formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    date = formatter.parse(inputDate)
                    newFormatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                }
                3 -> {
                    formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                    date = formatter.parse(inputDate)
                    newFormatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                }
                4 -> {
                    formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    if (inputDate == null) {
                        inputDate = "2020-10-09"
                    }
                    date = formatter.parse(inputDate)
                    newFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                }
                5 -> {
                    formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    if (inputDate == null) {
                        inputDate = "2020-10-09"
                    }
                    date = formatter.parse(inputDate)
                    newFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                }
                else -> throw IllegalStateException("Unexpected value: $action")
            }
            stDate = newFormatter.format(date)

            /* if (inputDate.length() >=24){ //from 2020-09-03T11:24:42.024Z to 03-09-20

                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                date = formatter.parse(inputDate);
                newFormatter  = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());


            }else if (inputDate.length()>=10){
                formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                date = formatter.parse(inputDate);
                newFormatter  = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            } else{ //from 20201009 to 09/10/20
                formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                date = formatter.parse(inputDate);
                newFormatter  = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            }*/stDate = newFormatter.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stDate
    }

    }

//    fun getDateFormat(inputDate: String?, action: Int): String? {
//        var inputDate = inputDate
//        var stDate = ""
//        val date: Date
//        try {
//            val formatter: DateFormat
//            val newFormatter: DateFormat
//            when (action) {
//                1 -> {
//                    formatter =
//                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//                    date = formatter.parse(inputDate)
//                    newFormatter = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
//                }
//                2 -> {
//                    formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                    date = formatter.parse(inputDate)
//                    newFormatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
//                }
//                3 -> {
//                    formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
//                    date = formatter.parse(inputDate)
//                    newFormatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
//                }
//                4 -> {
//                    formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                    if (inputDate == null) {
//                        inputDate = "2020-10-09"
//                    }
//                    date = formatter.parse(inputDate)
//                    newFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//                }
//                5 -> {
//                    formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//                    if (inputDate == null) {
//                        inputDate = "2020-10-09"
//                    }
//                    date = formatter.parse(inputDate)
//                    newFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                }
//                else -> throw IllegalStateException("Unexpected value: $action")
//            }
//            stDate = newFormatter.format(date)
//
//            /* if (inputDate.length() >=24){ //from 2020-09-03T11:24:42.024Z to 03-09-20
//
//                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//                date = formatter.parse(inputDate);
//                newFormatter  = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());
//
//
//            }else if (inputDate.length()>=10){
//                formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                date = formatter.parse(inputDate);
//                newFormatter  = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
//            } else{ //from 20201009 to 09/10/20
//                formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
//                date = formatter.parse(inputDate);
//                newFormatter  = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
//            }*/stDate = newFormatter.format(date)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return stDate
//    }

    /*
     * from 2020-09-03T11:24:42.024Z to 03-09-20 - 11:24AM
     * from 20201009 to 09/10/20
     * */
    fun getDateFormatWithAmPm(inputDate: String?): String? {
        var stDate = ""
        var result = ""
        val date: Date
        try {
            val formatter: DateFormat
            val newFormatter: DateFormat
            formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            date = formatter.parse(inputDate)
            newFormatter = SimpleDateFormat("dd-MM-yyyy - hh:mm aa", Locale.getDefault())
            stDate = newFormatter.format(date)
            result =
                stDate.substring(0, stDate.length - 3) + " " + stDate.substring(stDate.length - 2)
                    .toLowerCase()[0] + "." + stDate.substring(stDate.length - 1)
                    .toLowerCase()[0].toString() + "."
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    /*
    * USD 12345 -> 12,345 USD
    * */

    /*
    * USD 12345 -> 12,345 USD
    * */
    fun getNumberFormat(context: Context, input: String): String? {
        var currency = ""
        try {
            currency = input.substring(0, 4)
            currency =
                if (currency.contains("USD")) context.getString(R.string.curr_usd) else context.getString(
                    R.string.curr_kip
                )
        } catch (e: Exception) {
        }
        return getNumberFormatWithoutCurrency(context, input) + " " + currency
    }

    fun getNumberFormatWithoutCurrency(context: Context, input: String): String {
        val number: String
        var currency: String
        var history = ""
        try {
            currency = input.substring(0, 4)
            number = input.substring(4)
            currency =
                if (currency.contains("USD")) context.getString(R.string.curr_usd) else context.getString(
                    R.string.curr_kip
                )
            var str1 = number
            var str2 = ""
            if (str1.contains(".")) {
                var dot_count = 0
                for (i in 0 until number.length) {
                    if ('.' == number[i]) {
                        dot_count++
                    }
                }
                if (dot_count > 1) {
                    return history
                } else {
                    str1 = number.substring(0, number.indexOf("."))
                    if ("" == str1) {
                        str1 = "0"
                    }
                    str2 = number.substring(number.indexOf("."), number.length)
                    if (str2.length > 2) {
                        str2 = str2.substring(0, 3)
                    }
                }
            }
            str1 = str1.replaceFirst("^0+(?!$)".toRegex(), "") //replace leading 0
            val builder = StringBuilder(str1)
            val reverseStr = builder.reverse().toString()
            var commaCount = 0
            for (i in 1 until reverseStr.length) {
                if (i % 3 == 0) {
                    builder.insert(commaCount + i, ",")
                    commaCount++
                }
            }
            str1 = builder.reverse().toString()
            history = str1 + str2 //forgot it
        } catch (e: Exception) {
            //e.printStackTrace();
            return "0"
        }
        return history
    }

    fun getFormatOnlyDate(context: Context?, input: String): String? {
        val language =  sharedPrefer.getPrefer(com.bnkc.library.util.Constants.CHANGED_LANGUAGE)
        return if (language.equals("en", ignoreCase = true)) {
            when (input) {
                "1", "21", "31" -> "" + input + "st"
                "2", "22" -> "" + input + "nd"
                "3", "23" -> "" + input + "rd"
                else -> "" + input + "th"
            }
        } else input
    }

    fun formatPhoneNumberMask(phone: String): String? {
        var phone = phone
        if (phone.startsWith("0")) phone = phone.substring(1)
        val builder = StringBuilder()
        for (i in 0 until phone.length) {
            if (i < 2 || i > phone.length - 3) {
                builder.append(phone[i])
            } else if (i == 2 || i == 5) {
                builder.append("-")
                builder.append("*")
            } else {
                builder.append("*")
            }
        }
        return builder.toString()
    }

    /**
     * 0 : for show phone number format ex.(855) 23 534 234
     * 1 : for call ex.012 123 654
     * @param tel
     * @param action
     * @return
     */
    fun getTelFormat(tel: String, action: Int): String? {
        var result: String? = ""
        var tmp = ""
        var tmp1 = ""
        when (action) {
            0 -> if (tel.contains("(855)")) { //(855) 023 903 305
                if (tel[6] == '0') {
                    if (tel.split(" ").toTypedArray().size == 4) {
                        result = "(+855) " + tel.substring(tel.indexOf("0") + 1)
                    } else {
                        tmp = tel.substring(tel.indexOf("0") + 1).replace(" ", "")
                        var i = 0
                        while (i < tmp.length) {
                            if (i == 2) {
                                tmp1 += tmp.substring(0, i) + " "
                            } else if (i == 5) {
                                tmp1 += tmp.substring(2, i) + " "
                            } else if (i == tmp.length - 1) {
                                tmp1 += tmp.substring(5) + " "
                            }
                            i++
                        }
                        result = "(+855) $tmp1"
                    }
                } else {
                    result = "(+855) " + tel.substring(6)
                }
            } else if (tel[0] == '0') {
                if (tel.contains(" ")) {
                    result = "(+855) " + tel.substring(1)
                } else {
                    tmp = tel.substring(1)
                    var i = 0
                    while (i < tel.length) {
                        if (i == 2) {
                            tmp1 += tmp.substring(0, i) + " "
                        } else if (i == 5) {
                            tmp1 += tmp.substring(2, i) + " "
                        } else if (i == tmp.length - 1) {
                            tmp1 += tmp.substring(5) + " "
                        }
                        i++
                    }
                    result = "(+855) $tmp1"
                }
            }
            1 -> if (tel.contains("(855)")) { //(855) 025 21 82 8
                if (tel[6] == '0') {
                    if (tel.split(" ").toTypedArray().size == 4) {
                        result = tel.substring(tel.indexOf("0"))
                    } else {
                        tmp = tel.substring(tel.indexOf("0")).replace(" ", "")
                        var i = 0
                        while (i < tmp.length) {
                            if (i == 3) {
                                tmp1 += tmp.substring(0, i) + " "
                            } else if (i == 6) {
                                tmp1 += tmp.substring(3, i) + " "
                            } else if (i == tmp.length - 1) {
                                tmp1 += tmp.substring(6) + " "
                            }
                            i++
                        }
                        result = tmp1
                    }
                } else {
                    result = "0" + tel.substring(6)
                }
            } else if (tel[0] == '0') { //024 900 135
                result = tel
            }
            2 -> {
                tmp = tel.substring(1)
                var i = 0
                while (i < tmp.length) {
                    //023478327
                    if (i == 2) {
                        tmp1 += tmp.substring(0, i) + " "
//                        DevLog.devLog(">>>>>>>", "tmp 1:: $tmp1")
                    } else if (i == 5) {
                        tmp1 += tmp.substring(2, i) + " "
                    } else if (i == tmp.length - 1) {
                        tmp1 += tmp.substring(5) + " "
                    }
                    i++
                }
                result = "+855 $tmp1"
            }
        }
        return result
    }

    fun getSeparateFont(
        mContext: Context?,
        indexStart: Int,
        lang: String,
        text: String
    ): SpannableString? {
        val ss = SpannableString(text)
//        if (lang == "en") {
//            ss.setSpan(
//                CustomTypefaceSpan(ResourcesCompat.getFont(mContext!!, R.font.rubik_medium)),
//                indexStart,
//                text.split(" ").toTypedArray()[0].length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        } else {
//            ss.setSpan(
//                CustomTypefaceSpan(
//                    ResourcesCompat.getFont(
//                        mContext!!,
//                        R.font.kantumruy_bold
//                    )
//                ), indexStart, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        }
        return ss
    }

    fun getSeparateFont(
        mContext: Context?,
        indexStart: Int,
        indexEnd: Int,
        lang: String,
        text: String?
    ): SpannableString? {
        val ss = SpannableString(text)
//        if (lang == "en") {
//            ss.setSpan(
//                CustomTypefaceSpan(ResourcesCompat.getFont(mContext!!, R.font.rubik_medium)),
//                indexStart,
//                indexEnd,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        } else {
//            ss.setSpan(
//                CustomTypefaceSpan(
//                    ResourcesCompat.getFont(
//                        mContext!!,
//                        R.font.kantumruy_bold
//                    )
//                ), indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        }
        return ss
    }

    /**
     * Set khmer font style with specific
     */
    fun getSeparateFont(
        mContext: Context?,
        indexStart: Int,
        indexEnd: Int,
        text: String?
    ): SpannableString? {
        val ss = SpannableString(text)
//        ss.setSpan(
//            CustomTypefaceSpan(ResourcesCompat.getFont(mContext!!, R.font.kantumruy_bold)),
//            indexStart,
//            indexEnd,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
        return ss
    }

    /**
     * Set khmer font style
     */
    fun getSeparateFont(mContext: Context?, text: String): SpannableString? {
        val ss = SpannableString(text)
//        ss.setSpan(
//            CustomTypefaceSpan(ResourcesCompat.getFont(mContext!!, R.font.kantumruy_bold)),
//            0,
//            text.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
        return ss
    }

    /**
     * Set font style with specific rage : start , end
     */
    fun getSeparateFontByLang(
        mContext: Context,
        indexStart: Int,
        indexEnd: Int,
        text: String?,
        isClicked: Boolean
    ): SpannableString? {
        val ss = SpannableString(text)
//        if (isClicked) {
//            ss.setSpan(
//                CustomTypefaceSpan(Utils.getTypeFace(mContext, 2)),
//                indexStart,
//                indexEnd,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//            ss.setSpan(
//                ForegroundColorSpan(mContext.resources.getColor(R.color.color_d7191f)),
//                indexStart,
//                indexEnd,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        } else {
//            if (LocaleHelper.getLanguage(mContext).equals("en")) {
//                ss.setSpan(
//                    CustomTypefaceSpan(Utils.getTypeFace(mContext, "en", 2)),
//                    indexStart,
//                    indexEnd,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//            }
//            ss.setSpan(
//                ForegroundColorSpan(mContext.resources.getColor(R.color.color_263238)),
//                indexStart,
//                indexEnd,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        }
        return ss
    }

    /**
     * Convert string number to decimal format
     */
    fun getDecimalFormattedString(value: String, is_negative: Boolean): String? {
        var value = value
        var history = ""
        value = value.replace(",".toRegex(), "")
        var sub = ""
        value = value.replace("-".toRegex(), "")
        value = value.replaceFirst("^0+(?!$)".toRegex(), "")
        if (is_negative && "0" != value && "" != value) {
            sub = "-"
        }
        var str1 = value
        var str2 = ""
        if (str1.contains(".")) {
            var dot_count = 0
            for (i in 0 until value.length) {
                if ('.' == value[i]) {
                    dot_count++
                }
            }
            if (dot_count > 1) {
                return history
            } else {
                str1 = value.substring(0, value.indexOf("."))
                if ("" == str1) {
                    str1 = "0"
                }
                str2 = value.substring(value.indexOf("."), value.length)
                if (str2.length > 2) {
                    str2 = str2.substring(0, 3)
                }
            }
        }
        str1 = str1.replaceFirst("^0+(?!$)".toRegex(), "") //replace leading 0
        val builder = StringBuilder(str1)
        val reverseStr = builder.reverse().toString()
        var commaCount = 0
        for (i in 1 until reverseStr.length) {
            if (i % 3 == 0) {
                builder.insert(commaCount + i, ",")
                commaCount++
            }
        }
        str1 = builder.reverse().toString()
        history = sub + str1 + str2 //forgot it
        return history
    }

    /**
     * Used to append % in the input text Rate ex : 12.4%
     */
    fun getPercentageFormattedString(value: String): String? {
        var value = value
        var history = ""
        var fixedValue = ""
        if (value.length == 4 && value[2] == '.') { // #case : input dot manually and have 2 digit already ex: 12
            history = value
        } else {
            if (value.length == 1 && value[0] == '%') { // #case : user delete all number we have to append nothing to EditText
                history = ""
            } else if (value.length == 4 && value[1] != '.') { // #case : input dot manually
                fixedValue = value.replace("%", "")
                value = if (value[fixedValue.length - 1] == '.') {
                    fixedValue.replace(".", "").substring(0, fixedValue.length - 1)
                } else if (value[2] == '.') {
                    fixedValue
                } else {
                    fixedValue.replace(".", "").substring(
                        0,
                        value.replace("%", "").length - 1
                    ) + "." + value[fixedValue.length - 1].toString() + ""
                }
                history = "$value%"
            } else {
                fixedValue = value.replace("%", "")
                value = fixedValue
                history = "$value%"
            }
        }
        return history
    }

    /**
     * This method will convert latin number to khmer number
     * Ex : 1 million reils = 250 dollar
     */
    fun getInterNumeralFormat(numString: String): Int {
        var num0: String
        var num1: String
        var num2: String
        var num3: String
        var num4: String
        var num5: String
        var num6: String
        var num7: String
        var num8: String
        var num9: String
        val acceptNumber = StringBuilder()
        return if (numString.contains(" ")) {
            numString.split("\\s+").toTypedArray()[0].toInt()
        } else {
            val s1 = numString.substring(0, 2)
            for (i in 0 until s1.length) {
                if (s1[i] == '០') {
                    num0 = "0"
                    acceptNumber.append(num0)
                } else if (s1[i] == '១') {
                    num1 = "1"
                    acceptNumber.append(num1)
                } else if (s1[i] == '២') {
                    num2 = "2"
                    acceptNumber.append(num2)
                } else if (s1[i] == '៣') {
                    num3 = "3"
                    acceptNumber.append(num3)
                } else if (s1[i] == '៤') {
                    num4 = "4"
                    acceptNumber.append(num4)
                } else if (s1[i] == '៥') {
                    num5 = "5"
                    acceptNumber.append(num5)
                } else if (s1[i] == '៦') {
                    num6 = "6"
                    acceptNumber.append(num6)
                } else if (s1[i] == '៧') {
                    num7 = "7"
                    acceptNumber.append(num7)
                } else if (s1[i] == '៨') {
                    num8 = "8"
                    acceptNumber.append(num8)
                } else {
                    num9 = "9"
                    acceptNumber.append(num9)
                }
            }
            acceptNumber.toString().toInt()
        }
    }
}