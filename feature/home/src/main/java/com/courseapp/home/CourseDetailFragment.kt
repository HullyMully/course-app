package com.courseapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.courseapp.home.databinding.FragmentCourseDetailBinding

class CourseDetailFragment : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = requireArguments()
        val title = args.getString(ARG_TITLE).orEmpty()
        val description = args.getString(ARG_DESCRIPTION).orEmpty()
        val price = args.getString(ARG_PRICE).orEmpty()
        val rate = args.getString(ARG_RATE).orEmpty()
        val date = args.getString(ARG_DATE).orEmpty()
        val imageUrl = args.getString(ARG_IMAGE_URL)
        val isFavorite = args.getBoolean(ARG_IS_FAVORITE, false)

        binding.title.text = title
        binding.description.text = description
        binding.priceLabel.text = getString(R.string.price_format, price)
        binding.rating.text = rate
        binding.rating.setCompoundDrawablesRelativeWithIntrinsicBounds(
            com.courseapp.core.R.drawable.ic_star_green, 0, 0, 0
        )
        binding.rating.compoundDrawablePadding = 4
        binding.date.text = date

        binding.favoriteButton.setImageResource(
            if (isFavorite) com.courseapp.core.R.drawable.ic_favorite_filled
            else com.courseapp.core.R.drawable.ic_favorite_border
        )

        loadCourseImage(title, imageUrl)
        loadAuthorAvatar()

        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        binding.startCourseButton.setOnClickListener {
            Toast.makeText(requireContext(), R.string.feature_unavailable, Toast.LENGTH_SHORT).show()
        }
        binding.goToPlatformButton.setOnClickListener {
            Toast.makeText(requireContext(), R.string.feature_unavailable, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCourseImage(title: String, imageUrl: String?) {
        if (!imageUrl.isNullOrBlank()) {
            Glide.with(this).load(imageUrl).transform(CenterCrop()).into(binding.courseImage)
        } else {
            val resId = com.courseapp.core.util.ImageUtils.resolveLocalCourseImage(
                title, resources, requireContext().packageName
            )
            if (resId != 0) {
                Glide.with(this).load(resId).transform(CenterCrop()).into(binding.courseImage)
            }
        }
    }

    private fun loadAuthorAvatar() {
        val avatarResId = resources.getIdentifier("author_merion", "drawable", requireContext().packageName)
        if (avatarResId != 0) {
            Glide.with(this).load(avatarResId).circleCrop().into(binding.authorAvatar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_TITLE = "arg_title"
        const val ARG_DESCRIPTION = "arg_description"
        const val ARG_PRICE = "arg_price"
        const val ARG_RATE = "arg_rate"
        const val ARG_DATE = "arg_date"
        const val ARG_IMAGE_URL = "arg_image_url"
        const val ARG_IS_FAVORITE = "arg_is_favorite"
    }
}
