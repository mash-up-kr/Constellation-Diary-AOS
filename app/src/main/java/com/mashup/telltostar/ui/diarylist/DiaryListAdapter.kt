package com.mashup.telltostar.ui.diarylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.source.remote.response.SimpleDiary
import timber.log.Timber

class DiaryListAdapter: RecyclerView.Adapter<DiaryListContentsViewHolder>(){
    var dataList : List<SimpleDiary> = listOf()
    private lateinit var checkNumListener : OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListContentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list, parent, false)
        return DiaryListContentsViewHolder(view)
    }


    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DiaryListContentsViewHolder, position: Int) {
        dataList[position].let {
            var diary = Temp_diary(it, false, false)
            holder.bind(diary)
            holder.changeVisible()
            holder.diarySelect.setOnCheckedChangeListener(null)
            holder.diarySelect.setOnCheckedChangeListener { buttonView, isChecked ->
                if (position != RecyclerView.NO_POSITION) {
                    if (checkNumListener != null) {
                        checkNumListener.onItemClick(isChecked)
                    }
                }

            }
        }
    }

    override fun onBindViewHolder(holder: DiaryListContentsViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (i in 1..(itemCount-1)) {
                var check = payloads[0]
                if (check is Boolean) {
                    holder.diary.isVisible = check
                    holder.changeVisible()
                    if (check == false) {
                        holder.diary.isChecked = false
                        holder.diarySelect.isChecked = false
                    }
                }
                if (check is String) {
                    if (holder.diary.isChecked) {
                    }
                    if(check.equals("set")){
                        var diary = Temp_diary(dataList[i],false,false)
                        holder.bind(diary)

                    }
                }

            }
        }
    }

    fun setData(item: List<SimpleDiary>){
        dataList = item
        notifyDataSetChanged()
        Timber.d(dataList.toString(),"")
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.checkNumListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(isChecked: Boolean)
    }
}