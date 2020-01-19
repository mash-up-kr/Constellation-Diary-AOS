package com.mashup.telltostar.ui.myconstellation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.myconstellation.adapter.ConstellationAdapter
import com.mashup.telltostar.util.ConstellationUtil
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_my_constellation.*
import org.jetbrains.anko.toast

class MyConstellationActivity : AppCompatActivity(),
    DiscreteScrollView.ScrollStateChangeListener<ConstellationAdapter.ConstellationViewHolder>,
    DiscreteScrollView.OnItemChangedListener<ConstellationAdapter.ConstellationViewHolder> {

    private val adapter by lazy {
        ConstellationAdapter(
            ConstellationUtil.getConstellations(this.resources)
        )
    }

    private val constellationAdapter
            by lazy { InfiniteScrollAdapter.wrap(adapter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_constellation)

        initButton()
        initCustomView()
    }

    private fun initButton() {
        btnMyConstellationStart.setOnClickListener {
            val realPosition = constellationAdapter.getRealPosition(customScrollView.currentItem)
            toast(adapter.getConstellation(realPosition))
        }
    }

    private fun initCustomView() {

        with(customScrollView) {
            //infinite scroll
            adapter = constellationAdapter

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
        //holder.hideText()
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
        //holder?.showText()
    }

}
