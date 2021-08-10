/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.010
 */
package com.bnkc.sourcemodule.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Window
import android.widget.DatePicker
import com.bnkc.library.base.BaseBasicDialogFragment
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.util.Formats
import java.text.SimpleDateFormat
import java.util.*

/**
 * default date picker system
 *
 * noticed:
 * allow to display first choose from year -> dialog.datePicker.touchables[0].performClick()
 * disable pass date & enable only future -> dialog.datePicker.minDate = c.time.time
 */
class DatePickerDialog: BaseBasicDialogFragment(), DatePickerDialog.OnDateSetListener {

    /** handle date picker listener */
    private var datePickerListener: ((dateString: String?) -> Unit)? = null

    /**
     * init dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.bg_dialog_white_radius_4)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        return dialog
    }

    /**
     * set date value
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val format = SimpleDateFormat(Formats.DAY_MONTH_YEAR, Locale.getDefault())
        val selectedDate = GregorianCalendar(year, month, dayOfMonth, 0, 0).time
        datePickerListener?.invoke(format.format(selectedDate))
    }

    /**
     * dismiss dialog
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        datePickerListener = null
    }

    /**
     * date picker listener
     */
    fun onDateSelected(datePickerListener: ((dateString: String?) -> Unit)) =
            apply { this.datePickerListener = datePickerListener }
}