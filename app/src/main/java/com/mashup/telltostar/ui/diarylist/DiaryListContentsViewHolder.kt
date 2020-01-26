package com.mashup.telltostar.ui.diarylist

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mashup.telltostar.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DiaryListContentsViewHolder(view: View): DiaryListAdapter.BaseViewHolder<Temp_diary>(view) {
    private lateinit var diary : Temp_diary
    private val diaryDate : TextView = view.findViewById(R.id.diaryListDateTV) as TextView
    private val diaryDay : TextView = view.findViewById(R.id.diaryListDayTV) as TextView
    private val diaryTitle : TextView = view.findViewById(R.id.diaryListTitleTV) as TextView
    private val diarySelect : CheckBox = view.findViewById(R.id.diaryListSelectCB) as CheckBox

    override fun bind(item: Temp_diary, pos:Int) {
        diary = item

        bindDate()

        diaryTitle.text = item.title

    }

    fun visibleCheckBox(){
        if(diary.isVisible == true){
            diarySelect.visibility = View.VISIBLE
        }else{
            diarySelect.visibility = View.INVISIBLE
        }
    }

//    fun checkClick(){
//        if(diary.isChecked == true){
//            diarySelect.setBackgroundResource(R.drawable.icon_checked)
//        }else{
//            diarySelect.setBackgroundResource(R.drawable.icon_unchecked)
//        }
//        diary.isChecked = diarySelect.isChecked
//    }

    fun bindDate(){
        var contentDate = ""
        var contentDay = ""
        if (diary.date != null) {
            //convert utc to localTime
            val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
            utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var gpsUTCDate: Date? = null
            try {
                gpsUTCDate = utcFormatter.parse(diary.date )
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
        }
        when(contentDay){//요일별 색 지정
            "토" -> {
                val lightishBlue = ContextCompat.getColor(itemView.context, R.color.lightish_blue)
                diaryDay.setTextColor(lightishBlue)
                diaryDate.setTextColor(lightishBlue)
            }
            "일" -> {
                val grapeFruit = ContextCompat.getColor(itemView.context, R.color.grapefruit)
                diaryDay.setTextColor(grapeFruit)
                diaryDate.setTextColor(grapeFruit)
            }
        }

        diaryDay.text = contentDay
        diaryDate.text = contentDate
    }
}
