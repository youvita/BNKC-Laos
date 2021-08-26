package com.mobile.bnkcl.ui.map

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.ConfirmDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.databinding.ActivityMapBinding
import com.mobile.bnkcl.utilities.FormatUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : BaseActivity<ActivityMapBinding>() , OnMapReadyCallback, OnCameraIdleListener,
    OnCameraMoveCanceledListener, OnCameraMoveListener, OnCameraMoveStartedListener {

    private val mapViewModel : MapViewModel by viewModels()
    private var branchId: Long = 0
    private var data: BranchResData? = null

    @Inject
    lateinit var confirmDialog: ConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarTransparent(this, true)
        super.onCreate(savedInstanceState)

        if (intent != null) {
//            branchId = intent.getLongExtra("branch_id", 0)
//            Log.d(">>>>>>", "onCreate :: $branchId")
//            mapViewModel.branchId = branchId
//            mapViewModel.reqOffice()
            data = intent.getSerializableExtra("branch_info") as BranchResData
        }
        binding.toolbarName.isSelected = true

        val llContactBranch: LinearLayout = findViewById(R.id.ll_contact_branch)
        llContactBranch.setOnClickListener {
            confirmDialog = ConfirmDialog.newInstance(
                R.drawable.ic_badge_call_now,
                getString(R.string.call_now),
                FormatUtil.getTelFormat(data!!.tel!!, 2)!!,
                getString(R.string.call)
            )
            confirmDialog.onConfirmClickedListener {
                startPhoneCall()
            }
            confirmDialog.isCancelable = false
            confirmDialog.show(supportFragmentManager, confirmDialog.tag)

        }
        binding.ivBack.setOnClickListener { finish() }
//        mapView = findViewById(R.id.mapView2)
        binding.mapView2.onCreate(savedInstanceState)

//        observeData()

        binding.tvOffice.text = data?.name
        binding.toolbarName.text = data?.name
        binding.tvAddress.text = data?.address
        binding.mapView2.getMapAsync(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_map
    }

//    private fun observeData() {
//        mapViewModel.officeLiveData
//            .observe(this, {
//                if (it != null) {
//                    data = it
//                    binding.tvOffice.setText(data?.name)
//                    binding.toolbarName.setText(data?.name)
//                    binding.tvAddress.setText(data?.address)
//                    binding.mapView2.getMapAsync(this)
//                }
//            })
//    }

    private fun startPhoneCall() {
        try {
            val number =
                Uri.parse("tel:" + FormatUtil.getTelFormat(data!!.tel!!, 1))
            val callIntent = Intent(Intent.ACTION_DIAL, number)
            startActivity(callIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                "Error in your phone call" + e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView2.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView2.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView2.onStop()
    }

    override fun onPause() {
        binding.mapView2.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.mapView2.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView2.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        /*
          Map default zoom level
         */
        val zoomLevel = data!!.location?.zoom_level
        //        mMap.setMinZoomPreference((float) data.getLocation().getZoom_level());
        googleMap.isIndoorEnabled = true
        googleMap.setOnCameraIdleListener(this)
        googleMap.setOnCameraMoveStartedListener(this)
        googleMap.setOnCameraMoveListener(this)
        googleMap.setOnCameraMoveCanceledListener(this)
        val uiSettings = googleMap.uiSettings
        uiSettings.isIndoorLevelPickerEnabled = true
        uiSettings.isMyLocationButtonEnabled = true
        val branch = LatLng(data?.location!!.lat, data?.location!!.lon)
        val markerOptions = MarkerOptions()
        markerOptions.position(branch)
            .title(data!!.name)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        val m = googleMap.addMarker(markerOptions)
        // Show Sydney on the map.
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                branch,
                if (zoomLevel!! >= 10) zoomLevel.toFloat() else Math.max(zoomLevel, 15).toFloat()
            )
        )
    }

    override fun onCameraIdle() {}

    override fun onCameraMoveCanceled() {}

    override fun onCameraMove() {}

    override fun onCameraMoveStarted(reason: Int) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
        }
    }
}