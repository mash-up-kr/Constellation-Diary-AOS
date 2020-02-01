package com.mashup.telltostar.ui.diarylist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.ModelDate
import com.mashup.telltostar.data.repository.DiaryRepoImpl
import kotlinx.android.synthetic.main.activity_diary_edit.*
import kotlinx.android.synthetic.main.activity_diary_list.*
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.MonthDay
import java.util.*
import java.util.logging.SimpleFormatter
import kotlin.collections.ArrayList

class DiaryListActivity : AppCompatActivity() {
    private val diaryListAdapter = DiaryListAdapter()
    private var checkedNum = 0;
    private val diaryRepository by lazy {
        DiaryRepoImpl(
            Injection.provideDiaryRepo(this)
        )
    }

    private var diaryList : ArrayList<Any> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        initRecycleview()
        changeClickData()
        changeDeleteText()
    }

    fun initRecycleview(){
        listDiaryRV.adapter = this.diaryListAdapter
        listDiaryRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var diveder = DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        diveder.setDrawable(this.getDrawable(R.drawable.divider_diary_list))
        listDiaryRV.addItemDecoration(diveder)

        closeClick()
        dataLoad()
    }

    @SuppressLint("CheckResult")
    fun dataLoad(){
        val formatYear = SimpleDateFormat("yyyy")
        val formatMonth = SimpleDateFormat("MM")

        val current = LocalDate.now()

        val currentYear = formatYear.parse(current.time.toString())
        val currentMonth = formatMonth.parse(current.toString()).time.toInt()

        while(diaryList.size <= 10) {
            var date = ModelDate(currentYear,currentMonth)
            diaryList.add(date)
            diaryRepository.gets(currentMonth, currentYear)
                .subscribe({
                    diaryList.addAll(it.diaries)
                }) {
                }
        }
        diaryListAdapter.setData(diaryList)
    }

    fun dataUpdate(){

    }


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

    fun deleteDiary(){

    }


    fun closeClick(){
        listDiaryCloseBtn.setOnClickListener{
            finish()
        }
    }
}
