/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.05
 */
package com.bnkc.sourcemodule.app

object Constants {

    const val ANIMATE_LEFT = 5656
    const val ANIMATE_NORMAL = 5757

    // Login info
    const val USER_ID = "USER_ID"
    const val CUST_NO = "CUST_NO"
//    const val KEY_TOKEN = "TOKEN"
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

    const val SIGN_UP = "SIGN_UP"
    const val SET_UP_PIN = "SET_UP_PIN"
    const val RESET_PIN = "RESET_PIN"

    object HandlePush{
        const val ACTION_URL = "url"
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
    const val WB_BNKCL_FB = "https://www.facebook.com/bscapitallaos"

    // Image Endpoint
    const val IMAGE_URL = "/api/ca/user/profile-image-uri"
    const val IMAGE_BITMAP = ""
}