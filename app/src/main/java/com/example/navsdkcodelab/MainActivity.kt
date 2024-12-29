// Copyright 2024 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.example.navsdkcodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.navsdkcodelab.ui.theme.NavSDKCodelabTheme
import android.content.res.Configuration
import com.google.android.libraries.navigation.NavigationView
import android.view.WindowManager
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    companion object {
        const val SPLASH_SCREEN_DELAY_MILLIS = 1000L
    }

    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestAccessPermissions()

        navView = findViewById(R.id.navigation_view)
        navView.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    override fun onStart() {
        super.onStart()
        navView.onStart()
    }

    override fun onResume() {
        super.onResume()
        navView.onResume()
    }

    override fun onPause() {
        navView.onPause()
        super.onPause()
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)
        navView.onConfigurationChanged(configuration)
    }

    override fun onStop() {
        navView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        navView.onDestroy()
        super.onDestroy()
    }

    private fun requestAccessPermissions() {
        val permissions =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        if (permissions.any { !checkPermissionGranted(it) }) {
            if (permissions.any { shouldShowRequestPermissionRationale(it) }) {
                // Display a dialogue explaining the required permissions.
            }
            val permissionsLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                        permissionResults ->
                    if (
                        permissionResults.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
                    ) {
                        onLocationPermissionGranted()
                    } else {
                        finish()
                    }
                }
            permissionsLauncher.launch(permissions)
        } else {
            android.os
                .Handler(Looper.getMainLooper())
                .postDelayed({ onLocationPermissionGranted() }, SPLASH_SCREEN_DELAY_MILLIS)
        }
    }

    private fun checkPermissionGranted(permissionToCheck: String): Boolean =
        ContextCompat.checkSelfPermission(this, permissionToCheck) == PackageManager.PERMISSION_GRANTED

    private fun onLocationPermissionGranted() {}


}
