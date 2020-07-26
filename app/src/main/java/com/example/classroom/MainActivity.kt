package com.example.classroom

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFontFamily()

        setAnimation()

        setEvent()
    }

    fun setEvent() {
        ivBottom.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft,R.anim.fhelper)
        }
    }

    fun setFontFamily() {
        val logox: Typeface = Typeface.createFromAsset(assets, "fonts/Fredoka.ttf")
        val mlight: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratLight.ttf")
        val mmedium: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratMedium.ttf")
        val mregular: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratRegular.ttf")

        ivLogo.typeface = logox
        ivSubtitle.typeface = mlight
        ivBtn.typeface = mmedium
    }

    fun setAnimation() {
        val smallTobig: Animation = AnimationUtils.loadAnimation(this, R.anim.smalltobig)
        val traX: Float = 400.toFloat()

        ivSplash.startAnimation(smallTobig)

        ivLogo.translationX = traX
        ivSubtitle.translationX = traX
        ivBottom.translationX = traX

        ivLogo.alpha = 0.toFloat()
        ivSubtitle.alpha = 0.toFloat()
        ivBottom.alpha = 0.toFloat()

        ivLogo.animate().translationX(0.toFloat()).alpha(1.toFloat()).setDuration(800)
            .setStartDelay(500).start()
        ivSubtitle.animate().translationX(0.toFloat()).alpha(1.toFloat()).setDuration(800)
            .setStartDelay(700).start()
        ivBottom.animate().translationX(0.toFloat()).alpha(1.toFloat()).setDuration(800)
            .setStartDelay(1000).start()
    }
}
