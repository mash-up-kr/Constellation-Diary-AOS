package com.mashup.telltostar.ui.diary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mashup.telltostar.R
import timber.log.Timber

class DiaryEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_edit)

        val type = intent.getSerializableExtra(EXTRA_TYPE) as EditType
        Timber.d("type : $type")

    }

    enum class EditType {
        NEW, MODIFY
    }

    companion object {

        const val EXTRA_TYPE = "type"

        fun startDiaryEditActivity(context: Context, type: EditType) {
            context.startActivity(
                Intent(context, DiaryEditActivity::class.java).apply {
                    putExtra(EXTRA_TYPE, type)
                }
            )
        }
    }
}
