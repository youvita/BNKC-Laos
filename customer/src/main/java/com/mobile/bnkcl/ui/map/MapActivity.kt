package com.mobile.bnkcl.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.bnkcl.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
    }
}