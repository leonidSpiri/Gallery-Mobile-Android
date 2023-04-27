package ru.spiridonov.gallery.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import ru.spiridonov.gallery.presentation.add_media.AddMediaActivity
import javax.inject.Inject

class CheckPermToAddMedia @Inject constructor(private val activity: AddMediaActivity) {

    fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            requestLocationPermission()
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        )
            requestCameraPermission()
        return true
    }

    private fun requestCameraPermission() = ActivityCompat.requestPermissions(
        activity,
        arrayOf(
            Manifest.permission.CAMERA,
        ),
        AddMediaActivity.MY_PERMISSIONS_REQUEST_CAMERA
    )

    private fun requestLocationPermission() =
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            AddMediaActivity.MY_PERMISSIONS_REQUEST_LOCATION
        )
}