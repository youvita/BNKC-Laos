/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.data.network

import org.json.JSONObject
import retrofit2.Response

object RetrofitRequest {

    suspend fun <T> handleNetworkRequest(call: suspend () -> Response<T>) : RetrofitResponse<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body == null) {
                    RetrofitResponse.Error(response.code())
                } else {
                    RetrofitResponse.Success(body)
                }
            } else {
                var errorMessage: String? = null
                var errorCode: String? = null
                val jsonObject = JSONObject(response.errorBody()!!.string())

                try {
                    errorCode = jsonObject.getString("code")
                    errorMessage = jsonObject.getString("message")
                }catch (e: Exception) {
                    e.printStackTrace()
                }
                RetrofitResponse.Error(response.code(), errorCode, errorMessage)
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            RetrofitResponse.Exception(throwable)
        }
    }

}