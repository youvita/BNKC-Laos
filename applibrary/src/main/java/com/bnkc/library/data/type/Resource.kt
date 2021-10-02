/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.data.type

import com.bnkc.library.data.type.Status.*

sealed class Resource<out T>(
        val status: Status,
        val data: T?,
        val code: String?,
        val message: String?
) {

   data class Loading<out T>(
      private val resource: T? = null
   ) : Resource<T>(LOADING, resource, null, null)

   data class Success<out T>(
      private val resource: T?,
   ) : Resource<T>(SUCCESS, resource, null, null)

   data class Error<out T>(
      private val errCode: String?,
      private val errMess: String?,
      private val resource: T? = null
   ) : Resource<T>(ERROR, resource, errCode, errMess)

}