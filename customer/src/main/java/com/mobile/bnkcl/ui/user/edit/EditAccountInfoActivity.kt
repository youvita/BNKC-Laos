package com.mobile.bnkcl.ui.user.edit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_NORMAL
import com.bnkc.sourcemodule.base.BaseStorageActivity
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.mobile.bnkcl.R
import com.mobile.bnkcl.com.view.BnkEditText
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import com.mobile.bnkcl.data.response.user.MyLeaseData
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.ActivityEditAccountInfoBinding
import com.mobile.bnkcl.ui.dialog.AlertEditInfoDialog
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.user.photo.PhotoSettingMenu
import com.mobile.bnkcl.ui.user.photo.PhotoViewModel
import com.mobile.bnkcl.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class EditAccountInfoActivity : BaseStorageActivity<ActivityEditAccountInfoBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog

    private val PICK_IMAGE = 1
    private val PICK_CAMERA = 1000
    private var firstIndex: Int? = 0
    private var selectedIndex: Int? = 0
    private var isUpdateInfo: Boolean? = false
    private var isUpdateProfile: Boolean? = false
    private var isEditInfo: Boolean? = false
    private var isUpdateOnlyImage: Boolean? = false
    private var firstJobType: String = ""
    private var selectJobType: String = ""
    private var profileData: ProfileData? = ProfileData()
    private val photoViewModel: PhotoViewModel? by viewModels()
    private var jobTypeList: ArrayList<CodesData>? = ArrayList()
    private var jobTypeCodeList: ArrayList<String>? = ArrayList()
    private var jobTypeTitleList: ArrayList<String>? = ArrayList()
    private val viewModel: EditAccountInfoViewModel by viewModels()
    private var photoSettingMenu: PhotoSettingMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(resources.getColor(R.color.color_f5f7fc))
        setAnimateType(ANIMATE_NORMAL)
        super.onCreate(savedInstanceState)

        initView()
        initActionBar()
        initDisposable()
        initDisablePersonalInfo()
        initLiveData()

    }

    private fun initLiveData() {

        viewModel.editAccountInfoLiveData.observe(this) {
            isUpdateProfile = true
            val inflater = layoutInflater
            val view: View = inflater.inflate(
                R.layout.custom_toast_layout,
                findViewById(R.id.relativeLayout1)
            )

            val toast = Toast(this@EditAccountInfoActivity)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 200)
            toast.duration = Toast.LENGTH_LONG
            toast.view = view
            toast.show()

            isUpdateInfo = false
            isEditInfo = false

            binding.btnSave.setActive(false)
            binding.btnSave.setOnClickListener(null)
        }

        photoViewModel!!.photoSettingLiveData.observe(this) {
            when (it!!) {
                PhotoViewModel.Setting.Camera -> {
                    photoSettingMenu!!.onDismiss()
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, PICK_CAMERA)
                }
                PhotoViewModel.Setting.Gallery -> {
                    photoSettingMenu!!.onDismiss()
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, PICK_IMAGE)
                }
            }
        }
    }

    private fun initView() {

        if (intent != null) {
            profileData = intent.getSerializableExtra("ACCOUNT_INFO") as ProfileData?
            binding.profile = profileData

            jobTypeList = intent.getSerializableExtra("JOB_TYPE") as ArrayList<CodesData>?

            for (i in 0 until jobTypeList!!.size) {
                jobTypeTitleList!!.add(jobTypeList!![i].title!!)
                jobTypeCodeList!!.add(jobTypeList!![i].code!!)
            }

            for (i in 0 until jobTypeCodeList!!.size) {
                if (profileData!!.jobType.equals(jobTypeCodeList!![i])) {
                    binding.lytAddressInfo.tvJobType.text = jobTypeTitleList!![i]
                    firstIndex = i
                    selectedIndex = i
                }
            }

            binding.lytAddressInfo.edtBankName.setText(profileData!!.bankName.toString())
            binding.lytAddressInfo.edtAccountNumber.setText(profileData!!.accountNumber.toString())

            if (profileData!!.myLease!!.isNotEmpty() && profileData!!.myLease != null) {
                binding.edtNaLease.visibility = View.GONE
                addLeaseInfo(profileData!!.myLease!!)
            } else {
                binding.edtNaLease.visibility = View.VISIBLE
            }
        }

        Utils.setHideKeyboard(this, binding.parentLayout)

        Glide.with(this@EditAccountInfoActivity)
            .load(R.drawable.rotate_loading_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.ivLoading))

        val url = GlideUrl(
            sharedPrefer.getPrefer(Constants.KEY_START_URL).plus(Constants.IMAGE_URL),
            LazyHeaders.Builder()
                .addHeader(
                    "Authorization",
                    "Bearer " + sharedPrefer.getPrefer(Constants.KEY_TOKEN)
                )
                .build()
        )

        UtilsGlide.loadCircle(
            this@EditAccountInfoActivity,
            url, binding.imageProfile, binding.ivLoading, binding.flUploadUi
        )


        binding.btnCancel.setOnClickListener(this)
        binding.lytAddressInfo.llJobType.setOnClickListener(this)
        binding.cvChangeProfile.setOnClickListener(this)
        binding.imageProfile.setOnClickListener(this)

        binding.btnSave.text = resources.getString(R.string.edit_save)
        binding.btnCancel.text = resources.getString(R.string.edit_cancel)
        binding.lytAddressInfo.edtBankName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                isUpdateInfo = true
                validateButton()
            }

        })
        binding.lytAddressInfo.edtAccountNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                isUpdateInfo = true
                validateButton()
            }

        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_cancel -> {
                if (isEditInfo!!) alertNotice()
                else onBackPressed()
            }
            R.id.btn_save -> {
                val data = EditAccountInfoData()
                for (i in 0 until jobTypeCodeList!!.size) {
                    data.jobType = jobTypeCodeList!![selectedIndex!!]
                }
                data.bankName = binding.lytAddressInfo.edtBankName.text.toString()
                data.accountNumber = binding.lytAddressInfo.edtAccountNumber.text.toString()

                if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                     if (viewModel.getFile() != null) {
                        viewModel.uploadProfile()
                    }
                    if (binding.lytAddressInfo.edtBankName.text.toString() != profileData!!.bankName
                        || binding.lytAddressInfo.edtAccountNumber.text.toString() != profileData!!.accountNumber
                        || binding.lytAddressInfo.tvJobType.text != jobTypeTitleList!![firstIndex!!]){
                        viewModel.editAccountInfo(data)
                    }
                }

            }
            R.id.ll_job_type -> {
                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_toggle_table_view_on_ico,
                    getString(R.string.addition_job_type),
                    jobTypeTitleList!!,
                    selectedIndex!!
                )
                listChoiceDialog.item = 5

                listChoiceDialog.setOnItemListener = {
                    binding.lytAddressInfo.tvJobType.text = jobTypeTitleList!![it]

                    for (i in 0 until jobTypeTitleList!!.size) {
                        if (jobTypeTitleList!![i] == jobTypeTitleList!![it]) {
                            selectedIndex = i

                            firstJobType = jobTypeTitleList!![firstIndex!!]
                            selectJobType = jobTypeTitleList!![i]

                        }
                    }

                    isUpdateInfo = true
                    validateButton()
                }
                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(supportFragmentManager, listChoiceDialog.tag)
            }
            R.id.image_profile, R.id.cv_change_profile -> {
                val storagePermission: Int =
                    this.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                val cameraPermission: Int =
                    this.checkCallingOrSelfPermission(Manifest.permission.CAMERA)

                if (storagePermission == PackageManager.PERMISSION_GRANTED && cameraPermission == PackageManager.PERMISSION_GRANTED) {
                    showPhotoMenu()
                } else {
                    askPermission()
                }
            }
        }
    }

    private fun validateButton() {
        if (binding.lytAddressInfo.edtBankName.text!!.isEmpty()
            || binding.lytAddressInfo.edtAccountNumber.text!!.isEmpty()
            || binding.lytAddressInfo.tvJobType.text!!.isEmpty()
        ) {
            binding.btnSave.setActive(false)
        } else if (isUpdateInfo!! || isUpdateOnlyImage!!) {
            binding.btnSave.setActive(true)
        } else {
            binding.btnSave.setActive(true)
        }

        if (binding.btnSave.isActive()) {
            binding.btnSave.setOnClickListener(this@EditAccountInfoActivity)
        } else {
            binding.btnSave.setOnClickListener(null)
        }

        isEditInfo = true
    }

    override fun onBackPressed() {
        if (isEditInfo!!) alertNotice()
        else {
            val intent = Intent()
            intent.putExtra("IS_UPDATE_PROFILE", isUpdateProfile)
            if (isUpdateProfile!!)
                setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun initDisablePersonalInfo() {
        val res = resources.getIdentifier("round_solid_f3f6f7_8", "drawable", this.packageName)
        binding.edtName.setBackgroundResource(res)
        binding.llDob.setBackgroundResource(res)
        binding.llIdNumber.setBackgroundResource(res)
        binding.llGender.setBackgroundResource(res)
        binding.lytAddressInfo.llCapital.setBackgroundResource(res)
        binding.lytAddressInfo.llDistrict.setBackgroundResource(res)
        binding.lytAddressInfo.llVillage.setBackgroundResource(res)
        binding.lytAddressInfo.edtDetailedAddress.setBackgroundResource(res)
        binding.lytAddressInfo.edtEtc.setBackgroundResource(res)

        binding.edtName.isEnabled = false
        binding.edtDob.isEnabled = false
        binding.llIdNumber.isEnabled = false
        binding.llGender.isEnabled = false
        binding.lytAddressInfo.llCapital.isEnabled = false
        binding.lytAddressInfo.llDistrict.isEnabled = false
        binding.lytAddressInfo.llVillage.isEnabled = false
        binding.lytAddressInfo.edtDetailedAddress.isEnabled = false
        binding.lytAddressInfo.edtEtc.isEnabled = false

        binding.lytAddressInfo.tvCapital.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_more_gray_ico,
            0
        )
        binding.lytAddressInfo.tvDistrict.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_more_gray_ico,
            0
        )
        binding.lytAddressInfo.tvVillage.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_more_gray_ico,
            0
        )

        binding.lytAddressInfo.tvCapital.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.color_c4d0d6
            )
        )
        binding.lytAddressInfo.tvDistrict.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.color_c4d0d6
            )
        )
        binding.lytAddressInfo.tvVillage.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.color_c4d0d6
            )
        )

        if (intent != null && profileData != null) {
            binding.lytAddressInfo.tvCapital.text = profileData!!.address?.state?.alias1
            binding.lytAddressInfo.tvDistrict.text = profileData!!.address?.district?.alias1
            binding.lytAddressInfo.tvVillage.text = profileData!!.address?.village?.alias1
            binding.lytAddressInfo.edtDetailedAddress.hint = profileData!!.address?.state?.alias1
            binding.lytAddressInfo.edtDetailedAddress.hint = profileData!!.etcDetailedAddress
            binding.lytAddressInfo.edtEtc.hint = profileData!!.edtStatus.toString()
        }
    }

    private fun initActionBar() {
        val actionBar = binding.actionBar
        actionBar.setBackgroundActionBar(ContextCompat.getColor(this, R.color.white))
        actionBar.setToolbarTitleWithActionBack(
            R.drawable.ic_nav_close_dark_btn,
            getString(R.string.edit_acc_info)
        )
        actionBar.setOnMenuLeftClick {
            if (isEditInfo!!) alertNotice()
            else onBackPressed()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_account_info
    }

    private fun addLeaseInfo(contractNo: List<MyLeaseData>) {

        for (element in contractNo) {
            val layout = binding.llLeaseProductNumber
            layout.setBackgroundResource(R.drawable.round_solid_ffffff_8)
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            params.setMargins(0, UtilsSize.getValueInDP(this, 10), 0, 0)
            val res = resources.getIdentifier("round_solid_f3f6f7_8", "drawable", this.packageName)
            val editText = BnkEditText(this)
            editText.layoutParams = params
            editText.textSize = 15F
            editText.isEnabled = false
            editText.setBackgroundResource(res)
            editText.hint = element.contractNo
            editText.typeface = ResourcesCompat.getFont(this, R.font.rubik_medium)
            editText.height = UtilsSize.getValueInDP(this, 50)
            editText.setHintTextColor(ContextCompat.getColor(this, R.color.color_c4d0d6))
            editText.setPadding(UtilsSize.getValueInDP(this, 15), 0, 0, 0)
            layout.addView(editText)
        }

    }

    private fun alertNotice() {
        val alertEditInfoDialog = AlertEditInfoDialog()
        alertEditInfoDialog.onConfirmClickedListener {
            val intent = Intent()
            intent.putExtra("IS_UPDATE_PROFILE", isUpdateInfo)
            if (isUpdateInfo!!) setResult(RESULT_OK, intent)
            finish()
        }
        alertEditInfoDialog.show(supportFragmentManager, alertEditInfoDialog.tag)
    }

    private fun initDisposable() {
        disposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe {
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                sharedPrefer.putPrefer(Constants.KEY_TOKEN, "")
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }
    }

    private fun showPhotoMenu() {
        photoSettingMenu = PhotoSettingMenu(this@EditAccountInfoActivity, photoViewModel)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RESULT_OK -> {
                showPhotoMenu()
            }
            RESULT_CANCELED -> {
                finish()
            }
        }
    }

    override fun onPermissionGranted() {
        super.onPermissionGranted()
        showPhotoMenu()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) return
        var file: File? = null
        var imageUri: Uri? = null
        var imageBitmap: Bitmap? = null
        when (requestCode) {
            PICK_IMAGE -> if (data != null) {
                imageUri = data.data
                val paths: String = getPath(this, imageUri)!!
                file = File(paths)
            }
            PICK_CAMERA -> if (data != null) {
                val extras: Bundle? = data.extras
                if (extras != null) {
                    imageBitmap = extras["data"] as Bitmap?
                }
                imageUri = getUri(applicationContext, imageBitmap!!)
                file = File(getRealPathFromURI(imageUri))
            }
        }
        assert(file != null)
        if (file!!.exists()) {
            viewModel.setFile(file)
            isUpdateOnlyImage = true
            validateButton()
            UtilsGlide.loadCircle(
                this@EditAccountInfoActivity,
                imageUri,
                binding.imageProfile,
                null
            )
        }
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        var path = ""
        if (contentResolver != null) {
            val cursor = contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private fun getUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getPath(context: Context, uri: Uri?): String? {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }
}