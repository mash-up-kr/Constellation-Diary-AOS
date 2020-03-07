package com.mashup.telltostar.ui.diarylist

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.source.remote.response.ResCountYearDiaryDto
import com.mashup.telltostar.data.source.remote.response.SimpleDiary
import com.mashup.telltostar.ui.diary.DiaryEditActivity
import com.mashup.telltostar.ui.diary.DiaryListSelectMonthAdapter
import com.mashup.telltostar.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_diary_list.*
import kotlinx.android.synthetic.main.dialog_delete_diary.view.*
import kotlinx.android.synthetic.main.dialog_select_month.view.*
import kotlinx.android.synthetic.main.fragment_diary_list.*
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DiaryListActivity : AppCompatActivity() {
    private val diaryListCalendarAdapter = DiaryListCalendarAdapter()
    private val diaryListAdapter = DiaryListAdapter()
    private val diaryListSelectMonthAdapter = DiaryListSelectMonthAdapter()
    private var data : List<SimpleDiary> = listOf()
    private var countData : ArrayList<ResCountYearDiaryDto> = arrayListOf()
    private var calendarData : ArrayList<DataCalendar> = arrayListOf()
    private val current = Calendar.getInstance()


    private val diaryRepository = Injection.provideDiaryRepo(this@DiaryListActivity)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        listDiaryformatBtn.setImageResource(R.drawable.icon_today_24_px)
        listDiaryformatBtn.tag = "list"
        //초기 셋팅
        initList()
        initCalendar()
        dataLoad()
        changeFormat()
        //종료
        closeClick()

        listDiaryDateTV.setOnClickListener {
            setDatePicker()
        }
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
                if(it.diaries.isEmpty()){
                    setEmpty()
                }else{
                    listDiaryEmpty.visibility = View.INVISIBLE
                    listDiaryList.visibility = View.VISIBLE
                    diaryListAdapter.setData(it.diaries)
                }
                setCalendarData(it.diaries)
                data = it.diaries
            }){
                var errorMsg = Toast.makeText(this,"error",Toast.LENGTH_SHORT)
                errorMsg.show()
            }

        Timber.d(data.toString(),"")
        diaryListInclude.visibility = View.INVISIBLE

        setDate()
    }

    fun diaryDetail(id : Int){
        DiaryEditActivity.startDiaryEditActivity(
            this,
            REQUEST_DIARY_EDIT,
            id
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("requestCode ; $requestCode , resultCode : $resultCode , data : $data")
        if (requestCode == REQUEST_DIARY_EDIT) {
            if (resultCode == Activity.RESULT_OK) {
                dataLoad()
            }
        }
    }

    fun diaryDelete(id : Int){
        val dialogDelete = AlertDialog
            .Builder(this@DiaryListActivity)
            .create()
        val dialogDeleteView = LayoutInflater
            .from(this@DiaryListActivity)
            .inflate(R.layout.dialog_delete_diary,null).apply{
                this.diaryDeleteTV.setOnClickListener {
                    diaryRepository.delete(id)
                        .subscribe({
                            dataLoad()
                            dialogDelete.dismiss()
                        }){
                            var errorMsg = Toast.makeText(this@DiaryListActivity,"error",Toast.LENGTH_SHORT)
                            errorMsg.show()
                        }
                }
                this.setOnClickListener {
                    dialogDelete.dismiss()
                }
            }

        dialogDelete.also {
            it.setView(dialogDeleteView)
            it.show()
        }
    }

    //달 선택
    fun setDatePicker(){
        val dialogSelect = AlertDialog
            .Builder(this@DiaryListActivity)
            .create()
        dialogSelect.window.setGravity(Gravity.BOTTOM)

        val dialogSelectView = LayoutInflater
            .from(this@DiaryListActivity)
            .inflate(R.layout.dialog_select_month,null).apply{
                this.diaryListSelectRV.let{
                    it.adapter = this@DiaryListActivity.diaryListSelectMonthAdapter
                    it.layoutManager = LinearLayoutManager(this@DiaryListActivity, LinearLayoutManager.VERTICAL, false)
                        diaryRepository.count(current.get(Calendar.YEAR))
                            .subscribe({
                                countData=it.diaries
                                diaryListSelectMonthAdapter.setData(it.diaries)
                            }){
                                var errorMsg = Toast.makeText(this@DiaryListActivity,"CountError",Toast.LENGTH_SHORT)
                                errorMsg.show()
                            }
                    diaryListSelectMonthAdapter.setOnItemClickListener(object :DiaryListSelectMonthAdapter.OnItemClickListener{
                        override fun onSelectMonth(view: View, year: Int, month: Int) {
                            view.findViewById<TextView>(R.id.diaryListMonthTV).setTextColor(Color.WHITE)
                            view.findViewById<TextView>(R.id.diaryListCountTV).setTextColor(Color.WHITE)
                            view.setBackgroundResource(R.drawable.btn_rounded)
                            Handler().postDelayed(object : Runnable{
                                override fun run() {
                                    dialogSelect.dismiss()
                                    current.set(year,month-1,1)
                                    dataLoad()
                                }
                            },300)
                        }
                    })
                }
            }

        dialogSelect.also {
            it.setView(dialogSelectView)
            it.show()
        }
    }

    //리스트
    fun initList(){//listAdapter
        listDiaryList.adapter = this.diaryListAdapter
        listDiaryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //list기능
        diaryListDetail()
        diaryListDelete()
        //글씨 비었을 때
    }

    fun diaryListDetail(){
        diaryListAdapter.setOnItemClickListener(object : DiaryListAdapter.OnItemClickListener{
            override fun onItemClick(id: Int) {
                diaryDetail(id)
            }

        })
    }

    fun diaryListDelete(){
        diaryListAdapter.setOnItemLongClickListener(object : DiaryListAdapter.OnItemLongClickListener{
            override fun onItemLongClick(id: Int) {
                diaryDelete(id)
            }
        })
    }

    //캘린더
    fun initCalendar(){
        listDiaryCalendarRV.adapter = this.diaryListCalendarAdapter
        listDiaryCalendarRV.setLayoutManager(GridLayoutManager(this,7))

        // 기능
        diaryCalendarDetail()
        diaryCalendarItemVisible()
        diaryCalendarDelete()
    }

    fun setCalendarData(diaryList : List<SimpleDiary>){
        calendarData = arrayListOf()
        val nullData = SimpleDiary(-1,"","")
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

    fun setEmpty(){
        listDiaryEmpty.visibility = View.VISIBLE
        listDiaryList.visibility = View.INVISIBLE

        listDiaryEmpty.findViewById<Button>(R.id.listDiaryConstellationBtn).setOnClickListener {
            
        }
        listDiaryEmpty.findViewById<Button>(R.id.listDiaryDiaryBtn).setOnClickListener {

        }
    }

    fun diaryCalendarItemVisible(){
        diaryListCalendarAdapter.setOnItemClickListener(object :DiaryListCalendarAdapter.OnItemClickListener {
            override fun onDiarySet(view: View, diary: SimpleDiary) {
                diaryListInclude.visibility = View.VISIBLE
                setDiary(diary)
            }

            override fun onDiaryClear(view: View) {
                diaryListInclude.visibility = View.INVISIBLE
            }
        })
    }

    fun diaryCalendarDetail(){
        diaryListInclude.setOnClickListener{
            val diaryDate = Integer.parseInt(diaryListInclude.findViewById<TextView>(R.id.diaryListDateTV).text.toString())-1
            diaryDetail(calendarData[diaryDate].diary.id)
        }
    }

    fun diaryCalendarDelete(){
        diaryListInclude.setOnLongClickListener {
            val diaryDate = Integer.parseInt(diaryListInclude.findViewById<TextView>(R.id.diaryListDateTV).text.toString())-1
            diaryDelete(calendarData[diaryDate].diary.id)

            return@setOnLongClickListener true
        }
    }

    //choice date
    fun changeFormat(){
        listDiaryformatBtn.setOnClickListener {
            if(listDiaryformatBtn.tag.equals("list")) {//리스트 보여주고 있음
                listDiaryformatBtn.setImageResource(R.drawable.icon_list_24_px)
                listDiaryformatBtn.tag = "calendar"
                if(data.isEmpty()){
                    listDiaryEmpty.visibility = View.INVISIBLE
                }else{
                    listDiaryList.visibility = View.INVISIBLE
                }
                listDiaryCalendar.visibility = View.VISIBLE
            }else{
                listDiaryformatBtn.setImageResource(R.drawable.icon_today_24_px)
                listDiaryformatBtn.tag = "list"
                if(data.isEmpty()){
                    listDiaryEmpty.visibility = View.VISIBLE
                }else{
                    listDiaryList.visibility = View.VISIBLE
                }
                listDiaryCalendar.visibility = View.INVISIBLE
            }


        }
    }

    fun setDate(){
        val formatDate = SimpleDateFormat("yyyy'년 'MM'월'",Locale.getDefault())
        val date = formatDate.format(current.time)
        listDiaryDateTV.text = date
        listDiaryformatBtn.setImageResource(R.drawable.icon_today_24_px)
        listDiaryformatBtn.tag = "list"
        if(data.isEmpty()){
            listDiaryEmpty.visibility = View.VISIBLE
        }else{
            listDiaryList.visibility = View.VISIBLE
        }
        listDiaryCalendar.visibility = View.INVISIBLE
    }


    //finish
    fun closeClick(){
        listDiaryCloseBtn.setOnClickListener{

            finish()
        }
    }


    companion object {
        private const val REQUEST_DIARY_EDIT = 0x001
    }
}
