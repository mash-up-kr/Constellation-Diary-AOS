package com.mashup.telltostar.ui.diarylist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Diary
import kotlinx.android.synthetic.main.activity_diary_list.*

class DiaryListActivity : AppCompatActivity() {
    private val diaryListAdapter = DiaryListAdapter()
    private var diaryList : ArrayList<Diary> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        initRecycleview()
    }

    fun initRecycleview(){
        listDiaryRV.adapter = this.diaryListAdapter
        listDiaryRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var diveder : DividerItemDecoration = DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        diveder.setDrawable(this.getDrawable(R.drawable.divider_diary_list))
        listDiaryRV.addItemDecoration(diveder)

        setData()
    }

    fun setData(){
        diaryListAdapter.setItem(diaryList)
    }
}
