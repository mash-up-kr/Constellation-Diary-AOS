package com.mashup.telltostar.ui.diarylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R

class DiaryListSelectMonthAdapter :RecyclerView.Adapter<DiaryListSelectMonthAdapter.DiaryListSelectMonthViewHolder>(){
   private var countList : ArrayList<Int> = arrayListOf()

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  {
      val convertView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list_calendar, parent, false)
      return DiaryListCalendarViewHolder(convertView)
   }

   override fun getItemCount() = countList.size

   override fun onBindViewHolder(holder: , position: Int) {
   }


   class DiaryListSelectMonthViewHolder(view:View): RecyclerView.ViewHolder(view){

   }
   class DiaryListSelectYearViewHolderview:View): RecyclerView.ViewHolder(view){

   }
}