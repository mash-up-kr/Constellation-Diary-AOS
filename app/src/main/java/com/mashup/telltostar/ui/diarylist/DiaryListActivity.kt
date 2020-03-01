package com.mashup.telltostar.ui.diarylist

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.source.remote.response.DiaryCount
import com.mashup.telltostar.data.source.remote.response.SimpleDiary
import com.mashup.telltostar.ui.diary.DiaryEditActivity
import kotlinx.android.synthetic.main.activity_diary_list.*
import kotlinx.android.synthetic.main.dialog_date_picker.view.*
import kotlinx.android.synthetic.main.dialog_delete_diary.view.*
import kotlinx.android.synthetic.main.fragment_diary_list.*
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DiaryListActivity : AppCompatActivity() {
    private val diaryListCalendarAdapter = DiaryListCalendarAdapter()
    private val diaryListAdapter = DiaryListAdapter()
    private val diaryListSelectMonthAdapter = DiaryListSelectMonthAdapter()
    private var data : List<SimpleDiary> = listOf()
    private var countData : ArrayList<DiaryCount> = arrayListOf()
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
                diaryListAdapter.setData(it.diaries)
                setCalendarData(it.diaries)
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
//
//        val windowParam = dialogDelete.window.attributes
//        windowParam.width = WindowManager.LayoutParams.WRAP_CONTENT
//        windowParam.height = WindowManager.LayoutParams.WRAP_CONTENT
//        dialogDelete.window.attributes = windowParam

        dialogDelete.also {
            it.setView(dialogDeleteView)
            it.show()
        }
    }

    @SuppressLint("TimberArgCount")
    fun setDatePicker(){
        val now = current.get(Calendar.YEAR)
        val dialogSelect = AlertDialog
            .Builder(this@DiaryListActivity)
            .create()
        val dialogSelectView = LayoutInflater
            .from(this@DiaryListActivity)
            .inflate(R.layout.dialog_date_picker,null).apply {
                this.numberPickerYear.minValue = now - 2
                this.numberPickerYear.maxValue = now + 2
                this.numberPickeMonth.minValue = 1
                this.numberPickeMonth.maxValue = 12

                this.selectBtn.setOnClickListener {
                    Timber.d("${this.numberPickeMonth.value}", "")
                    current.set(Calendar.YEAR, this.numberPickerYear.value)
                    current.set(Calendar.MONTH, this.numberPickeMonth.value - 1)
                    dataLoad()
                    dialogSelect.dismiss()
                }
                this.cancelBtn.setOnClickListener {
                    dialogSelect.dismiss()
                }
            }

        dialogSelect.also {
            it.setView(dialogSelectView)
            it.show()
        }

    }

    //달 선택
//    fun setDatePicker(){
//        val dialogSelect = AlertDialog
//            .Builder(this@DiaryListActivity)
//            .create()
//
//        this@DiaryListActivity.countData.isEmpty()
//
//        val dialogSelectView = LayoutInflater
//            .from(this@DiaryListActivity)
//            .inflate(R.layout.dialog_select_month,null).apply{
//                this.diaryListSelectRV.let{
//                    it.adapter = this@DiaryListActivity.diaryListSelectMonthAdapter
//                    it.layoutManager = LinearLayoutManager(this@DiaryListActivity, LinearLayoutManager.VERTICAL, false)
////                    this@DiaryListActivity.countLoad(current.get(Calendar.YEAR))
//                    for(i in current.get(Calendar.YEAR) .. current.get(Calendar.YEAR)+2){
//                        diaryRepository.count(i)
//                            .subscribe({
//                                countData.add(it)
//                                Timber.d(it.toString(),"")
//                            }){
//                                var errorMsg = Toast.makeText(this@DiaryListActivity,"CountError",Toast.LENGTH_SHORT)
//                                errorMsg.show()
//                            }
//                    }
//                    diaryListSelectMonthAdapter.setData(countData)
//                }
//            }
//
//        dialogSelect.also {
//            it.setView(dialogSelectView)
//            it.show()
//        }
//    }

//    @SuppressLint("CheckResult")
//    fun countLoad(year : Int){
//        for(i in year+2 until year-2){
//            diaryRepository.count(i)
//                .subscribe({
//                    countData.add(it)
//                    Timber.d(it.toString(),"")
//                }){
//                    var errorMsg = Toast.makeText(this@DiaryListActivity,"CountError",Toast.LENGTH_SHORT)
//                    errorMsg.show()
//                }
//        }
//
//        diaryListSelectMonthAdapter.setData(countData)
//    }



    //리스트
    fun initList(){//listAdapter
        listDiaryList.adapter = this.diaryListAdapter
        listDiaryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //list기능
        diaryListDetail()
        diaryListDelete()
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

    fun setDate(){
        val formatDate = SimpleDateFormat("yyyy'년 'MM'월'",Locale.getDefault())
        val date = formatDate.format(current.time)
        listDiaryDateTV.text = date
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
