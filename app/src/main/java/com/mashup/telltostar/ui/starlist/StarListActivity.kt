package com.mashup.telltostar.ui.starlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.telltostar.R
import com.mashup.telltostar.data.StarList
import kotlinx.android.synthetic.main.activity_star_list.*

class StarListActivity : AppCompatActivity(){
    private lateinit var starListAdapter: StarListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star_list)

        initRecycleview()

        listClose()
    }

    fun initRecycleview(){
        starListAdapter = StarListAdapter()

        list_rv.adapter = this.starListAdapter
        list_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setData()

        starListAdapter.setOnItemClickListener(object : StarListAdapter.OnItemClickListener{
            override fun onItemClick(view: View, pos: Int) {
                val intent  = Intent(this@StarListActivity,StarListDetailActivity::class.java)
                intent.putExtra("position",pos)

                startActivity(intent)
            }
        })
    }

    fun listClose(){
        list_close_btn.setOnClickListener { finish() }
    }

    fun setData(){
        starListAdapter.setImage(StarList().starImage)
        starListAdapter.setNamge(StarList().starName)
    }
}
