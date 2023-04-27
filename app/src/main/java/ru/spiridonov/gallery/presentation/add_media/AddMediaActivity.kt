package ru.spiridonov.gallery.presentation.add_media

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.databinding.ActivityAddMediaBinding
import ru.spiridonov.gallery.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class AddMediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMediaBinding

    private val component by lazy {
        (application as GalleryApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AddMediaViewModel


    private var locationManager: LocationManager? = null

    private val locationListener: LocationListener = LocationListener { location ->
        Log.d(
            "myTag",
            "Location Changed, new location " + location.longitude + ":" + location.latitude
        )
        myLocation = location
    }

   private lateinit var myLocation: Location


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityAddMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[AddMediaViewModel::class.java]
        checkLocation()
        // if(::myLocation.isInitialized)
    }

    private fun startLocationService() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                locationManager?.requestLocationUpdates(
                    LocationManager.FUSED_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
            else
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }
    }

    private fun checkLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            showAlertLocation()
        startLocationService()
    }

    private fun showAlertLocation() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("Your location settings is set to Off, Please enable location to use this application")
        dialog.setPositiveButton("Settings") { _, _ ->
            requestLocationPermission()
        }
        dialog.setNegativeButton("Cancel") { _, _ ->
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun requestLocationPermission() =
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startLocationService()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AddMediaActivity::class.java)

        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99


    }
}