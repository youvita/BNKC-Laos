package com.bnkc.library.data.type

sealed class AppLogin(var code: String) {

    companion object {
        const val PIN_TYPE = "N"
        const val INTERCEPT_INTENT = "N"
    }

    object PIN: AppLogin(PIN_TYPE)
    object InterceptIntent: AppLogin(INTERCEPT_INTENT)
}
