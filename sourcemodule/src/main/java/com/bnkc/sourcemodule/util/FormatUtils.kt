package com.bnkc.sourcemodule.util

import android.content.Context
import com.bnkc.sourcemodule.R
import android.text.SpannableString
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FormatUtils {
    companion object{
        fun getDateFormat(inputDate: String?, action: Int): String {
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
                        newFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    }
                    2 -> {
                        formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        date = formatter.parse(inputDate)
                        newFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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
                    6 -> {
                        formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault())
                        date = formatter.parse(inputDate)
                        newFormatter = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                    }
                    7 -> {

                        formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

                        date = formatter.parse(inputDate)

                        newFormatter = SimpleDateFormat("dd-MM-yyyy-HH:mm a", Locale.getDefault())

                    }
                    8 -> {
                        formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        if (inputDate == null) {
                            inputDate = "2020-10-09"
                        }
                        date = formatter.parse(inputDate)
                        newFormatter = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                    }
                    else -> throw IllegalStateException("Unexpected value: $action")
                }
                stDate = newFormatter.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return stDate
        }

        /*
        * USD 12345 -> 12,345 USD
        * */
        fun getNumberFormat(context: Context, input: String): String {
            var currency = ""
            try {
                currency = input.substring(0, 4)
                currency =
                    if (currency.contains("USD")) context.getString(R.string.currency_usd) else context.getString(
                        R.string.currency_kip
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
                    if (currency.contains("USD")) context.getString(R.string.currency_usd) else context.getString(
                        R.string.currency_kip
                    )
                var str1 = number
                var str2 = ""
                if (str1.contains(".")) {
                    var dot_count = 0
                    for (element in number) {
                        if ('.' == element) {
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
                return "0"
            }
            return history
        }
        fun getTelFormat(tel: String, action: Int): String? {
            var result: String? = ""
            var tmp = ""
            var tmp1 = ""
            when (action) {
                0 -> if (tel.contains("(855)")) { //(855) 023 903 305
                    if (tel[6] == '0') {
                        if (tel.split(" ".toRegex()).toTypedArray().size == 4) {
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
                        if (tel.split(" ".toRegex()).toTypedArray().size == 4) {
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

        fun getFormatOnlyDate(input: String, lang: String): String {
            return if (lang.equals("en", ignoreCase = true)) {
                when (input) {
                    "01", "1", "21", "31" -> "" + input + "st"
                    "02", "2", "22" -> "" + input + "nd"
                    "03", "3", "23" -> "" + input + "rd"
                    else -> "" + input + "th"
                }
            } else input
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

    }

}