package ru.spiridonov.gallery.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import ru.spiridonov.gallery.R
import ru.spiridonov.gallery.databinding.EachMediaItemBinding
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.utils.ImageUtils
import javax.inject.Inject

class MediaItemAdapter @Inject constructor() :
    ListAdapter<Media, MediaItemViewHolder>(MediaItemDiffCallback) {
    var onItemClickListener: ((Media) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val layoutID =
            when (viewType) {
                MEDIA_ITEM -> R.layout.each_media_item
                else -> throw RuntimeException("Unknown view type: $viewType")
            }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutID,
            parent,
            false
        )
        return MediaItemViewHolder(binding)
    }

    override fun getItemViewType(position: Int) =
        MEDIA_ITEM


    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            when (this) {
                is EachMediaItemBinding -> {
                    mediaItem = item
                    item.photoFile?.let { photo ->
                        val bitmap = ImageUtils.getResizedBitmap(photo, 256)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
            root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }

        }
    }

    companion object {
        private const val MEDIA_ITEM = 0
    }
}