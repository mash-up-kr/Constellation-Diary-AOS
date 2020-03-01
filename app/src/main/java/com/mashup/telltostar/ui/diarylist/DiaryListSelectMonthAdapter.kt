package com.mashup.telltostar.ui.diarylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.source.remote.response.DiaryCount
import com.mashup.telltostar.data.source.remote.response.ResCountYearDiaryDto


class DiaryListSelectMonthAdapter :RecyclerView.Adapter<DiaryListSelectMonthAdapter.DiaryListSelectMonthViewHolder>(){
   private var countList : ArrayList<DiaryCount> = arrayListOf()
   private lateinit var ItemClick : DiaryListSelectMonthAdapter.OnItemClickListener

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListSelectMonthViewHolder {
      val convertView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list_time, parent, false)
      return DiaryListSelectMonthViewHolder(convertView)
   }

   override fun getItemCount() = countList.size

   override fun onBindViewHolder(holder: DiaryListSelectMonthViewHolder, position: Int) {
      countList[position].diaries.let{
         holder.Year.text = it.year.toString()
         var count = 0
         for(i in 0 .. 12){
            holder.SelectMonth.get(i).findViewById<TextView>(R.id.diaryListMonthTV).text = i.toString()+"월"
            when(i){
               1-> count = it.january
               2-> count = it.february
               3-> count = it.march
               4-> count = it.april
               5-> count = it.may
               6-> count = it.june
               7-> count = it.july
               8-> count = it.august
               9-> count = it.september
               10-> count = it.october
               11-> count = it.november
               12-> count = it.december
            }
            holder.SelectMonth.get(i).findViewById<TextView>(R.id.diaryListMonthTV).text =count.toString()

         }
      }

   }

   fun setData(countList : ArrayList<DiaryCount>){
      this.countList = countList
   }

   fun setOnItemClickListener(listener : OnItemClickListener){
      this.ItemClick = listener
   }

   interface OnItemClickListener{
      fun onSelectMonth(year : Int, month : Int)
   }

   class DiaryListSelectMonthViewHolder(view:View): RecyclerView.ViewHolder(view){
      val Year :TextView = view.findViewById(R.id.diaryListYearTV) as TextView

      val SelectMonth : GridLayout = view.findViewById(R.id.diaryListSelectMonthGL) as GridLayout

      val Jan   = view.findViewById(R.id.diaryListJan) as View
      val Feb   = view.findViewById(R.id.diaryListJan) as View
      val Mar   = view.findViewById(R.id.diaryListJan) as View
      val Apr   = view.findViewById(R.id.diaryListJan) as View
      val May   = view.findViewById(R.id.diaryListJan) as View
      val Jun   = view.findViewById(R.id.diaryListJan) as View
      val Ju1   = view.findViewById(R.id.diaryListJan) as View
      val Aug   = view.findViewById(R.id.diaryListJan) as View
      val Sep   = view.findViewById(R.id.diaryListJan) as View
      val Oct   = view.findViewById(R.id.diaryListJan) as View
      val Nov   = view.findViewById(R.id.diaryListJan) as View
      val Dec   = view.findViewById(R.id.diaryListJan) as View
   }
}