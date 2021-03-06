package com.mashup.telltostar.ui.starlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.source.remote.request.ReqModifyConstellationDto
import com.mashup.telltostar.data.source.remote.response.Horoscope
import com.mashup.telltostar.databinding.ActivityStarListDetailBinding
import com.mashup.telltostar.ui.main.HoroscopeItemViewModel
import com.mashup.telltostar.ui.main.MainActivity
import com.mashup.telltostar.util.PrefUtil
import kotlinx.android.synthetic.main.activity_star_list_detail.*
import java.text.SimpleDateFormat
import java.util.*

class StarListDetailActivity : AppCompatActivity() {
    private lateinit var reqModifyConstellationDto : ReqModifyConstellationDto
    private lateinit var name : String
    private val horoscopeRepository = Injection.provideHoroscopeRepo()
    private val userChangeRepository = Injection.provideUserChangeRepo()

    lateinit var binding: ActivityStarListDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_star_list_detail)

        val intent : Intent = getIntent()
        name = intent.extras.getString("name")
        reqModifyConstellationDto = ReqModifyConstellationDto(name)

        getHoroscope()
        btnBackClick()
        changeHoroscope()
    }

    fun bindActivity(horoscope: Horoscope){
        with(binding) {
            detailStarNameTV.text = horoscope.constellation
            detailStarDateTV.text = changeFormat(horoscope.date)
            detailStarDiaryTV.text = horoscope.content

            binding.horoscopeInfoViewModel = HoroscopeItemViewModel(horoscope)
        }

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

    fun changeHoroscope(){

        detailStarChangeStarBtn.setOnClickListener {
            userChangeRepository.setHoroscope(name)
                .subscribe({
                    PrefUtil.put(PrefUtil.CONSTELLATION, name)
                    MainActivity.startMainActivityRestart(this@StarListDetailActivity)
                }){
                    val errorMsg = Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT)
                    errorMsg.show()
                }
        }
    }

}
