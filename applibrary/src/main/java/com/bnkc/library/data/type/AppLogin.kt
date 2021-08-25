package com.bnkc.library.data.type

sealed class AppLogin(var code: String) {

    companion object {
        const val PIN_TYPE = "N"
    }

    object PIN: AppLogin(PIN_TYPE)
}
