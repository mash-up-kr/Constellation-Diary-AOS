package com.mashup.telltostar.ui.diarylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Diary
import com.mashup.telltostar.data.Horoscope
import com.mashup.telltostar.data.ModelDate
import kotlinx.android.synthetic.main.activity_diary_list.*
import kotlinx.android.synthetic.main.fragment_diary_list.*

class DiaryListActivity : AppCompatActivity() {
    private val diaryListAdapter = DiaryListAdapter()
    private val Horo = Horoscope("dd")
    private val data_1 = Diary(0,"title","contents","2020-01-19T18:02:21.786Z","constellation",Horo)
    private val data_2 = Diary(0,"title","contents","2020-01-18T18:02:21.786Z","constellation",Horo)
    private val data_3 = Diary(0,"title","contents","2020-01-17T18:02:21.786Z","constellation",Horo)//"2020-01-19T18:02:21.786Z"
    private var date_1 : ModelDate = ModelDate(2020,1)
    private var date_2 : ModelDate = ModelDate(2020,2)


    private var diaryList : ArrayList<Any> = arrayListOf(date_1,data_1,data_2,data_3,data_1,date_2,data_1,data_2,data_3,data_1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        initRecycleview()
    }

    fun initRecycleview(){
        listDiaryRV.adapter = this.diaryListAdapter
        listDiaryRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var diveder = DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        diveder.setDrawable(this.getDrawable(R.drawable.divider_diary_list))
        listDiaryRV.addItemDecoration(diveder)

        setData()
        closeClick()
        selectClick()
    }

    fun setData(){
        diaryListAdapter.setData(diaryList)
    }

    fun selectClick(){

        listDiarySelectTV.setOnClickListener{
            if(listDiarySelectTV.text.equals("선택")){
                listDiarySelectTV.text = "삭제"
            }else if(listDiarySelectTV.text == "@string/delete"){

            }
        }
    }

    fun closeClick(){
        listDiaryCloseBtn.setOnClickListener{
            finish()
        }
    }
}
