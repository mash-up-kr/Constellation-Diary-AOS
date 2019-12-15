package com.mashup.telltostar.ui.myconstellation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Constellation
import com.mashup.telltostar.ui.myconstellation.adapter.ConstellationPagerAdapter
import kotlinx.android.synthetic.main.activity_my_constellation.*
import kotlinx.android.synthetic.main.toolbar.view.*

class MyConstellationActivity : AppCompatActivity() {
    private val mPageChangeCallback: ViewPager2.OnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_constellation)

        myConstellationToolbar.toolbarTextView.text = getString(R.string.title_my_constellation)

        vp_constellation.apply {
            adapter = ConstellationPagerAdapter(getSampleConstellationList())
            offscreenPageLimit = 3
            registerOnPageChangeCallback(mPageChangeCallback)
            setPageTransformer { page, position ->
                val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
                val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
                val offset = position * (2 * offsetPx + pageMarginPx)

                page.translationX = offset
            }
        }
    }

    private fun getSampleConstellationList() =
        mutableListOf(
            Constellation("양자리"),
            Constellation("황소자리"),
            Constellation("물병자리"),
            Constellation("무엇자리")
        )

    override fun onDestroy() {
        vp_constellation.unregisterOnPageChangeCallback(mPageChangeCallback)

        super.onDestroy()
    }
}
