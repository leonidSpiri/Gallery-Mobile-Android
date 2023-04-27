package ru.spiridonov.gallery.presentation.add_media

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.databinding.ActivityAddMediaBinding
import ru.spiridonov.gallery.presentation.viewmodels.ViewModelFactory
import ru.spiridonov.gallery.utils.CheckPermToAddMedia
import ru.spiridonov.gallery.utils.ShowAlert
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class AddMediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMediaBinding

    private val component by lazy {
        (application as GalleryApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AddMediaViewModel


    private lateinit var pDialog: ProgressDialog

    @get:Inject
    val checkPermToAddMedia by lazy { CheckPermToAddMedia(this) }

    private var locationManager: LocationManager? = null

    private var imageUri: Uri? = null
    private var imageName = ""

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
        if (checkPermToAddMedia.checkPermissions())
            startLocationService()
        handlerClickListener()
    }

    private fun handlerClickListener() {
        binding.btnCamera.setOnClickListener {
            if (!checkPermToAddMedia.checkPermissions()) return@setOnClickListener
            imageUri = null
            binding.imgPhoto.setImageDrawable(null)
            imageUri = openCameraInterface()
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            resultTakePhoto.launch(intent)
        }

        binding.btnGallery.setOnClickListener {
            if (!checkPermToAddMedia.checkPermissions()) return@setOnClickListener
            imageUri = null
            binding.imgPhoto.setImageDrawable(null)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultTakePhoto.launch(intent)
        }

        binding.btnSave.setOnClickListener {
            if (imageUri == null) {
                ShowAlert(this, "Выберите фото")
                return@setOnClickListener
            }

            pDialog = ProgressDialog(this)
            pDialog.setMessage("Загрузка...")
            pDialog.show()

            val iikBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
            viewModel.uploadPhoto(
                iikBitmap,
                if (::myLocation.isInitialized) "${myLocation.latitude} ${myLocation.longitude}" else ""
            )
        }
    }

    private fun openCameraInterface(): Uri? {
        imageName = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Gallery")
        }
        return contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    private var resultTakePhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    if (imageUri?.path == null)
                        imageUri = result.data?.data
                    var ivkeBitmap = BitmapFactory.decodeStream(
                        contentResolver.openInputStream(imageUri!!)
                    )
                    ivkeBitmap = viewModel.getResizedBitmap(ivkeBitmap)
                    binding.imgPhoto.setImageBitmap(ivkeBitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this,
                        "Ошибка. Воспользуйтесь камерой",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                ShowAlert(this, "Ошибка при выборе фото")
                imageUri = null
            }
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
        private const val FILENAME_FORMAT = "_yyyyMMdd_HHmmss"
        const val MY_PERMISSIONS_REQUEST_LOCATION = 1
        const val MY_PERMISSIONS_REQUEST_CAMERA = 2
        fun newIntent(context: Context) = Intent(context, AddMediaActivity::class.java)
    }
}