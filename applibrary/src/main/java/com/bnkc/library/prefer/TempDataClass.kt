package com.bnkc.library.prefer

sealed class TempDataClass(var code: String) {

    companion object {
        const val ACTION_TYPE = ""
        const val ACTION_ID = ""
        const val ACTION_URL = ""
    }

    object ActionType: TempDataClass(ACTION_TYPE)

    object ActionId: TempDataClass(ACTION_ID)

    object ActionUrl: TempDataClass(ACTION_URL)
}
