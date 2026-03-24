package com.courseapp.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.courseapp.core.util.ImageUtils
import com.courseapp.domain.model.Course
import com.courseapp.account.databinding.ItemProfileCourseBinding

class ProfileCourseAdapter(
    private val onCardClick: (Course) -> Unit
) : ListAdapter<Course, ProfileCourseAdapter.VH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemProfileCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding, onCardClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemProfileCourseBinding,
        private val onCardClick: (Course) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Course) {
            val ctx = binding.root.context
            binding.courseTitle.text = item.title
            binding.rating.text = "${item.rate}"
            binding.rating.setCompoundDrawablesRelativeWithIntrinsicBounds(
                com.courseapp.core.R.drawable.ic_star_green, 0, 0, 0
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
            binding.lessonsCount.text = ctx.getString(
                R.string.lessons_format, completedLessons, totalLessons
            )

            val imageUrl = item.imageUrl
            if (!imageUrl.isNullOrBlank()) {
                Glide.with(binding.courseImage).load(imageUrl).transform(CenterCrop()).into(binding.courseImage)
            } else {
                val resId = ImageUtils.resolveLocalCourseImage(item.title, ctx.resources, ctx.packageName)
                if (resId != 0) {
                    Glide.with(binding.courseImage).load(resId).transform(CenterCrop()).into(binding.courseImage)
                } else {
                    binding.courseImage.setImageDrawable(null)
                }
            }

            itemView.setOnClickListener { onCardClick(item) }
        }
    }

    object Diff : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(a: Course, b: Course) = a.id == b.id
        override fun areContentsTheSame(a: Course, b: Course) = a == b
    }
}
