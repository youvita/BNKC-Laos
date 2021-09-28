package com.mobile.bnkcl.utilities

import android.content.Context
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.mobile.bnkcl.R
import java.text.DateFormat
import java.text.NumberFormat
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

        fun formatPhoneNumberMask(phone: String): String? {
            var phone = phone
            if (phone.startsWith("0")) phone = phone.substring(1)
            val builder = java.lang.StringBuilder()
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

        fun getTelFormat(tel: String, action: Int): String? {
            var result: String? = ""
            var tmp = ""
            var tmp1 = ""
            when (action) {
                0 -> if (tel.contains("(856)")) { //(856) 023 903 305
                    if (tel[6] == '0') {
                        if (tel.split(" ".toRegex()).toTypedArray().size == 4) {
                            result = "(+856) " + tel.substring(tel.indexOf("0") + 1)
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
                            result = "(+856) $tmp1"
                        }
                    } else {
                        result = "(+856) " + tel.substring(6)
                    }
                } else if (tel[0] == '0') {
                    if (tel.contains(" ")) {
                        result = "(+856) " + tel.substring(1)
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
                        result = "(+856) $tmp1"
                    }
                }
                1 -> if (tel.contains("(856)")) { //(855) 025 21 82 8
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
                }
                else if (tel[0] == '0') { //024 900 135
                    result = tel
                }
                2 -> if (tel.contains("-")){
                    tmp = tel.replace("-", "").substring(1)
                    var i = 0
                    while (i < tmp.length) {
                        //023478327
                        if (i == 2) {
                            tmp1 += tmp.substring(0, i) + "-"
                        } else if (i == 5) {
                            tmp1 += tmp.substring(2, i) + "-"
                        }
                        else if (i == 8) {
                            tmp1 += tmp.substring(5, i) + "-"
                        }
                        else if (i == tmp.length - 1) {
                            tmp1 += tmp.substring(8)
                        }
                        i++
                    }
                    result = "+856 $tmp1"
                }else{
                    tmp = tel.substring(1)
                    result = "+856 $tmp"
                }
                3 -> {
                    var i = 0
                    while (i < tel.length) {
                        //023478327
                        if (i == 3) {
                            tmp1 += tel.substring(0, i) + " "
                        } else if (i == 6) {
                            tmp1 += tel.substring(3, i) + " "
                        } else if (i == tel.length - 1) {
                            tmp1 += tel.substring(5) + " "
                        }
                        i++
                    }
                    result = tmp1
                }
            }
            return result
        }

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
            val builder = java.lang.StringBuilder(str1)
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
    }

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

    fun format(value: Double): String? {
        val str = value.toString()
        return format(str)
    }

    fun format(value: String?): String? {
        val str: String
        if (value == null) return ""
        if (value.trim { it <= ' ' } == "") return "0"
        val money = NumberFormat.getInstance(Locale.US)
        str = money.format(value.toDouble())
        return str
    }

    fun getTel(tel: String): String? {
        var result: String? = ""
        result = "(+856) ".plus(getTelFormat(tel, 2))
        return result
    }

}