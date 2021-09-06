package com.mobile.bnkcl.ui.signup

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.DatePickerDialog
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.area.AddressData
import com.mobile.bnkcl.data.response.area.AreaItems
import com.mobile.bnkcl.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>() , View.OnClickListener{
    @Inject
    lateinit var datePickerDialog: DatePickerDialog
    private var objects: ArrayList<AreaItems>? = ArrayList()
    private lateinit var req : AddressData
    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog
    private val addressInfoViewModel: AddressInfoViewModel by viewModels()
    private var signUpDisposable: Disposable? = null
    private var selectedItem = 0

    override fun getLayoutId(): Int = R.layout.activity_sign_up
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*observe data*/
        observeCapitalArea()
        observeAddressData()

        /**
         *
         * */
        binding.llDob.setOnClickListener {
            datePickerDialog.show(supportFragmentManager, datePickerDialog.tag)
            datePickerDialog.onDateSelected {
                binding.tvDob.text

            }
        }

        addressInfoViewModel.getCapitalData()

        binding.lltAddress.llCapital.setOnClickListener {
            if (objects != null && objects!!.size > 0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_general,
                        getString(R.string.addition_capital),
                        addressInfoViewModel.setUpAreaName(objects!!),
                        selectedItem
                )

                listChoiceDialog.setOnItemListener ={
                    a : Int ->
                    selectedItem = a
                    binding.lltAddress.tvCapital.text = objects!![a].alias1

                    addressInfoViewModel.addressData = AddressData("DISTRICT",objects!![a].id.toString())
                    addressInfoViewModel.getAddress()

                }
                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
        }


        binding.lltAddress.llDistrict.setOnClickListener{
            if (objects != null && objects!!.size>0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_general,
                        "District",
                        addressInfoViewModel.setUpAreaName(objects!!),
                        selectedItem
                )

                listChoiceDialog.setOnItemListener = {
                    b : Int ->
                    selectedItem = b
                    binding.lltAddress.tvDistrict.text = objects!![b].alias1

                    addressInfoViewModel.addressData = AddressData("VILLAGE",objects!![b].id.toString())
                    addressInfoViewModel.getAddress()
                }

                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
        }

        binding.lltAddress.llVillage.setOnClickListener{
            if (objects != null && objects!!.size>0){
                listChoiceDialog = ListChoiceDialog.newInstance(
                        R.drawable.ic_badge_general,
                        "Village",
                        addressInfoViewModel.setUpAreaName(objects!!),
                        selectedItem
                )

                listChoiceDialog.setOnItemListener = {
                    b : Int ->
                    selectedItem = b
                    binding.lltAddress.tvVillage.text = objects!![b].alias1
                }

                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }

        }
    }

    private fun observeCapitalArea(){
        addressInfoViewModel.capital.observe(this){
            objects!!.clear()
            objects!!.addAll(it)
        }
    }

        private fun observeAddressData() {
       addressInfoViewModel.address.observe(this){
           objects!!.clear()
           objects!!.addAll(it)
       }
    }



    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back -> onBackPressed()

        }
    }
}