/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.rxjava

class RxEvent {

    class CommentSuccess(val value: String)

    class MGSuccess(val value: String)

    class ResponseSuccess

    class ServerError(val code: Int, val title: String, val message: String)
}