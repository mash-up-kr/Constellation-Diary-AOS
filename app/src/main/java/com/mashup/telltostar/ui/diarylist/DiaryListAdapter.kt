package com.mashup.telltostar.ui.diarylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Diary

class DiaryListAdapter: RecyclerView.Adapter<DiaryListAdapter.DiaryListViewHolder>() {
    private var diaryList :ArrayList<Diary> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListViewHolder {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list, parent, false)
        return DiaryListViewHolder(convertView)
    }

    override fun getItemCount(): Int = diaryList.size + 1

    override fun onBindViewHolder(holder: DiaryListViewHolder, position: Int) {
        diaryList[position].let{item ->
            holder.let{
                it.diaryContent.text = item.contents
                it.diaryDate.text = item.date.toString()
                it.diaryDay.text = item.date.toString()
                it.diaryTitle.text = item.title
            }
        }
    }

    fun setItem(item:ArrayList<Diary>){
        diaryList.addAll(item)
    }

    class DiaryListViewHolder(view: View):RecyclerView.ViewHolder(view){
        val diaryDate :TextView = view.findViewById(R.id.diaryListDateTV) as TextView
        val diaryDay : TextView = view.findViewById(R.id.diaryListDayTV) as TextView
        val diaryTitle : TextView = view.findViewById(R.id.diaryListTitleTV) as TextView
        val diaryContent : TextView = view.findViewById(R.id.diaryListContentTV) as TextView

    }

    companion object{
        private val TYPE_HEADER = 0
        private  val TYPR_ITEM = 1
    }
}