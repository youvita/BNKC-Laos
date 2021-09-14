/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.05
 */
package com.bnkc.sourcemodule.app

object Constants {

    // Login info
    const val USER_ID = "USER_ID"
    const val KEY_TOKEN = "TOKEN"
    const val SESSION_ID = "SESSION_ID"

    //    MG
    const val KEY_START_URL         = "c_start_url"

    // Web
    const val WEB_URL = "WEB_URL"
    const val WEB_TITLE = "WEB_TITLE"

    // Language
    const val LANGUAGE = "LANGUAGE"


    // C/S center
    const val CLAIM_ID = "CLAIM_ID"
    const val CATEGORY = "CATEGORY"

    const val SIGN_UP_FAIL = "SIGN_UP_FAIL"
    const val SET_UP_PIN = "SET_UP_PIN"

    object HandlePush{
        const val ACTION_ON = "ACTION_ON"
        const val ACTION_TYPE = "type"
        const val ACTION_ID = "id"
        const val ACTION_URL = "url"
        const val PUSH_ID = "push_id"
    }

    object Push {
        const val PUSH_ALARM = "ALARM"
    }

    // Web Endpoint
    const val WB_NOTICES = "/mobile/views/notices?category=customer"
    const val WB_POLICY = "/pages/policy.html"
    const val WB_COM_PROFILE = "/pages/company_profile.html"
    const val WB_TER_AND_CON = "/pages/terms_and_conditions.html"
    const val WB_FAQS = "/mobile/views/faqs"

    // Image Endpoint
    const val IMAGE_URL = "/api/ca/user/profile-image-uri"
}