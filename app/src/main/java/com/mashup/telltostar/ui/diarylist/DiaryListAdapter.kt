package com.mashup.telltostar.ui.diarylist


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Diary
import com.mashup.telltostar.data.ModelDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DiaryListAdapter: RecyclerView.Adapter<DiaryListAdapter.BaseViewHolder<*>>() {
    var dataList : ArrayList<Any> = arrayListOf()

    companion object{
        private val TYPE_CONTENTS = 0
        private  val TYPE_DATE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            TYPE_CONTENTS ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list, parent, false)
                contentsViewHolder(view)
            }
            TYPE_DATE ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_date, parent, false)
                dateViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        dataList[position].let{
            when(holder){
                is contentsViewHolder -> holder.bind(it as Diary)
                is dateViewHolder -> holder.bind(it as ModelDate)
                else -> throw IllegalArgumentException()
            }
        }
    }

    fun setData(item: ArrayList<Any>)= dataList.addAll(item)

    override fun getItemViewType(position: Int): Int {
        dataList[position].let{
            return when (it) {
                is Diary -> TYPE_CONTENTS
                is ModelDate -> TYPE_DATE
                else -> throw IllegalArgumentException("Invalid type of data " + position)
            }
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class contentsViewHolder(view: View): BaseViewHolder<Diary>(view) {
        val diaryDate : TextView = itemView.findViewById(R.id.diaryListDateTV) as TextView
        val diaryDay : TextView = itemView.findViewById(R.id.diaryListDayTV) as TextView
        val diaryTitle : TextView = itemView.findViewById(R.id.diaryListTitleTV) as TextView

        override fun bind(item: Diary) {
            var contentDate = ""
            var contentDay = ""
            if (item.date != null) {
                //convert utc to localTime
                val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
                utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
                var gpsUTCDate: Date? = null //from  ww  w.j  a va 2 s  . c  o  m
                try {
                    gpsUTCDate = utcFormatter.parse(item.date )
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val localDateFormatter = SimpleDateFormat("dd", Locale.KOREA)
                val localDayFormatter = SimpleDateFormat("EEE", Locale.KOREA)
                localDateFormatter.timeZone = TimeZone.getDefault()
                localDayFormatter.timeZone = TimeZone.getDefault()
                assert(gpsUTCDate != null)
                contentDate = localDateFormatter.format(gpsUTCDate!!.time)

                contentDay = localDayFormatter.format(gpsUTCDate!!.time)
                //요일 지정

            }
            when(contentDay){
                "토" -> {
                    diaryDay.setTextColor(Color.parseColor("#4a5eff"))
                    diaryDate.setTextColor(Color.parseColor("#4a5eff"))
                }
                "일" -> {
                    diaryDay.setTextColor(Color.parseColor("#ff6262"))
                    diaryDate.setTextColor(Color.parseColor("#ff6262"))
                }
            }
            diaryDay.text = contentDay
            diaryDate.text = contentDate
            diaryTitle.text = item.title
        }
    }

    class dateViewHolder(view: View) : BaseViewHolder<ModelDate>(view){
        val diaryMonth : TextView = itemView.findViewById(R.id.diaryListHeaderDateTV)
        override fun bind(item: ModelDate) {
            diaryMonth.text = item.month.toString()+"년 "+item.day.toString()+"월"
        }
    }
}