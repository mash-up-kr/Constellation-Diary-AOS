package com.mashup.telltostar.ui.starlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import org.jetbrains.anko.find

class StarListAdapter : RecyclerView.Adapter<StarListAdapter.StarListViewHolder>() {
    private var nameList : ArrayList<String> = arrayListOf()
    private var imageList : ArrayList<Int> = arrayListOf()
    private lateinit var mListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarListViewHolder {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_star_list, parent, false)
        return StarListViewHolder(convertView)
    }

    override fun getItemCount(): Int = nameList.size

    override fun onBindViewHolder(holder: StarListViewHolder, position: Int){
        holder.starName.text = nameList[position]
        holder.starImage.setImageResource(imageList[position])

        holder.starBox.setOnClickListener{
            if(position != RecyclerView.NO_POSITION){
                if (mListener != null) {
                    mListener.onItemClick(it, nameList[position]) ;
                }
            }
        }
    }

    fun setNamge(nameList:ArrayList<String>){
        this.nameList = nameList
    }
    fun setImage(imageList : ArrayList<Int>){
        this.imageList = imageList
    }

    fun setOnItemClickListener(listener : OnItemClickListener){
        this.mListener = listener
    }

    interface OnItemClickListener{
        fun onItemClick(view: View,name :String)
    }

    class StarListViewHolder(view: View):RecyclerView.ViewHolder(view){
        val starImage : ImageView = view.find(R.id.listStarImageIV) as ImageView
        val starName : TextView = view.find(R.id.listStarNameTV) as TextView
        val starBox : LinearLayout = view.find(R.id.listStarLL) as LinearLayout


    }
}