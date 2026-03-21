package com.courseapp.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.courseapp.data.db.FavoriteEntity
import com.courseapp.favorites.databinding.ItemFavoriteBinding

class FavoritesAdapter(
    private val onRemove: (String) -> Unit,
    private val onCardClick: (FavoriteEntity) -> Unit
) : ListAdapter<FavoriteEntity, FavoritesAdapter.VH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b, onRemove, onCardClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemFavoriteBinding,
        private val onRemove: (String) -> Unit,
        private val onCardClick: (FavoriteEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteEntity) {
            val ctx = binding.root.context
            binding.title.text = item.title
            binding.description.text = item.text
            binding.description.maxLines = 2
            binding.price.text = "${item.price} ₽"
            binding.rating.text = "${item.rate}"
            binding.rating.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_star_green, 0, 0, 0
            )
            binding.rating.compoundDrawablePadding = 6
            binding.date.text = item.publishDate
            binding.favoriteButton.setOnClickListener { onRemove(item.id) }
            itemView.setOnClickListener { onCardClick(item) }

            val imageUrl = item.imageUrl
            if (!imageUrl.isNullOrBlank()) {
                Glide.with(binding.image).load(imageUrl).transform(CenterCrop()).into(binding.image)
            } else {
                val name = when {
                    item.title.contains("Java", ignoreCase = true) -> "course_java"
                    item.title.contains("3D", ignoreCase = true) || item.title.contains("дженералист", ignoreCase = true) -> "course_3d"
                    else -> "course_x"
                }
                val resId = ctx.resources.getIdentifier(name, "drawable", ctx.packageName)
                if (resId != 0) {
                    Glide.with(binding.image).load(resId).transform(CenterCrop()).into(binding.image)
                } else {
                    binding.image.setImageDrawable(null)
                }
            }
        }
    }

    object Diff : DiffUtil.ItemCallback<FavoriteEntity>() {
        override fun areItemsTheSame(a: FavoriteEntity, b: FavoriteEntity) = a.id == b.id
        override fun areContentsTheSame(a: FavoriteEntity, b: FavoriteEntity) = a == b
    }
}
