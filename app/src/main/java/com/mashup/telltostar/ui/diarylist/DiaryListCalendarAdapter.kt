package com.mashup.telltostar.ui.diarylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import org.jetbrains.anko.find

class DiaryListCalendarAdapter : RecyclerView.Adapter<DiaryListCalendarAdapter.DiaryListCalendarViewHolder>() {
    private var stampList : ArrayList<DataCalendar> = arrayListOf()
    private var startPosition : Int = 0
    private var lastDay : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListCalendarViewHolder {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list_calendar, parent, false)
        return DiaryListCalendarViewHolder(convertView)
    }

    override fun getItemCount() = stampList.size + startPosition

    override fun onBindViewHolder(holder: DiaryListCalendarViewHolder, position: Int) {
        if(position > startPosition-1){
            holder.calendarDay.text = (position-startPosition+1).toString()
            if(stampList[position-startPosition].stamp){
                holder.calendarStamp.visibility = View.VISIBLE
            }else{
                holder.calendarStamp.visibility = View.INVISIBLE
            }
        }
    }



    fun setDay(dayList : ArrayList<DataCalendar>,startPosition : Int,lastDay : Int){
        this.stampList = dayList
        this.startPosition = startPosition-1
        this.lastDay = lastDay
    }

    class DiaryListCalendarViewHolder(view: View):RecyclerView.ViewHolder(view){
        val calendarDay : TextView =view.find(R.id.diaryListCalendardayTV) as TextView
        val calendarSelect : ImageView = view.find(R.id.diaryListCalendarSelectIV) as ImageView
        val calendarStamp : ImageView = view.find(R.id.diaryListCalendarStampIV) as ImageView
    }

}