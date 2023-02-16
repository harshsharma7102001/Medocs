package com.entertainment.medkit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager
import com.entertainment.medkit.databinding.ActivityWalkthroughBinding

class Walkthrough : AppCompatActivity() {
    private lateinit var binding:ActivityWalkthroughBinding
    private lateinit var sliderAdapter: SliderAdapter
    private var dots: Array<TextView?>? = null
    private lateinit var layouts: Array<Int>
    var firstStart:Boolean = true
    private val sliderChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            if (position == layouts.size.minus(1)) {
                binding.nextBtn.visibility = View.INVISIBLE
                binding.skipBtn.visibility = View.INVISIBLE
                binding.startBtn.visibility = View.VISIBLE
            } else {
                binding.nextBtn.visibility = View.VISIBLE
                binding.skipBtn.visibility = View.VISIBLE
                binding.startBtn.visibility = View.INVISIBLE
//                binding.startBtn.isInvisible = true
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val pref = getSharedPreferences("prefs", MODE_PRIVATE)
        firstStart = pref.getBoolean("firstStart",true)
        init()
        dataSet()
        interactions()
    }
    private fun init() {
        /** Layouts of the three onBoarding Screens.
         *  Add more layouts if you wish.
         **/
        layouts = arrayOf(R.layout.screen_one,
            R.layout.screen_two)

        sliderAdapter = SliderAdapter(this, layouts)
    }

    private fun dataSet() {
        /**
         * Adding bottom dots
         * */
        addBottomDots(0)

        binding.slider.apply {
            adapter = sliderAdapter
            addOnPageChangeListener(sliderChangeListener)
        }
    }

    private fun interactions() {
        binding.skipBtn.setOnClickListener {
            // Launch login screen
            navigateToLogin()
        }
        binding.startBtn.setOnClickListener {
            // Launch login screen
            navigateToLogin()
        }
        binding.nextBtn.setOnClickListener {
            /**
             *  Checking for last page, if last page
             *  login screen will be launched
             * */
            val current = getCurrentScreen(+1)
            if (current < layouts.size) {
                /**
                 * Move to next screen
                 * */
                binding.slider.currentItem = current
            } else {
                // Launch login screen
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        AppPrefs(this).setFirstTimeLaunch(false)
        if (firstStart) {
            val pref = getSharedPreferences("prefs", MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("firstStart", false)
            editor.apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        finish()
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)

        binding.dotsLayout.removeAllViews()
        for (i in 0 until dots!!.size) {
            dots!![i] = TextView(this)
            dots!![i]?.text = Html.fromHtml("&#8226;")
            dots!![i]?.textSize = 35f
            dots!![i]?.setTextColor(resources.getColor(R.color.black))
            binding.dotsLayout.addView(dots!![i])
        }

        if (dots!!.isNotEmpty()) {
            dots!![currentPage]?.setTextColor(resources.getColor(R.color.indigo))
        }
    }

    private fun getCurrentScreen(i: Int): Int = binding.slider.currentItem.plus(i)
    override fun onStart() {
        if (firstStart==false){
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        }
        super.onStart()
    }
}