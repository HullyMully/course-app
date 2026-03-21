package com.courseapp.home

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.courseapp.core.util.ImageUtils
import com.courseapp.home.databinding.ItemCourseBinding
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

private fun courseDelegate(
    onFavoriteClick: (CourseItem) -> Unit,
    onCardClick: (CourseItem) -> Unit
) = adapterDelegateViewBinding<CourseItem, CourseItem, ItemCourseBinding>(
    { inflater, parent -> ItemCourseBinding.inflate(inflater, parent, false) }
) {
    binding.favoriteButton.setOnClickListener { onFavoriteClick(item) }
    itemView.setOnClickListener { onCardClick(item) }

    bind {
        binding.title.text = item.dto.title
        binding.description.text = item.dto.text
        binding.price.text = context.getString(R.string.price_format, item.dto.price)
        binding.rating.text = "${item.dto.rate}"
        binding.rating.setCompoundDrawablesRelativeWithIntrinsicBounds(
            com.courseapp.core.R.drawable.ic_star_green, 0, 0, 0
        )
        binding.rating.compoundDrawablePadding = 6
        binding.date.text = item.dto.publishDate
        binding.favoriteButton.setImageResource(
            if (item.isFavorite) com.courseapp.core.R.drawable.ic_favorite_filled
            else com.courseapp.core.R.drawable.ic_favorite_border
        )

        val imageUrl = item.dto.imageUrl
        if (!imageUrl.isNullOrBlank()) {
            Glide.with(binding.image).load(imageUrl).transform(CenterCrop()).into(binding.image)
        } else {
            val localRes = ImageUtils.resolveLocalCourseImage(
                item.dto.title, context.resources, context.packageName
            )
            if (localRes != 0) {
                Glide.with(binding.image).load(localRes).transform(CenterCrop()).into(binding.image)
            } else {
                binding.image.setImageDrawable(null)
            }
        }
    }
}

class CourseListAdapter(
    onFavoriteClick: (CourseItem) -> Unit,
    onCardClick: (CourseItem) -> Unit
) : AsyncListDifferDelegationAdapter<CourseItem>(CourseItem.DIFF) {

    init {
        delegatesManager.addDelegate(courseDelegate(onFavoriteClick, onCardClick))
    }

    fun submitList(list: List<CourseItem>) {
        items = list
    }
}
