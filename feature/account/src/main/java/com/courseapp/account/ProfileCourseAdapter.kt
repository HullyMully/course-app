package com.courseapp.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.courseapp.data.db.FavoriteEntity
import com.courseapp.account.databinding.ItemProfileCourseBinding

class ProfileCourseAdapter(
    private val onCardClick: (FavoriteEntity) -> Unit
) : ListAdapter<FavoriteEntity, ProfileCourseAdapter.VH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemProfileCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b, onCardClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemProfileCourseBinding,
        private val onCardClick: (FavoriteEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FavoriteEntity) {
            val ctx = binding.root.context
            binding.courseTitle.text = item.title

            binding.rating.text = "${item.rate}"
            binding.rating.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_star_green, 0, 0, 0
            )
            binding.rating.compoundDrawablePadding = 6
            binding.date.text = item.publishDate

            val totalLessons = when {
                item.title.contains("3D", ignoreCase = true) -> 44
                item.title.contains("Java", ignoreCase = true) -> 48
                else -> 36
            }
            val completedLessons = when {
                item.title.contains("3D", ignoreCase = true) -> 22
                item.title.contains("Java", ignoreCase = true) -> 15
                else -> 10
            }
            val progress = (completedLessons * 100) / totalLessons

            binding.progressText.text = "$progress%"
            binding.progressBar.progress = progress
            binding.lessonsCount.text = ctx.getString(R.string.lessons_format, completedLessons, totalLessons)

            val imageUrl = item.imageUrl
            if (!imageUrl.isNullOrBlank()) {
                Glide.with(binding.courseImage).load(imageUrl).transform(CenterCrop()).into(binding.courseImage)
            } else {
                val name = when {
                    item.title.contains("Java", ignoreCase = true) -> "course_java"
                    item.title.contains("3D", ignoreCase = true) || item.title.contains("дженералист", ignoreCase = true) -> "course_3d"
                    else -> "course_x"
                }
                val resId = ctx.resources.getIdentifier(name, "drawable", ctx.packageName)
                if (resId != 0) {
                    Glide.with(binding.courseImage).load(resId).transform(CenterCrop()).into(binding.courseImage)
                } else {
                    binding.courseImage.setImageDrawable(null)
                }
            }

            itemView.setOnClickListener { onCardClick(item) }
        }
    }

    object Diff : DiffUtil.ItemCallback<FavoriteEntity>() {
        override fun areItemsTheSame(a: FavoriteEntity, b: FavoriteEntity) = a.id == b.id
        override fun areContentsTheSame(a: FavoriteEntity, b: FavoriteEntity) = a == b
    }
}
