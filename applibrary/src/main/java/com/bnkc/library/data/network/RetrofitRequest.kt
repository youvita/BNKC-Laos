/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.data.network

import com.bnkc.library.data.type.RunTimeDataStore
import org.json.JSONObject
import retrofit2.Response

object RetrofitRequest {

    suspend fun <T> handleNetworkRequest(call: suspend () -> Response<T>) : RetrofitResponse<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val cookies = response.headers()["set-cookie"]
                if (cookies != null) {
                    val jsessionid = cookies.split(";").toTypedArray()
                    if (RunTimeDataStore.JsessionId.value.isEmpty())
                        RunTimeDataStore.JsessionId.value = jsessionid[0]
                }
                val body = response.body()
                if (body == null) {
                    RetrofitResponse.Error(response.message())
                } else {
                    RetrofitResponse.Success(body)
                }
            } else {
                var errorMessage: String? = null
                var errorCode: String? = null
                val jsonObject = JSONObject(response.errorBody()!!.string())
                val jsonError = jsonObject.getJSONObject("error")

                try {
                    errorCode = jsonError.getString("code")
                    errorMessage = jsonError.getString("message")
                }catch (e: Exception) {
                    e.printStackTrace()
                }
                RetrofitResponse.Error(errorCode, errorMessage)
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            RetrofitResponse.Exception(throwable)
        }
    }

}