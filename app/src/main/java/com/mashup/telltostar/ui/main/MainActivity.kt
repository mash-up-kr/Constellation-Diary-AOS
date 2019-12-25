package com.mashup.telltostar.ui.main

import android.annotation.SuppressLint
import android.app.Activity
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
import com.mashup.telltostar.data.Diary
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.repository.DiaryRepository
import com.mashup.telltostar.ui.diary.DiaryEditActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import kotlinx.android.synthetic.main.main_contents.*
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
            DiaryRepository(
                Injection.provideDiaryRepository(this)
            ),
            compositeDisposable
        )

        initToolbar()
        initBottomSheet()
        initButton()

        presenter.loadTodayDiary()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbarMain as Toolbar)
        supportActionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initBottomSheet() {
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        drawer_container.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    }
                    else -> {
                        drawer_container.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    }
                }
            }
        })
    }

    private fun initButton() {
        tvMainContentsDescription.setOnClickListener {
            presenter.startDiary()
        }
        btnBottomsheetEditDiary.setOnClickListener {
            showWriteDiary()
        }
    }

    override fun showDiaryTitle(title: String) {
        tvMainContentsTitle.text = title
        tvMainContentsDescription.text = resources.getString(R.string.edit_diary)

    }

    override fun showQuestionTitle() {
        tvMainContentsTitle.text = resources.getString(R.string.main_question_title)
        tvMainContentsDescription.text = resources.getString(R.string.write_diary)
    }

    override fun showEditDiary(diary: Diary) {
        DiaryEditActivity.startDiaryEditActivity(
            this,
            REQUEST_DIARY_EDIT,
            diary
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("requestCode ; $requestCode , resultCode : $resultCode , data : $data")
        if (requestCode == REQUEST_DIARY_EDIT) {
            if (resultCode == Activity.RESULT_OK) {
                val diary = data?.getParcelableExtra<Diary>(DiaryEditActivity.EXTRA_DIARY)
                if (diary != null) {
                    presenter.changeDiary(diary)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawer_container.openDrawer(Gravity.LEFT)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("WrongConstant")
    override fun onBackPressed() {
        if (drawer_container.isDrawerOpen(GravityCompat.START)) {
            drawer_container.closeDrawer(Gravity.START)
        } else if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val REQUEST_DIARY_EDIT = 0x001
    }
}
