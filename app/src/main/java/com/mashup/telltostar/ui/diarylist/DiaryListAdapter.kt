package com.mashup.telltostar.ui.diarylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.source.remote.response.SimpleDiary
import timber.log.Timber

class DiaryListAdapter: RecyclerView.Adapter<DiaryListContentsViewHolder>(){
    var dataList : List<SimpleDiary> = listOf()
    private lateinit var clickListener : OnItemClickListener
    private lateinit var longClickListener : OnItemLongClickListener



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListContentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list, parent, false)
        return DiaryListContentsViewHolder(view)
    }


    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DiaryListContentsViewHolder, position: Int) {
        dataList[position].let {data ->
            var diary = Temp_diary(data, false, false)
            holder.bind(diary)
            holder.diaryBox.setOnClickListener {
                if(position != RecyclerView.NO_POSITION){
                    if (clickListener != null) {
                        clickListener.onItemClick(data.id)
                    }
                }
            }
            holder.diaryBox.setOnLongClickListener {
                if(position != RecyclerView.NO_POSITION){
                    if (longClickListener != null) {
                        longClickListener.onItemLongClick(data.id)
                    }
                }
                return@setOnLongClickListener true
            }
        }
    }

    fun setData(item: List<SimpleDiary>){
        dataList = item
        notifyDataSetChanged()
        Timber.d(dataList.toString(),"")
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.longClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(id : Int)
    }
    interface OnItemLongClickListener {
        fun onItemLongClick(id : Int)
    }
}