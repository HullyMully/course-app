package com.courseapp

import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.courseapp.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityOnboardingBinding
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private val chipViews = mutableListOf<View>()
    private var parallaxBaseX = 0f
    private var parallaxBaseY = 0f
    private var hasBaseline = false
    private val maxParallaxDp = 15f

    private data class ChipSpec(
        val text: String,
        val isGreen: Boolean,
        val xFraction: Float,
        val yFraction: Float,
        val rotation: Float
    )

    private val chips = listOf(
        ChipSpec("1С Администрирование", false, -0.08f, 0.02f, -3f),
        ChipSpec("RabbitMQ", true, 0.42f, 0.00f, 5f),
        ChipSpec("Трафик", false, 0.78f, 0.03f, -2f),

        ChipSpec("Контент маркетинг", false, -0.05f, 0.16f, 2f),
        ChipSpec("B2B маркетинг", false, 0.38f, 0.17f, -4f),
        ChipSpec("Google Аналитика", false, 0.72f, 0.15f, 3f),

        ChipSpec("UX исследователь", false, -0.03f, 0.32f, -2f),
        ChipSpec("Веб-аналитика", false, 0.42f, 0.33f, 4f),
        ChipSpec("Big Data", true, 0.80f, 0.30f, -6f),

        ChipSpec("Дизайн", false, -0.06f, 0.48f, 5f),
        ChipSpec("Веб-дизайн", false, 0.22f, 0.49f, -3f),
        ChipSpec("Cinema 4D", false, 0.50f, 0.47f, 2f),
        ChipSpec("Промпт инженер", false, 0.75f, 0.50f, -4f),

        ChipSpec("Three.js", true, 0.05f, 0.65f, 6f),
        ChipSpec("Парсинг", false, 0.38f, 0.67f, -2f),
        ChipSpec("Python-разработка", false, 0.65f, 0.66f, 3f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("courseapp_prefs", MODE_PRIVATE)
        if (prefs.getBoolean("onboarding_shown", false)) {
            goToLogin()
            return
        }

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as? SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        binding.chipsContainer.post { layoutChips() }

        binding.continueButton.setOnClickListener {
            prefs.edit().putBoolean("onboarding_shown", true).apply()
            goToLogin()
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
        hasBaseline = false
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]

        if (!hasBaseline) {
            parallaxBaseX = x
            parallaxBaseY = y
            hasBaseline = true
            return
        }

        val dx = x - parallaxBaseX
        val dy = y - parallaxBaseY

        val maxPx = dpToPx(maxParallaxDp.toInt()).toFloat()

        for ((i, view) in chipViews.withIndex()) {
            val depth = 0.6f + (i % 3) * 0.2f
            val targetX = -dx / 9.8f * maxPx * depth
            val targetY = dy / 9.8f * maxPx * depth

            view.translationX = view.translationX + (targetX - view.translationX) * 0.1f
            view.translationY = view.translationY + (targetY - view.translationY) * 0.1f
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun layoutChips() {
        val container = binding.chipsContainer
        val cw = container.width
        val ch = container.height

        for (spec in chips) {
            val chip = createChipView(spec.text, spec.isGreen)
            chip.rotation = spec.rotation

            chip.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val chipW = chip.measuredWidth

            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lp.leftMargin = (spec.xFraction * cw).toInt() - chipW / 4
            lp.topMargin = (spec.yFraction * ch).toInt()

            container.addView(chip, lp)
            chipViews.add(chip)
        }
    }

    private fun createChipView(text: String, isGreen: Boolean): TextView {
        val dp12 = dpToPx(12)
        val dp16 = dpToPx(16)

        return TextView(this).apply {
            this.text = text
            textSize = 14f
            setPadding(dp16, dp12, dp16, dp12)
            gravity = Gravity.CENTER

            if (isGreen) {
                setBackgroundResource(R.drawable.bg_chip_green)
                setTextColor(Color.BLACK)
            } else {
                setBackgroundResource(R.drawable.bg_chip_dark)
                setTextColor(Color.WHITE)
            }
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, Class.forName("com.courseapp.login.LoginActivity")))
        finish()
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
        ).toInt()
    }
}
