package com.mashup.telltostar.ui.diarylist

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.source.remote.response.SimpleDiary
import org.jetbrains.anko.find
import timber.log.Timber

class DiaryListCalendarAdapter : RecyclerView.Adapter<DiaryListCalendarAdapter.DiaryListCalendarViewHolder>() {
    private var stampList : ArrayList<DataCalendar> = arrayListOf()
    private var startPosition : Int = 0
    private var lastDay : Int = 0

    private var selectDay = -1

    private lateinit var ItemClick : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListCalendarViewHolder {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list_calendar, parent, false)
        return DiaryListCalendarViewHolder(convertView)
    }

    override fun getItemCount() = stampList.size + startPosition

    override fun onBindViewHolder(holder: DiaryListCalendarViewHolder, position: Int) {
        if(position > startPosition-1){
            holder.calendarDay.text = (position-startPosition+1).toString()
            holder.calendarSelect.visibility = View.INVISIBLE
            if(stampList[position-startPosition].stamp){
                holder.calendarStamp.visibility = View.VISIBLE
            }else{
                holder.calendarStamp.visibility = View.INVISIBLE
            }
            if(stampList[position-startPosition].select){
                holder.calendarSelect.visibility = View.VISIBLE
                holder.calendarDay.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
            }else{
                holder.calendarSelect.visibility = View.INVISIBLE
                holder.calendarDay.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
            }
        }else{
            holder.calendarDay.text = ""
            holder.calendarStamp.visibility = View.INVISIBLE
            holder.calendarSelect.visibility = View.INVISIBLE
        }
        holder.calendarBox.setOnClickListener {
            if(position != RecyclerView.NO_POSITION){
                if (position > startPosition-1) {
                    stampList[position - startPosition].select = true
                    if(selectDay != -1){
                        stampList[selectDay].select = false
                        notifyItemChanged(selectDay+startPosition)
                    }
                    selectDay = position-startPosition
                    notifyItemChanged(position)

                    if(stampList[position-startPosition].diary.id != -1) {
                        ItemClick.onDiarySet(it, stampList[position - startPosition].diary)
                    }else{
                        ItemClick.onDiaryClear(it)
                    }
                }
            }
        }

    }


    fun setDay(dayList : ArrayList<DataCalendar>,startPosition : Int,lastDay : Int){
        selectDay = -1
        this.stampList = dayList
        this.startPosition = startPosition-1
        this.lastDay = lastDay
        notifyDataSetChanged()
        Timber.d(dayList.toString(),"")
    }

    fun setOnItemClickListener(listener : OnItemClickListener){
        this.ItemClick = listener
    }

    interface OnItemClickListener{
        fun onDiarySet(view: View,diary : SimpleDiary)
        fun onDiaryClear(view : View)
    }

    class DiaryListCalendarViewHolder(view: View):RecyclerView.ViewHolder(view){
        val calendarBox : LinearLayout = view.find(R.id.diaryListCalendarBox)
        val calendarDay : TextView =view.find(R.id.diaryListCalendardayTV) as TextView
        val calendarSelect : ImageView = view.find(R.id.diaryListCalendarSelectIV) as ImageView
        val calendarStamp : ImageView = view.find(R.id.diaryListCalendarStampIV) as ImageView
    }

}