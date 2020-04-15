package com.mashup.telltostar.ui.developer

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_developer.*

class DeveloperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)

        btnDeveloperClose.setOnClickListener {
            onBackPressed()
        }

        val content = resources.getString(R.string.developer_content)

        val htmlContent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(content)
        }

        tvDeveloperContent.text = htmlContent
    }
}
