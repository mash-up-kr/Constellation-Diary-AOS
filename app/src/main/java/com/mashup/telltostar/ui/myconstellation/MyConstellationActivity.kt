package com.mashup.telltostar.ui.myconstellation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.main.MainActivity
import com.mashup.telltostar.ui.myconstellation.adapter.ConstellationAdapter
import com.mashup.telltostar.util.ConstellationUtil
import com.mashup.telltostar.util.PrefUtil
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_my_constellation.*
import timber.log.Timber

class MyConstellationActivity : AppCompatActivity(),
    DiscreteScrollView.ScrollStateChangeListener<ConstellationAdapter.ConstellationViewHolder>,
    DiscreteScrollView.OnItemChangedListener<ConstellationAdapter.ConstellationViewHolder> {

    private val adapter by lazy {
        ConstellationAdapter(
            ConstellationUtil.getConstellations(this.resources)
        )
    }

    private val constellationAdapter
            by lazy { InfiniteScrollAdapter.wrap(adapter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_constellation)

        initButton()
        initCustomView()
    }

    private fun initButton() {
        btnMyConstellationStart.setOnClickListener {
            val realPosition = constellationAdapter.getRealPosition(customScrollView.currentItem)
            signUp(adapter.getConstellation(realPosition))
        }
    }

    private fun initCustomView() {

        with(customScrollView) {
            //infinite scroll
            adapter = constellationAdapter

            setOffscreenItems(3)
            setOverScrollEnabled(false)

            setItemTransformer(
                ScaleTransformer.Builder()
                    .setMaxScale(1.0f)
                    .setMinScale(0.8f)
                    .setPivotX(Pivot.X.CENTER)
                    .setPivotY(Pivot.Y.CENTER)
                    .build()
            )

            addScrollStateChangeListener(this@MyConstellationActivity)
            addOnItemChangedListener(this@MyConstellationActivity)
        }
    }

    private fun signUp(constellation: String) {
        intent?.run {
            val id = getStringExtra(KEY_SIGN_UP_ID)
            val email = getStringExtra(KEY_SIGN_UP_EMAIL)
            val password = getStringExtra(KEY_SIGN_UP_PASSWORD)

            Timber.d("constellation : $constellation , id : $id , email : $email , password : $password")

            PrefUtil.put(PrefUtil.CONSTELLATION, constellation)

            //TODO 회원 가입 성공 후 이동
            MainActivity.startMainActivity(this@MyConstellationActivity)
            finish()
        }
    }

    override fun onScrollStart(
        holder: ConstellationAdapter.ConstellationViewHolder,
        adapterPosition: Int
    ) {
        //holder.hideText()
    }

    override fun onScroll(
        currentPosition: Float,
        currentIndex: Int,
        newIndex: Int,
        currentHolder: ConstellationAdapter.ConstellationViewHolder?,
        newCurrent: ConstellationAdapter.ConstellationViewHolder?
    ) {
        if (currentHolder != null && newCurrent != null) {
            val position = Math.abs(currentPosition)
        }
    }

    override fun onScrollEnd(
        holder: ConstellationAdapter.ConstellationViewHolder,
        adapterPosition: Int
    ) {

    }


    override fun onCurrentItemChanged(
        holder: ConstellationAdapter.ConstellationViewHolder?,
        adapterPosition: Int
    ) {
        //holder?.showText()
    }

    companion object {

        private const val KEY_SIGN_UP_ID = "sign_up_id"
        private const val KEY_SIGN_UP_EMAIL = "sign_up_email"
        private const val KEY_SIGN_UP_PASSWORD = "sign_up_password"

        fun startMyConstellationForSignUp(
            context: Context,
            userId: String,
            email: String,
            password: String
        ) {
            context.startActivity(
                Intent(context, MyConstellationActivity::class.java).apply {
                    putExtra(KEY_SIGN_UP_ID, userId)
                    putExtra(KEY_SIGN_UP_EMAIL, email)
                    putExtra(KEY_SIGN_UP_PASSWORD, password)
                }
            )
        }
    }

}
