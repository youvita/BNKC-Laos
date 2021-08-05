/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.04
 */
package com.bnkc.library.prefer

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * read and write encrypted data shared preferences easily and safely.
 */
class CredentialSharedPrefer(context: Context) {

    /**
     * file name
     */
    private val sharedPrefsFile: String = "CRE_SHARED_PREFER"

    /**
     * encrypt data with encryption keys
     */
    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    /**
     * create encrypt shared preferences
     */
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        sharedPrefsFile,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * push value prefer
     * @param key set name for value
     * @param value set value
     */
    fun putPrefer(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    /**
     * get value prefer
     * @param key get value by access name
     * @return empty by default value
     */
    fun getPrefer(key: String) = sharedPreferences.getString(key, "")

    /**
     * created instead of using the static keyword for class methods
     */
    companion object {
        private var instance: CredentialSharedPrefer? = null

        fun getInstance(context: Context): CredentialSharedPrefer {
            if (instance == null) {
                synchronized(CredentialSharedPrefer::class.java) {
                    if (instance == null) {
                        instance = CredentialSharedPrefer(context)
                    }
                }
            }
            return instance!!
        }
    }

}