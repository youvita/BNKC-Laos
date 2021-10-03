package com.mobile.bnkcl.util

import java.text.NumberFormat
import java.util.*

fun String.getDecimalFormattedString(value: String, is_negative: Boolean): String? {
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

fun String.format(value: Double): String? {
    val str = value.toString()
    return format(str)
}

fun String.format(value: String?): String? {
    val str: String
    if (value == null) return ""
    if (value.trim { it <= ' ' } == "") return "0"
    val money = NumberFormat.getInstance(Locale.US)
    str = money.format(value.toDouble())
    return str
}