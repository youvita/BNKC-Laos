package com.bnkc.library.data.type

sealed class RunTimeDataStore(var value: String) {

    companion object {

        // push notification
        const val ACTION_TYPE = ""
        const val ACTION_ID = ""
        const val ACTION_URL = ""

        // MG
        const val BASE_URL = ""

        // Login Info
        const val LOGIN_TOKEN = ""
    }

    object ActionType: RunTimeDataStore(ACTION_TYPE)

    object ActionId: RunTimeDataStore(ACTION_ID)

    object ActionUrl: RunTimeDataStore(ACTION_URL)

    object BaseUrl: RunTimeDataStore(BASE_URL)

    object LoginToken: RunTimeDataStore(LOGIN_TOKEN)
}
