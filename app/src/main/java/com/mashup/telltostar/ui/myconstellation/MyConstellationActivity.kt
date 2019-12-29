package com.mashup.telltostar.ui.myconstellation

import android.animation.ArgbEvaluator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Constellation
import com.mashup.telltostar.ui.myconstellation.adapter.ConstellationAdapter
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_my_constellation.*
import org.jetbrains.anko.toast

class MyConstellationActivity : AppCompatActivity(),
    DiscreteScrollView.ScrollStateChangeListener<ConstellationAdapter.ConstellationViewHolder>,
    DiscreteScrollView.OnItemChangedListener<ConstellationAdapter.ConstellationViewHolder> {

    private val constellationAdapter by lazy { ConstellationAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_constellation)

        initButton()
        initCustomView()

        constellationAdapter.replaceAll(
            listOf(
                Constellation("별자리1"),
                Constellation("별자리2"),
                Constellation("별자리3"),
                Constellation("별자리4"),
                Constellation("별자리5"),
                Constellation("별자리6")
            )
        )
    }

    private fun initButton() {
        btnMyConstellationStart.setOnClickListener {
            toast("next")
        }
    }

    private val evaluator = ArgbEvaluator()
    private val currentOverlayColor by lazy {
        ContextCompat.getColor(
            this,
            R.color.constellationCurrentItemOverlay
        )
    }
    private val overlayColor by lazy {
        ContextCompat.getColor(
            this,
            R.color.constellationItemOverlay
        )
    }

    private fun initCustomView() {

        with(customScrollView) {
            //adapter = constellationAdapter

            //infinite scroll
            adapter = InfiniteScrollAdapter.wrap(constellationAdapter)

            setOffscreenItems(3)
            setOverScrollEnabled(false)

            setItemTransformer(
                ScaleTransformer.Builder()
                    .setMaxScale(1.0f)
                    .setMinScale(0.8f)
                    .setPivotX(Pivot.X.CENTER)
                    .setPivotY(Pivot.Y.CENTER)
                    .build()
            )

            addScrollStateChangeListener(this@MyConstellationActivity)
            addOnItemChangedListener(this@MyConstellationActivity)
        }
    }


    override fun onScrollStart(
        holder: ConstellationAdapter.ConstellationViewHolder,
        adapterPosition: Int
    ) {
        holder.hideText()
    }

    override fun onScroll(
        currentPosition: Float,
        currentIndex: Int,
        newIndex: Int,
        currentHolder: ConstellationAdapter.ConstellationViewHolder?,
        newCurrent: ConstellationAdapter.ConstellationViewHolder?
    ) {
        if (currentHolder != null && newCurrent != null) {
            val position = Math.abs(currentPosition)
            //currentHolder.setOverlayColor(interpolate(position, currentOverlayColor, overlayColor))
            //newCurrent.setOverlayColor(interpolate(position, overlayColor, currentOverlayColor))
        }
    }

    override fun onScrollEnd(
        holder: ConstellationAdapter.ConstellationViewHolder,
        adapterPosition: Int
    ) {

    }


    override fun onCurrentItemChanged(
        holder: ConstellationAdapter.ConstellationViewHolder?,
        adapterPosition: Int
    ) {
        //viewHolder?.setOverlayColor(currentOverlayColor)
        holder?.showText()
    }

    private fun interpolate(fraction: Float, c1: Int, c2: Int): Int {
        return evaluator.evaluate(fraction, c1, c2) as Int
    }
}
