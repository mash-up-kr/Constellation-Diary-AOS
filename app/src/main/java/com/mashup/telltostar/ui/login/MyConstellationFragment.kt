package com.mashup.telltostar.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

import com.mashup.telltostar.R
import com.mashup.telltostar.data.Constellation
import com.mashup.telltostar.ui.myconstellation.adapter.ConstellationPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_constellation.*
import kotlinx.android.synthetic.main.fragment_my_constellation.view.*
import timber.log.Timber

class MyConstellationFragment : Fragment() {
    private val mPageChangeCallback: ViewPager2.OnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        val rootView = inflater.inflate(R.layout.fragment_my_constellation, container, false)

        rootView.vp_constellation.apply {
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

        return rootView
    }

    private fun getSampleConstellationList() =
        mutableListOf(
            Constellation("양자리"),
            Constellation("황소자리"),
            Constellation("물병자리"),
            Constellation("무엇자리")
        )

    override fun onDestroyView() {
        vp_constellation.unregisterOnPageChangeCallback(mPageChangeCallback)

        super.onDestroyView()
    }
}
