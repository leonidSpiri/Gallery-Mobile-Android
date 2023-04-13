package ru.spiridonov.gallery.presentation.ui.fullscreen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.databinding.ActivityFullscreenBinding
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject


class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding
    private val component by lazy {
        (application as GalleryApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FullScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[FullScreenViewModel::class.java]
        binding.fullImage.setImageBitmap(null)
        binding.fullImage.setImageBitmap(parseIntent()?.photoFile)
    }


    private fun parseIntent(): Media? {
        if (!intent.hasExtra(CONTENT_TYPE))
            throw RuntimeException("Content mode is absent")
        val buff = intent.getStringExtra(CONTENT_TYPE)
        if (buff != PHOTO_TYPE && buff != VIDEO_TYPE)
            throw RuntimeException("REGISTER_TYPE was not found. REGISTER_TYPE = $buff")
        if (buff == PHOTO_TYPE) {
            return when {
                Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra(
                    PHOTO_TYPE,
                    Media::class.java
                )
                    ?: throw IllegalArgumentException("No feeder item")
                else -> @Suppress("DEPRECATION")
                intent.getParcelableExtra(PHOTO_TYPE)
                    ?: throw IllegalArgumentException("No feeder item")
            }
        }
        return null
    }


    companion object {
        private const val CONTENT_TYPE = "content_type"
        private const val PHOTO_TYPE = "photo"
        private const val VIDEO_TYPE = "video"

        fun newIntentImage(context: Context, media: Media): Intent {
            val intent = Intent(context, FullscreenActivity::class.java)
            intent.putExtra(CONTENT_TYPE, PHOTO_TYPE)
            intent.putExtra(PHOTO_TYPE, media)
            return intent
        }
    }
}