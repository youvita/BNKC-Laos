/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.09
 */
package com.bnkc.library.data.type

/**
 * user type
 */
sealed class AppUser(val code: String) {

    companion object {
        const val CUSTOMER_TYPE = "CUSTOMER"
        const val EMPLOYEE_TYPE = "EMPLOYEE"
        const val DEALER_TYPE   = "DEALER"
    }

    object Customer: AppUser(CUSTOMER_TYPE)

    object Employee: AppUser(EMPLOYEE_TYPE)

    object Dealer: AppUser(DEALER_TYPE)
}
