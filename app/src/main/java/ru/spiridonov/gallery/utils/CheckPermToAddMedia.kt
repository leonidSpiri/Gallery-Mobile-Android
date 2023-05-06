package ru.spiridonov.gallery.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import ru.spiridonov.gallery.presentation.activity.add_media.AddMediaActivity
import javax.inject.Inject

class CheckPermToAddMedia @Inject constructor(private val activity: Activity) {

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

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            requestWritePermission()

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            )
                requestWritePermission()

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

    private fun requestWritePermission() =
        ActivityCompat.requestPermissions(
            activity,
            mutableListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray(),
            AddMediaActivity.MY_PERMISSIONS_REQUEST_WRITE
        )
}