package com.bnkc.sourcemodule.util

import android.content.Context
import com.bnkc.sourcemodule.R
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

        private fun getNumberFormatWithoutCurrency(context: Context, input: String): String {
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
    }

}