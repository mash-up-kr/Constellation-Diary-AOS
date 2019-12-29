package com.mashup.telltostar.ui.starlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mashup.telltostar.R
import com.mashup.telltostar.data.StarList
import kotlinx.android.synthetic.main.activity_star_list_detail.*

class StarListDetailActivity : AppCompatActivity() {
    private var pos : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star_list_detail)

        val intent : Intent = getIntent()
        pos = intent.extras.getInt("position")

        bindActivity()
        btnBackClick()
    }

    fun bindActivity(){
        tvStarName.text = StarList().starName[pos]
    }

    fun btnBackClick(){
        btnBack.setOnClickListener { finish() }
    }
}
