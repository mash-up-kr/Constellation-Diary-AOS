package com.mashup.telltostar.ui.myconstellation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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

        private val topParent: LinearLayout =
            itemView.findViewById(R.id.llItemConstellationTextParent)
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

        fun showText() {
            val parentHeight = constellationImage.height
            val scale = (parentHeight - topParent.height) / constellationImage.height.toFloat()

            constellationImage.pivotX = constellationImage.width * 0.5f
            constellationImage.pivotY = constellationImage.height * 1f
            constellationImage.animate()
                .scaleX(scale)
                .scaleY(scale)
                .withEndAction {
                    topParent.visibility = View.VISIBLE
                    constellationImage.alpha = 1f
                }
                .setDuration(200)
                .start()

        }

        fun hideText() {
            constellationImage.alpha = 0.5f
            topParent.visibility = View.INVISIBLE
            constellationImage.animate().scaleX(1.0f).scaleY(1.0f)
                .setDuration(200)
                .start()
        }
    }
}