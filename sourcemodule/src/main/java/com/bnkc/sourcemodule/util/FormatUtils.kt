package com.bnkc.sourcemodule.util

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

    }

}