package com.bnkc.sourcemodule.data

sealed class SettingMenu(val code: String) {

    companion object {
        const val GALLERY = "1"
        const val CAMERA  = "2"
    }

    object Gallery: SettingMenu(GALLERY)

    object Camera: SettingMenu(CAMERA)
}
