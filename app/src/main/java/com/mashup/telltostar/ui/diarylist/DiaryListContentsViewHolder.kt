package com.mashup.telltostar.ui.diarylist

import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Diary
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DiaryListContentsViewHolder(view: View): DiaryListAdapter.BaseViewHolder<Diary>(view) {
    val diaryDate : TextView = view.findViewById(R.id.diaryListDateTV) as TextView
    val diaryDay : TextView = view.findViewById(R.id.diaryListDayTV) as TextView
    val diaryTitle : TextView = view.findViewById(R.id.diaryListTitleTV) as TextView
    val diarySelect : CheckBox = view.findViewById(R.id.diaryListSelectCB) as CheckBox

    override fun bind(item: Diary, pos:Int) {
        var contentDate = ""
        var contentDay = ""
        if (item.date != null) {
            //convert utc to localTime
            val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
            utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var gpsUTCDate: Date? = null
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
        }
        when(contentDay){//요일별 색 지정
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

        diarySelect.setOnClickListener {
            if(pos != RecyclerView.NO_POSITION){
                if (mListener != null) {
                    mListener.onItemClick(it, position) ;
                }
            }
        }
    }
}
