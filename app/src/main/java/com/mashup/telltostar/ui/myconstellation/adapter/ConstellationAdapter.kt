package com.mashup.telltostar.ui.myconstellation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Constellation

class ConstellationAdapter :
    RecyclerView.Adapter<ConstellationAdapter.ConstellationViewHolder>() {

    private val items = mutableListOf<Constellation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstellationViewHolder =
        ConstellationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_constellation,
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ConstellationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun replaceAll(items: List<Constellation>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ConstellationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val container: CardView = itemView.findViewById(R.id.cvItemConstellationParent)

        private val topParent: LinearLayout =
            itemView.findViewById(R.id.llItemConstellationTextParent)
        private val topIcon: ImageView = itemView.findViewById(R.id.ivItemConstellationIcon)
        private val topTitle: TextView = itemView.findViewById(R.id.tvItemConstellationTitle)
        private val topDate: TextView = itemView.findViewById(R.id.tvItemConstellationDate)

        private val constellation: ImageView = itemView.findViewById(R.id.ivItemConstellation)

        fun bind(item: Constellation) {
            topIcon.setImageResource(R.drawable.star_ic_girl)
            topTitle.text = itemView.resources.getString(R.string.girl)
            topDate.text = itemView.resources.getString(R.string.date_girl)
            constellation.setImageResource(R.drawable.star_img_girl)
        }

        fun showText() {
            val parentHeight = constellation.height
            val scale = (parentHeight - topParent.height) / constellation.height.toFloat()

            constellation.pivotX = constellation.width * 0.5f
            constellation.pivotY = constellation.height * 1f
            constellation.animate()
                .scaleX(scale)
                .scaleY(scale)
                .withEndAction {
                    topParent.visibility = View.VISIBLE
                    constellation.alpha = 1f
                }
                .setDuration(200)
                .start()

        }

        fun hideText() {
            constellation.alpha = 0.5f
            topParent.visibility = View.INVISIBLE
            constellation.animate().scaleX(1.0f).scaleY(1.0f)
                .setDuration(200)
                .start()
        }
    }
}