package com.courseapp.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.courseapp.home.databinding.ActivityCourseDetailBinding

class CourseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseDetailBinding

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_PRICE = "extra_price"
        const val EXTRA_RATE = "extra_rate"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_IMAGE_URL = "extra_image_url"
        const val EXTRA_IS_FAVORITE = "extra_is_favorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        val description = intent.getStringExtra(EXTRA_DESCRIPTION).orEmpty()
        val price = intent.getStringExtra(EXTRA_PRICE).orEmpty()
        val rate = intent.getDoubleExtra(EXTRA_RATE, 0.0)
        val date = intent.getStringExtra(EXTRA_DATE).orEmpty()
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val isFavorite = intent.getBooleanExtra(EXTRA_IS_FAVORITE, false)

        binding.title.text = title
        binding.description.text = description
        binding.priceLabel.text = getString(R.string.price_format, price)
        binding.rating.text = "$rate"
        binding.rating.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_star_green, 0, 0, 0
        )
        binding.rating.compoundDrawablePadding = 4
        binding.date.text = date

        binding.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        imageUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .transform(CenterCrop())
                .into(binding.courseImage)
        }

        binding.backButton.setOnClickListener { finish() }

        binding.startCourseButton.setOnClickListener {
            Toast.makeText(this, "Функция пока недоступна", Toast.LENGTH_SHORT).show()
        }
        binding.goToPlatformButton.setOnClickListener {
            Toast.makeText(this, "Функция пока недоступна", Toast.LENGTH_SHORT).show()
        }
    }
}
