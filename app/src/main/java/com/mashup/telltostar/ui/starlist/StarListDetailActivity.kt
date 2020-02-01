package com.mashup.telltostar.ui.starlist

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.StarList
import com.mashup.telltostar.data.source.HoroscopeRepository
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.request.ReqModifyConstellationDto
import com.mashup.telltostar.data.source.remote.response.Horoscope
import kotlinx.android.synthetic.main.activity_star_list_detail.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class StarListDetailActivity : AppCompatActivity() {
    private lateinit var reqModifyConstellationDto : ReqModifyConstellationDto
    private lateinit var name : String
    private var horoscopeRepository = Injection.provideHoroscopeRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star_list_detail)

        val intent : Intent = getIntent()
        name = intent.extras.getString("name")
        reqModifyConstellationDto = ReqModifyConstellationDto(name)

        getHoroscope()
        btnBackClick()
//        changeConstellation()
    }

    fun bindActivity(horoscope: Horoscope){
        detailStarNameTV.text = horoscope.constellation
        detailStarLuckTV.text = horoscope.stylist
        detailStarLoveTV.text = horoscope.numeral
        detailStarHealthTV.text = horoscope.exercise
        detailStarTimeTV.text = horoscope.word

        detailStarDateTV.text = changeFormat(horoscope.date)
        detailStarDiaryTV.text = horoscope.content
    }

    fun changeFormat(date :String):String{
        val beforeFormatter = SimpleDateFormat("yyyy-MM-dd")
        val afterFormatter = SimpleDateFormat("yyyy'년' M'월' d'일' '('EEE')'", Locale.KOREA)
        var beforeDate = beforeFormatter.parse(date)
        var afterDate = afterFormatter.format(beforeDate.time)

        return afterDate
    }

    fun btnBackClick(){
        detailStarBackBtn.setOnClickListener { finish() }
    }

    fun getHoroscope(){
        horoscopeRepository.get(name)
            .subscribe({
                bindActivity(it)
            }) {
            }.also {
            }
    }

//    @SuppressLint("TimberArgCount")
//    fun changeConstellation(){
//        detailStarChangeStarBtn.setOnClickListener{
//            ApiProvider
//                .provideUserChangeApi()
//                .constellation(
//                    reqModifyConstellationDto
//                )
//                .subscribe({
//                    Timber.d("succeed",name)
//                   //TODO(이진성) CLICK THE BUTTON THAT MODIFY CONSTELLATION
//                },{
//                    Timber.d("$it.message",it.message)
//                })
//        }
//    }
}
