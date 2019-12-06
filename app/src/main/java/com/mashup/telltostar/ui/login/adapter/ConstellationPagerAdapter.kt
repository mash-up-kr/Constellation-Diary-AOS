package com.mashup.telltostar.ui.login.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Constellation
import kotlinx.android.synthetic.main.item_constellation.view.*

class ConstellationPagerAdapter(private val mConstellations: List<Constellation>) :
    RecyclerView.Adapter<ConstellationPagerAdapter.ConstellationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstellationViewHolder =
        ConstellationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_constellation,
                parent,
                false
            )
        )

    override fun getItemCount() = mConstellations.size

    override fun onBindViewHolder(holder: ConstellationViewHolder, position: Int) {
        holder.bind(mConstellations[position])
    }

    class ConstellationViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val mConstellationNameTextView: TextView = view.constellationName

        fun bind(constellation: Constellation) {
            mConstellationNameTextView.text = constellation.mName
        }
    }
}