package com.mashup.telltostar.ui.diarylist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.telltostar.R
import com.mashup.telltostar.data.ModelDate
import kotlinx.android.synthetic.main.activity_diary_list.*

class DiaryListActivity : AppCompatActivity() {
    private val diaryListAdapter = DiaryListAdapter()
    private var checkedNum = 0;
    private val data_1 = Temp_diary(0, "title", "2020-01-19T18:02:21.786Z", false, false)
    private val data_2 = Temp_diary(0, "title", "2020-01-18T18:02:21.786Z", false, false)
    private val data_3 =
        Temp_diary(0, "title", "2020-01-17T18:02:21.786Z", false, false)//"2020-01-19T18:02:21.786Z"
    private var date_1: ModelDate = ModelDate(2020, 1)
    private var date_2: ModelDate = ModelDate(2020, 2)


    private var diaryList: ArrayList<Any> = arrayListOf(
        date_1,
        data_1,
        data_2,
        data_3,
        data_1,
        date_2,
        data_1,
        data_2,
        data_3,
        data_1,
        date_1,
        data_1,
        data_2,
        data_3,
        data_1,
        date_2,
        data_1,
        data_2,
        data_3,
        data_1
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        initRecycleview()
        changeClickData()
        changeDeleteText()
    }

    fun initRecycleview() {
        listDiaryRV.adapter = this.diaryListAdapter
        listDiaryRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var diveder = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        diveder.setDrawable(this.getDrawable(R.drawable.divider_diary_list))
        listDiaryRV.addItemDecoration(diveder)

        setData()
        closeClick()
    }

    fun setData() {
        diaryListAdapter.setData(diaryList)
    }


    fun changeClickData() {
        listDiarySelectTV.let { btn ->
            btn.setOnClickListener {
                diaryListAdapter.let { adapter ->
                    if (btn.text.equals(getString(R.string.select))) {
                        adapter.notifyItemRangeChanged(0, adapter.itemCount, true)
                        btn.setText(getString(R.string.delete))
                        val red = ContextCompat.getColor(this, R.color.grapefruit)
                        btn.setTextColor(red)
                    } else {
                        adapter.notifyItemRangeChanged(0, adapter.itemCount, false)
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

    fun changeDeleteText() {
        diaryListAdapter.setOnItemClickListener(object : DiaryListAdapter.OnItemClickListener {
            override fun onItemClick(isChecked: Boolean) {
                if (isChecked == true) {
                    checkedNum++
                    listDiarySelectTV.text = checkedNum.toString() + getString(R.string.delete)
                } else {
                    checkedNum--
                    if (checkedNum == 0) {
                        listDiarySelectTV.setText(getString(R.string.delete))
                    } else {
                        listDiarySelectTV.text = checkedNum.toString() + getString(R.string.delete)
                    }
                }
            }

        })
    }

    fun deleteDiary() {

    }


    fun closeClick() {
        listDiaryCloseBtn.setOnClickListener {
            finish()
        }
    }
}
