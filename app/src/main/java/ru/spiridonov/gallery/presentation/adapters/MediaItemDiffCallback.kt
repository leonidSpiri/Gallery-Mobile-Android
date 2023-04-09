package ru.spiridonov.gallery.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.spiridonov.gallery.domain.entity.Media

object MediaItemDiffCallback : DiffUtil.ItemCallback<Media>() {
    override fun areItemsTheSame(oldItem: Media, newItem: Media) =
        oldItem.media_id == newItem.media_id

    override fun areContentsTheSame(oldItem: Media, newItem: Media) = oldItem == newItem
}