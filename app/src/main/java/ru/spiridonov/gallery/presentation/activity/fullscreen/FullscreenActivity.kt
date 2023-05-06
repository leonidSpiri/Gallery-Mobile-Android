package ru.spiridonov.gallery.presentation.activity.fullscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.databinding.ActivityFullscreenBinding
import ru.spiridonov.gallery.presentation.viewmodels.ViewModelFactory
import ru.spiridonov.gallery.utils.ImageUtils
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
        parseIntent()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.media.observe(this) {
            binding.media = it
            it.photoFile?.let { photo ->
                val bitmap = ImageUtils.getResizedBitmap(photo, 2048)
                binding.fullImage.setImageBitmap(bitmap)
            }
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(CONTENT_TYPE))
            throw RuntimeException("Content mode is absent")
        val buff = intent.getStringExtra(CONTENT_TYPE)
        if (buff != PHOTO_TYPE && buff != VIDEO_TYPE)
            throw RuntimeException("REGISTER_TYPE was not found. REGISTER_TYPE = $buff")
        if (buff == PHOTO_TYPE) {
            val mediaId = intent.getStringExtra(PHOTO_TYPE) ?: return
            viewModel.setMediaId(mediaId)
        }
    }

    companion object {
        private const val CONTENT_TYPE = "content_type"
        private const val PHOTO_TYPE = "photo"
        private const val VIDEO_TYPE = "video"

        fun newIntentImage(context: Context, mediaId: String): Intent {
            val intent = Intent(context, FullscreenActivity::class.java)
            intent.putExtra(CONTENT_TYPE, PHOTO_TYPE)
            intent.putExtra(PHOTO_TYPE, mediaId)
            return intent
        }
    }
}