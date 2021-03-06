package com.mashup.telltostar.ui.diarylist


import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.source.remote.response.SimpleDiary
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DiaryListContentsViewHolder(view: View): RecyclerView.ViewHolder(view){
    lateinit var diary : Temp_diary
    lateinit var content : SimpleDiary
    val diaryDate : TextView = view.findViewById(R.id.diaryListDateTV) as TextView
    val diaryDay : TextView = view.findViewById(R.id.diaryListDayTV) as TextView
    val diaryTitle : TextView = view.findViewById(R.id.diaryListTitleTV) as TextView
    val diaryBox : LinearLayout = view.findViewById(R.id.diaryListItem) as LinearLayout

    fun bind(item: Temp_diary) {
        Timber.d(item.toString(),"")

        diary = item
        content = item.diary

        bindDate()

        diaryTitle.text = content.title

    }

    fun bindDate() {
        var contentDate = ""
        var contentDay = ""
        if (content.date != null) {
            //convert utc to localTime
            val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
            utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var gpsUTCDate: Date? = null
            try {
                gpsUTCDate = utcFormatter.parse(content.date )
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val localDateFormatter = SimpleDateFormat("dd", Locale.KOREA)
            val localDayFormatter = SimpleDateFormat("EEE", Locale.KOREA)
            localDateFormatter.timeZone = TimeZone.getDefault()
            localDayFormatter.timeZone = TimeZone.getDefault()
            assert(gpsUTCDate != null)
            contentDate = localDateFormatter.format(gpsUTCDate?.time)

            contentDay = localDayFormatter.format(gpsUTCDate?.time)
        }
        when (contentDay) {//요일별 색 지정
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
