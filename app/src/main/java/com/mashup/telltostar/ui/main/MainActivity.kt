package com.mashup.telltostar.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.source.remote.response.Horoscope
import com.mashup.telltostar.eventbus.RxEventBusHelper
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
import kotlinx.android.synthetic.main.main_top_bar.*
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

        initBottomSheet()
        initButton()
        initBus()
        setConstellationTitle()

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
                setConstellationTitle()
                presenter.loadDailyQuestion()
                presenter.loadHoroscope()
                removeExtra(TYPE)
            } else if (type == TYPE_SHOW_HOROSCOPE) {
                showBottomSheet()
            }
        }
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
    }

    private fun initButton() {
        tvMainContentsDescription.setOnClickListener {
            presenter.editDiary()
        }
        btnBottomsheetEditDiary.setOnClickListener {
            presenter.editDiary()
            closeBottomSheet()
        }

        mainTopBarMenu.setOnClickListener {
            showNavigationView()
        }

        mainTopBarDiaryList.setOnClickListener {
            showDiaryList()
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

    private fun initBus() {
        RxEventBusHelper.diaryTitleBus.subscribe({
            presenter.loadDailyQuestion()
        }) {

        }.also {
            compositeDisposable.add(it)
        }
    }

    private fun showNavigationView() {
        drawerContainer.openDrawer(GravityCompat.START)
    }

    private fun closeNavigationView() {
        drawerContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun showBottomSheet() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun closeBottomSheet() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setConstellationTitle() {
        val constellation = PrefUtil.get(PrefUtil.CONSTELLATION, "")

        mainTopBarConstellation.setImageResource(
            ConstellationUtil.getIcon(resources, constellation)
        )

        with(navigationView.getHeaderView(0)) {
            ivLogo.setImageResource(ConstellationUtil.getIconBlack(resources, constellation))
            tvMyConstellation.text = constellation
            tvConstellationDuration.text = ConstellationUtil.getDate(resources, constellation)
        }

        with(llBottomSheetView) {
            tvBottomSheetHoroscopeTitle.text = "$constellation 운세"
            tvBottomSheetDate.text = TimeUtil.getKSTDateFromUTCDate(TimeUtil.getUTCDate())
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

    override fun showEditDiary(diaryId: Int) {
        DiaryEditActivity.startDiaryEditActivity(
            this,
            REQUEST_DIARY_EDIT,
            diaryId
        )
    }

    override fun showWriteDiary() {
        DiaryEditActivity.startDiaryWriteActivity(
            this,
            REQUEST_DIARY_EDIT
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
        const val TYPE_SHOW_HOROSCOPE = "show_horoscope"

        fun startMainActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        fun startMainActivityRestart(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                putExtra(TYPE, TYPE_RESTART)
            })
        }

        fun startMainActivityWithHoroscope(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                putExtra(TYPE, TYPE_SHOW_HOROSCOPE)
            })
        }
    }

}
