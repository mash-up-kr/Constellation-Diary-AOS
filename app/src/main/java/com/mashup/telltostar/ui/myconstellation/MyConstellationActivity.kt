package com.mashup.telltostar.ui.myconstellation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.ui.main.MainActivity
import com.mashup.telltostar.ui.myconstellation.adapter.ConstellationAdapter
import com.mashup.telltostar.ui.starlist.StarListDetailActivity
import com.mashup.telltostar.util.ConstellationUtil
import com.mashup.telltostar.util.PrefUtil
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_my_constellation.*
import org.jetbrains.anko.toast
import timber.log.Timber

class MyConstellationActivity : AppCompatActivity(),
    DiscreteScrollView.ScrollStateChangeListener<ConstellationAdapter.ConstellationViewHolder>,
    DiscreteScrollView.OnItemChangedListener<ConstellationAdapter.ConstellationViewHolder> {

    private val signRepository by lazy {
        Injection.provideSignRepo()
    }

    private val adapter by lazy {
        ConstellationAdapter(
            ConstellationUtil.getConstellations(this.resources)
        )
    }

    private val constellationAdapter
            by lazy { InfiniteScrollAdapter.wrap(adapter) }

    private lateinit var type: Type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_constellation)

        initType()
        initButton()
        initCustomView()
    }

    private fun initType() {
        val type = intent?.getSerializableExtra(KEY_TYPE) as? Type
        if (type != null) {
            this.type = type
            Timber.d("type : $type")

            val title = when (type) {
                Type.SIGNUP -> getString(R.string.start_star_star)
                Type.WATCH -> getString(R.string.watch_constellation)
            }

            btnMyConstellationStart.text = title
        } else {
            error("type exception")
        }
    }

    private fun initButton() {
        btnMyConstellationStart.setOnClickListener {
            val realPosition = constellationAdapter.getRealPosition(customScrollView.currentItem)
            val constellation = adapter.getConstellation(realPosition)

            when (type) {
                Type.SIGNUP -> {
                    signUp(constellation)
                }
                Type.WATCH -> {
                    startActivity(Intent(this, StarListDetailActivity::class.java).apply {
                        putExtra("name", constellation)
                    })
                }
            }
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
            val fcmToken = getStringExtra(KEY_SIGN_UP_FCM_TOKEN)
            val password = getStringExtra(KEY_SIGN_UP_PASSWORD)
            val token = getStringExtra(KEY_SIGN_UP_TOKEN)

            Timber.d("constellation : $constellation , id : $id , email : $email , fcmToken , : $fcmToken, password : $password , token : $token")

            signRepository.sighUp(
                constellation = constellation,
                email = email,
                fcmToken = fcmToken,
                password = password,
                userId = id,
                token = token
            ).doOnSubscribe {
                showLoading()
            }.doOnSuccess {
                hideLoading()
            }.doOnError {
                hideLoading()
            }.subscribe({
                val authenticationToken = it.tokens.authenticationToken
                val refreshToken = it.tokens.refreshToken

                PrefUtil.put(PrefUtil.CONSTELLATION, constellation)
                PrefUtil.put(PrefUtil.AUTHENTICATION_TOKEN, authenticationToken)
                PrefUtil.put(PrefUtil.REFRESH_TOKEN, refreshToken)

                MainActivity.startMainActivity(this@MyConstellationActivity)
                finish()
            }) {
                Timber.e(it)
                toast(it.message ?: "error")
            }
        }
    }

    private fun showLoading() {
        pbMyConstellationStart.visibility = View.VISIBLE
        btnMyConstellationStart.isEnabled = true
    }

    private fun hideLoading() {
        pbMyConstellationStart.visibility = View.GONE
        btnMyConstellationStart.isEnabled = false
    }

    override fun onScrollStart(
        holder: ConstellationAdapter.ConstellationViewHolder,
        adapterPosition: Int
    ) {
        holder.hideLine()
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
        if (holder != null) {
            changeConstellationInfo(holder.getTitle())
            holder.showLine()
        }
    }

    private fun changeConstellationInfo(constellation: String) {
        tvMyConstellationInfo.text = ConstellationUtil.getInfo(resources, constellation)
    }

    enum class Type {
        SIGNUP, WATCH
    }

    companion object {

        private const val KEY_TYPE = "type"

        private const val KEY_SIGN_UP_ID = "sign_up_id"
        private const val KEY_SIGN_UP_EMAIL = "sign_up_email"
        private const val KEY_SIGN_UP_FCM_TOKEN = "sign_up_fcm_token"
        private const val KEY_SIGN_UP_PASSWORD = "sign_up_password"
        private const val KEY_SIGN_UP_TOKEN = "sign_up_token"

        fun startMyConstellationForSignUp(
            context: Context,
            userId: String,
            email: String,
            fcmToken: String,
            password: String,
            token: String
        ) {
            context.startActivity(
                Intent(context, MyConstellationActivity::class.java).apply {
                    putExtra(KEY_TYPE, Type.SIGNUP)
                    putExtra(KEY_SIGN_UP_ID, userId)
                    putExtra(KEY_SIGN_UP_EMAIL, email)
                    putExtra(KEY_SIGN_UP_FCM_TOKEN, fcmToken)
                    putExtra(KEY_SIGN_UP_PASSWORD, password)
                    putExtra(KEY_SIGN_UP_TOKEN, token)
                }
            )
        }

        fun startMyConstellationForWatch(
            context: Context
        ) {
            context.startActivity(
                Intent(context, MyConstellationActivity::class.java).apply {
                    putExtra(KEY_TYPE, Type.WATCH)
                }
            )
        }
    }

}
