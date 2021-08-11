/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.data.network

sealed class RetrofitResponse<out T> {

    data class Success<T>(val body: T) : RetrofitResponse<T>()

    data class Exception<T>(val throwable: Throwable) : RetrofitResponse<T>()

    data class Error<T>(val code: Int, val errorTitle: String? = null,val errorMessage : String? = null) : RetrofitResponse<T>()
}