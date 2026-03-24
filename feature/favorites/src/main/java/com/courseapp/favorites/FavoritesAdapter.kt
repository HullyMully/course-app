package com.courseapp.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.courseapp.core.util.ImageUtils
import com.courseapp.domain.model.Course
import com.courseapp.favorites.databinding.ItemFavoriteBinding

class FavoritesAdapter(
    private val onRemove: (String) -> Unit,
    private val onCardClick: (Course) -> Unit
) : ListAdapter<Course, FavoritesAdapter.VH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding, onRemove, onCardClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemFavoriteBinding,
        private val onRemove: (String) -> Unit,
        private val onCardClick: (Course) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Course) {
            val ctx = binding.root.context
            binding.title.text = item.title
            binding.description.text = item.description
            binding.description.maxLines = 2
            binding.price.text = "${item.price} ₽"
            binding.rating.text = "${item.rate}"
            binding.rating.setCompoundDrawablesRelativeWithIntrinsicBounds(
                com.courseapp.core.R.drawable.ic_star_green, 0, 0, 0
            )
            binding.rating.compoundDrawablePadding = 6
            binding.date.text = item.publishDate
            binding.favoriteButton.setOnClickListener { onRemove(item.id) }
            itemView.setOnClickListener { onCardClick(item) }

            val imageUrl = item.imageUrl
            if (!imageUrl.isNullOrBlank()) {
                Glide.with(binding.image).load(imageUrl).transform(CenterCrop()).into(binding.image)
            } else {
                val resId = ImageUtils.resolveLocalCourseImage(item.title, ctx.resources, ctx.packageName)
                if (resId != 0) {
                    Glide.with(binding.image).load(resId).transform(CenterCrop()).into(binding.image)
                } else {
                    binding.image.setImageDrawable(null)
                }
            }
        }
    }

    object Diff : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(a: Course, b: Course) = a.id == b.id
        override fun areContentsTheSame(a: Course, b: Course) = a == b
    }
}
