package com.mashup.telltostar.ui.diarylist

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.source.remote.response.SimpleDiary
import kotlinx.android.synthetic.main.activity_diary_list.*
import kotlinx.android.synthetic.main.fragment_diary_list.*
import org.w3c.dom.Text
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DiaryListActivity : AppCompatActivity() {
    private val diaryListCalendarAdapter = DiaryListCalendarAdapter()
    private val diaryListAdapter = DiaryListAdapter()
    private var checkedNum = 0
    private var data : List<SimpleDiary> = listOf()
    private var calendarData : ArrayList<DataCalendar> = arrayListOf()
    private val current = Calendar.getInstance()


    private val diaryRepository = Injection.provideDiaryRepo(this@DiaryListActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        listDiaryformatBtn.setImageResource(R.drawable.icon_today_24_px)
        listDiaryformatBtn.tag = "list"

        initList()
        initCalendar()
        changeFormat()

        setDatePicker()

        closeClick()
//        changeClickData()
//        changeDeleteText()
    }
    //전체 데이터 로드 (리스트 & 캘린더 공동사용 )
    @SuppressLint("CheckResult")
    @TargetApi(Build.VERSION_CODES.O)
    fun dataLoad(){
        val formatYear = SimpleDateFormat("yyyy",Locale.getDefault())
        val formatMonth = SimpleDateFormat("MM",Locale.getDefault())

        val currentYear = formatYear.format(current.time).toInt()
        val currentMonth = formatMonth.format(current.time).toInt()

        diaryRepository.gets(currentMonth, currentYear)
            .subscribe({
//                data = it.diaries
                diaryListAdapter.setData(it.diaries)
                setCalendarData(it.diaries)
            }){
                var errorMsg = Toast.makeText(this,"error",Toast.LENGTH_SHORT)
                errorMsg.show()
            }

        Timber.d(data.toString(),"")

//        diaryListAdapter.setData(data)
//        setCalendarData(data)

        setDate()

        diaryListAdapter.notifyItemRangeChanged(0,diaryListAdapter.itemCount,"set")
    }

    fun dataUpdate(){

    }
    fun deleteDiary(){

    }

    //달 선택
    fun setDatePicker(){
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                current.set(Calendar.YEAR,year)
                current.set(Calendar.MONTH,month)
                setDate()
                dataLoad()
            }
        }
        listDiaryDateTV.setOnClickListener {
            DatePickerDialog(this@DiaryListActivity,dateSetListener,
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)).show()
        }


    }



    //리스트
    fun initList(){//listAdapter
        listDiaryList.adapter = this.diaryListAdapter
        listDiaryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var diveder = DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        diveder.setDrawable(this.getDrawable(R.drawable.divider_diary_list))
        listDiaryList.addItemDecoration(diveder)

        dataLoad()
    }
    //캘린더
    fun initCalendar(){
        listDiaryCalendarRV.adapter = this.diaryListCalendarAdapter
        listDiaryCalendarRV.setLayoutManager(GridLayoutManager(this,7))
        diaryListCalendarAdapter.setOnItemClickListener(object :DiaryListCalendarAdapter.OnItemClickListener {
            override fun onDiarySet(view: View, diary: SimpleDiary) {
                diaryListInclude.visibility = View.VISIBLE
                setDiary(diary)
            }

            override fun onDiaryClear(view: View) {
                diaryListInclude.visibility = View.INVISIBLE
            }
        })

        diaryListInclude.setOnLongClickListener {
            val diaryDate = Integer.parseInt(diaryListInclude.findViewById<TextView>(R.id.diaryListDateTV).text.toString())
            val diaryId= calendarData[diaryDate].diary.idg

            true
        }

    }
    fun setCalendarData(diaryList : List<SimpleDiary>){
        val nullData = SimpleDiary(-1,"","")

        val currentDay = current.get(Calendar.DAY_OF_MONTH)
        current.set(Calendar.DAY_OF_MONTH,1)

        val startPosition = current.get(Calendar.DAY_OF_WEEK)
        val lastDay = current.getActualMaximum(Calendar.DAY_OF_MONTH)

        for(i in 0 until lastDay){
            calendarData.add(DataCalendar(false,false,nullData))
        }

        for(i in 0 until diaryList.size){
            val date = utcToDay(diaryList[i].date)
            calendarData[date].stamp = true
            calendarData[date].diary = diaryList[i]
        }
        diaryListCalendarAdapter.setDay(calendarData,startPosition,lastDay)
    }
    fun utcToDay(utc : String) : Int{
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(utc)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localDateFormatter = SimpleDateFormat("d", Locale.KOREA)
        localDateFormatter.timeZone = TimeZone.getDefault()
        assert(gpsUTCDate != null)

        return Integer.parseInt(localDateFormatter.format(gpsUTCDate?.time))-1
    }
    fun setDiary(diary : SimpleDiary){
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss", Locale.KOREA)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormatter.parse(diary.date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localDateFormatter = SimpleDateFormat("dd", Locale.KOREA)
        val localDayFormatter = SimpleDateFormat("EEE", Locale.KOREA)
        localDateFormatter.timeZone = TimeZone.getDefault()
        localDayFormatter.timeZone = TimeZone.getDefault()
        assert(gpsUTCDate != null)
        diaryListInclude.findViewById<TextView>(R.id.diaryListDateTV).text = localDateFormatter.format(gpsUTCDate?.time)
        diaryListInclude.findViewById<TextView>(R.id.diaryListDayTV).text = localDayFormatter.format(gpsUTCDate?.time)
        when (diaryListDayTV.text) {//요일별 색 지정
            "토" -> {
                val lightishBlue =
                    ContextCompat.getColor(this@DiaryListActivity, R.color.lightish_blue)
                diaryListInclude.findViewById<TextView>(R.id.diaryListDateTV).setTextColor(lightishBlue)
                diaryListInclude.findViewById<TextView>(R.id.diaryListDayTV).setTextColor(lightishBlue)
            }
            "일" -> {
                val grapeFruit =
                    ContextCompat.getColor(this@DiaryListActivity, R.color.grapefruit)
                diaryListInclude.findViewById<TextView>(R.id.diaryListDateTV).setTextColor(grapeFruit)
                diaryListInclude.findViewById<TextView>(R.id.diaryListDayTV).setTextColor(grapeFruit)
            }
        }
        diaryListInclude.findViewById<TextView>(R.id.diaryListTitleTV).text = diary.title
    }


    //메뉴 화면 변화
    fun changeClickData(){
        listDiarySelectTV.let{btn ->
            btn.setOnClickListener {
                diaryListAdapter.let{adapter ->
                    if(btn.text.equals(getString(R.string.select))){
                        adapter.notifyItemRangeChanged(0,adapter.itemCount,true)
                        btn.setText(getString(R.string.delete))
                        val red = ContextCompat.getColor(this, R.color.grapefruit)
                        btn.setTextColor(red)
                    }else {
                        adapter.notifyItemRangeChanged(0,adapter.itemCount,false)
                        btn.setText(getString(R.string.select))
                        val blackTwo = ContextCompat.getColor(this, R.color.black_two)
                        btn.setTextColor(blackTwo)
                        checkedNum = 0
                    }
                }
            }
        }
        diaryListAdapter.notifyDataSetChanged()
    }
    fun changeFormat(){
        val listImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_today_24_px)
        listDiaryformatBtn.setOnClickListener {
            if(listDiaryformatBtn.tag.equals("list")) {//리스트 보여주고 있음
                listDiaryformatBtn.setImageResource(R.drawable.icon_list_24_px)
                listDiaryformatBtn.tag = "calendar"
                listDiaryList.visibility = View.INVISIBLE
                listDiaryCalendar.visibility = View.VISIBLE
            }else{
                listDiaryformatBtn.setImageResource(R.drawable.icon_today_24_px)
                listDiaryformatBtn.tag = "list"
                listDiaryList.visibility = View.VISIBLE
                listDiaryCalendar.visibility = View.INVISIBLE
            }


        }
    }

    fun changeDeleteText(){
        diaryListAdapter.setOnItemClickListener(object : DiaryListAdapter.OnItemClickListener{
            override fun onItemClick(isChecked: Boolean) {
                if(isChecked == true){
                    checkedNum ++
                    listDiarySelectTV.text = checkedNum.toString() + getString(R.string.delete)
                }else{
                    checkedNum --
                    if(checkedNum == 0){
                        listDiarySelectTV.setText(getString(R.string.delete))
                    }else{
                        listDiarySelectTV.text = checkedNum.toString() + getString(R.string.delete)
                    }
                }
            }

        })
    }
    fun setDate(){
        val formatDate = SimpleDateFormat("yyyy'년 'MM'월'",Locale.getDefault())
        val date = formatDate.format(current.time)
        listDiaryDateTV.text = date
    }

    fun closeClick(){
        listDiaryCloseBtn.setOnClickListener{
            finish()
        }
    }
}
