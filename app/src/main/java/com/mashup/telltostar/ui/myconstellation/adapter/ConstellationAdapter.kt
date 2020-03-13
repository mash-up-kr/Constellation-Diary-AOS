package com.mashup.telltostar.ui.myconstellation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.util.ConstellationUtil

class ConstellationAdapter(private val constellations: List<String>) :
    RecyclerView.Adapter<ConstellationAdapter.ConstellationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstellationViewHolder =
        ConstellationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_constellation,
                parent,
                false
            )
        )

    override fun getItemCount() = constellations.size

    override fun onBindViewHolder(holder: ConstellationViewHolder, position: Int) {
        holder.bind(constellations[position])
    }

    fun getConstellation(position: Int): String {
        return if (position >= 0 && constellations.size > position) {
            constellations[position]
        } else {
            "nothing"
        }
    }

    class ConstellationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val parent: ConstraintLayout = itemView.findViewById(R.id.cvItemConstellationParent)
        private val topIcon: ImageView = itemView.findViewById(R.id.ivItemConstellationIcon)
        private val topTitle: TextView = itemView.findViewById(R.id.tvItemConstellationTitle)
        private val topDate: TextView = itemView.findViewById(R.id.tvItemConstellationDate)

        private val constellationImage: ImageView = itemView.findViewById(R.id.ivItemConstellation)

        fun bind(constellation: String) {
            topIcon.setImageResource(ConstellationUtil.getIcon(itemView.resources, constellation))
            topTitle.text = constellation
            topDate.text = ConstellationUtil.getDate(itemView.resources, constellation)
            constellationImage.setImageResource(
                ConstellationUtil.getImg(
                    itemView.resources,
                    constellation
                )
            )
        }

        fun getTitle() = topTitle.text.toString()

        fun showLine() {
            parent.background =
                ContextCompat.getDrawable(itemView.context, R.drawable.constellation_selected)
        }

        fun hideLine() {
            parent.background =
                ContextCompat.getDrawable(itemView.context, R.drawable.constellation_unselected)
        }
    }
}