package com.mashup.telltostar.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.source.remote.response.Horoscope
import com.mashup.telltostar.ui.diary.DiaryEditActivity
import com.mashup.telltostar.ui.diarylist.DiaryListActivity
import com.mashup.telltostar.ui.myconstellation.MyConstellationActivity
import com.mashup.telltostar.ui.setting.SettingActivity
import com.mashup.telltostar.util.ConstellationUtil
import com.mashup.telltostar.util.PrefUtil
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import kotlinx.android.synthetic.main.fragment_bottomsheet.view.*
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.android.synthetic.main.main_contents.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast
import timber.log.Timber


class MainActivity : AppCompatActivity(), MainContract.View {

    override lateinit var presenter: MainContract.Presenter

    private val sheetBehavior by lazy {
        BottomSheetBehavior.from(llBottomSheetView)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(
            this,
            Injection.provideDailyQuestionRepo(),
            Injection.provideHoroscopeRepo(),
            Injection.provideUserRepo(),
            compositeDisposable
        )

        initToolbar()
        initBottomSheet()
        initNavigationView()
        initButton()

        presenter.loadDailyQuestion()
        presenter.loadHoroscope()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.run {
            val type = getStringExtra(TYPE)
            Timber.d("type : $type")
            if (type == TYPE_RESTART) {
                presenter.loadDailyQuestion()
                presenter.loadHoroscope()
                removeExtra(TYPE)
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbarMain as Toolbar)
        supportActionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbarImageView.setImageResource(
            ConstellationUtil.getIcon(resources, PrefUtil.get(PrefUtil.CONSTELLATION, ""))
        )
    }

    private fun initBottomSheet() {
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        drawerContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    }
                    else -> {
                        drawerContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    }
                }
            }
        })


        val constellation = PrefUtil.get(PrefUtil.CONSTELLATION, "")

        with(llBottomSheetView) {
            tvBottomSheetHoroscopeTitle.text = "$constellation 운세"
            tvBottomSheetDate.text = TimeUtil.getDateFromUTC(TimeUtil.getUTCDate())
        }

    }

    private fun initNavigationView() {
        val constellation = PrefUtil.get(PrefUtil.CONSTELLATION, "")
        with(navigationView.getHeaderView(0)) {
            ivLogo.setImageResource(ConstellationUtil.getIconBlack(resources, constellation))
            tvMyConstellation.text = constellation
            tvConstellationDuration.text = ConstellationUtil.getDate(resources, constellation)
        }
    }

    private fun initButton() {
        tvMainContentsDescription.setOnClickListener {
            presenter.editDiary()
        }
        btnBottomsheetEditDiary.setOnClickListener {
            presenter.editDiary()
            closeBottomSheet()
        }

        with(navigationView.getHeaderView(0)) {
            constellationMenuTextView.setOnClickListener {
                closeNavigationView()
                showStarList()
            }
            diaryMenuTextView.setOnClickListener {
                closeNavigationView()
                showDiaryList()
            }
            settingMenuTextView.setOnClickListener {
                closeNavigationView()
                showSetting()
            }
        }
    }

    private fun closeNavigationView() {
        drawerContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun closeBottomSheet() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun showDiaryTitle(title: String) {
        tvMainContentsTitle.text = title
        tvMainContentsDescription.text = resources.getString(R.string.edit_diary) + " >"
        llBottomSheetView.btnBottomsheetEditDiary.text = resources.getString(R.string.edit_diary)
    }

    override fun showQuestionTitle(title: String) {
        tvMainContentsTitle.text = title
        tvMainContentsDescription.text = resources.getString(R.string.write_diary) + " >"
        llBottomSheetView.btnBottomsheetEditDiary.text = resources.getString(R.string.write_diary)
    }

    override fun showHoroscope(horoscope: Horoscope) {
        with(llBottomSheetView) {
            tvBottomSheetHoroscopeContents.text = horoscope.content
            tvBottomSheetClothes.text = horoscope.stylist
            tvBottomSheetNumber.text = horoscope.numeral
            tvBottomSheetWorkout.text = horoscope.exercise
            tvBottomSheetWord.text = horoscope.word
        }
    }

    override fun showEditDiary(diaryId: Int, horoscopeId: Int) {
        DiaryEditActivity.startDiaryEditActivity(
            this,
            REQUEST_DIARY_EDIT,
            diaryId,
            horoscopeId
        )
    }

    override fun showWriteDiary(horoscopeId: Int) {
        DiaryEditActivity.startDiaryWriteActivity(
            this,
            REQUEST_DIARY_EDIT,
            horoscopeId
        )
    }

    override fun showToast(message: String?) {
        if (message != null) {
            toast(message)
        }
    }

    override fun showStarList() {
        MyConstellationActivity.startMyConstellationForWatch(this)
    }

    override fun showDiaryList() {
        startActivity(
            Intent(this@MainActivity, DiaryListActivity::class.java)
        )
    }

    override fun showSetting() {
        startActivity(
            Intent(this@MainActivity, SettingActivity::class.java)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("requestCode ; $requestCode , resultCode : $resultCode , data : $data")
        if (requestCode == REQUEST_DIARY_EDIT) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.loadDailyQuestion()
            }
        }
    }

    @SuppressLint("WrongConstant")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawerContainer.openDrawer(Gravity.START)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("WrongConstant")
    override fun onBackPressed() {
        if (drawerContainer.isDrawerOpen(GravityCompat.START)) {
            drawerContainer.closeDrawer(Gravity.START)
        } else if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val REQUEST_DIARY_EDIT = 0x001

        const val TYPE = "type"
        const val TYPE_RESTART = "restart"

        fun startMainActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        fun startMainActivityRestart(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                putExtra(TYPE, TYPE_RESTART)
            })
        }
    }

}
