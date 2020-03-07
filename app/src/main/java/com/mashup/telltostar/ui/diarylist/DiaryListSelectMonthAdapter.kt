package com.mashup.telltostar.ui.diary

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.source.remote.response.ResCountYearDiaryDto
import kotlinx.android.synthetic.main.fragment_diary_list_time.view.*
import timber.log.Timber


class DiaryListSelectMonthAdapter :RecyclerView.Adapter<DiaryListSelectMonthAdapter.DiaryListSelectMonthViewHolder>(){
   private var countList : ArrayList<ResCountYearDiaryDto> = arrayListOf()
   private lateinit var ItemClick : DiaryListSelectMonthAdapter.OnItemClickListener

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListSelectMonthViewHolder {
      val convertView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list_time, parent, false)
      return DiaryListSelectMonthViewHolder(convertView)
   }

   override fun getItemCount() = countList.size

   override fun onBindViewHolder(holder: DiaryListSelectMonthViewHolder, position: Int) {
      Timber.d(countList[position].toString(),"")
      countList[position].let{
         holder.Year.text = it.year.toString()

         setMonth(holder.Jan,it.year,1,it.january)
         setMonth(holder.Feb,it.year,2,it.february)
         setMonth(holder.Mar,it.year,3,it.march)
         setMonth(holder.Apr,it.year,4,it.april)
         setMonth(holder.May,it.year,5,it.may)
         setMonth(holder.Jun,it.year,6,it.june)
         setMonth(holder.Jul,it.year,7,it.july)
         setMonth(holder.Aug,it.year,8,it.august)
         setMonth(holder.Sep,it.year,9,it.september)
         setMonth(holder.Oct,it.year,10,it.october)
         setMonth(holder.Nov,it.year,11,it.november)
         setMonth(holder.Dec,it.year,12,it.december)
      }

   }

   fun setMonth(view : View , year : Int, month : Int, count : Int){
       view.findViewById<TextView>(R.id.diaryListMonthTV).text = month.toString() + "월"
       view.findViewById<TextView>(R.id.diaryListCountTV).text = count.toString()
       view.setOnClickListener {
           ItemClick.onSelectMonth(it,year,month)
       }
   }

   fun setData(countList : ArrayList<ResCountYearDiaryDto>){
      this.countList = countList
      notifyDataSetChanged()
   }

   fun setOnItemClickListener(listener : OnItemClickListener){
      this.ItemClick = listener
   }

   interface OnItemClickListener{
      fun onSelectMonth(view : View ,year : Int, month : Int)
   }

   class DiaryListSelectMonthViewHolder(view:View): RecyclerView.ViewHolder(view){
      val Year :TextView = view.findViewById(R.id.diaryListYearTV) as TextView

//      val SelectMonth : GridLayout = view.findViewById(R.id.diaryListSelectMonthGL) as GridLayout

      val Jan = view.findViewById(R.id.diaryListJan) as View
      val Feb = view.findViewById(R.id.diaryListFeb) as View
      val Mar = view.findViewById(R.id.diaryListMar) as View
      val Apr = view.findViewById(R.id.diaryListApr) as View
      val May = view.findViewById(R.id.diaryListMay) as View
      val Jun = view.findViewById(R.id.diaryListJun) as View
      val Jul = view.findViewById(R.id.diaryListJul) as View
      val Aug = view.findViewById(R.id.diaryListAug) as View
      val Sep = view.findViewById(R.id.diaryListSep) as View
      val Oct = view.findViewById(R.id.diaryListOct) as View
      val Nov = view.findViewById(R.id.diaryListNov) as View
      val Dec = view.findViewById(R.id.diaryListDec) as View
   }
}