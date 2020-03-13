package com.mashup.telltostar.ui.login

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.iid.FirebaseInstanceId
import com.mashup.telltostar.R
import com.mashup.telltostar.di.DaggerLoginComponent
import com.mashup.telltostar.ui.login.forgotid.ForgotIdFragment
import com.mashup.telltostar.ui.login.forgotpassword.ForgotPasswordFragment
import com.mashup.telltostar.ui.login.signup.CustomPasswordTransformationMethod
import com.mashup.telltostar.ui.main.MainActivity
import com.mashup.telltostar.util.PrefUtil
import com.mashup.telltostar.util.VibratorUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_forgot_id_password.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginFragment: Fragment() {
    private lateinit var mRootView: View
    private lateinit var mFragmentListener: LoginActivity.FragmentListener

    @Inject
    lateinit var mLoginViewModel: LoginViewModel
    private val mIdRegistrationViewModel by lazy {
        (activity as LoginActivity).mIdRegistrationComponent.idRegistrationViewModel()
    }
    private var mFcmToken: String? = null
    private val mCompositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerLoginComponent.builder().build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        mRootView = rootView

        initFcmToken()
        setListeners()
        setViewModelObserver()

        return rootView
    }

    private fun initFcmToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.token?.let { token ->
                    mLoginViewModel.setFcmToken(token)
                    mIdRegistrationViewModel.setFcmToken(token)
                }
            } else if (it.isCanceled) {
                timber.log.Timber.d("firebase token getting canceled")
                mLoginViewModel.setFcmToken(null)
                mIdRegistrationViewModel.setFcmToken(null)
            }
        }
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners() {
        with(activity as LoginActivity) {
            mRootView.signUpTextViewContainer.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mSignUpFragment,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            }
            mRootView.forgotIdTextViewContainer.setOnClickListener {
                performForgotIdPasswordTextViewClick()
            }
            mRootView.closeImageViewContainer.setOnClickListener {
                mFragmentListener.closeBottomSheet()
            }
            mRootView.startStarStarDiaryButton.setOnClickListener {
                performStartButtonClick()
            }
            mRootView.idEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    mFragmentListener.expandBottomSheet()
                }
            }
            with(mRootView.passwordEditText) {
                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        mFragmentListener.expandBottomSheet()
                    }
                }
                setOnEditorActionListener { v, actionId, event ->
                    performStartButtonClick()

                    true
                }
            }
            mRootView.passwordVisibilityLinearLayout.setOnClickListener {
                performPasswordVisibilityLinearLayoutClick()
            }
        }
    }

    private fun setViewModelObserver() {
        with(mLoginViewModel) {
            context?.let { context ->
                isInputIdWarningTextViewVisible.observe(this@LoginFragment, Observer {
                    if (it) {
                        VibratorUtil.vibrate(mRootView.context)
                        mRootView.idEditText.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.coral))
                        mRootView.inputIdWarningTextView.visibility = View.VISIBLE
                    } else {
                        mRootView.idEditText.backgroundTintList = null
                        mRootView.inputIdWarningTextView.visibility = View.GONE
                    }
                })
                isInputPasswordWarningTextViewVisible.observe(this@LoginFragment, Observer {
                    if (it) {
                        VibratorUtil.vibrate(mRootView.context)
                        mRootView.passwordEditText.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.coral))
                        mRootView.inputPasswordWarningTextView.visibility = View.VISIBLE
                    } else {
                        mRootView.passwordEditText.backgroundTintList = null
                        mRootView.inputPasswordWarningTextView.visibility = View.GONE
                    }
                })
                isInputPasswordVisible.observe(this@LoginFragment, Observer {
                    if (it) {
                        mRootView.passwordVisibilityImageView.visibility = View.VISIBLE
                        mRootView.passwordInvisibilityImageView.visibility = View.GONE
                    } else {
                        mRootView.passwordVisibilityImageView.visibility = View.GONE
                        mRootView.passwordInvisibilityImageView.visibility = View.VISIBLE
                    }

                    mRootView.passwordEditText.transformationMethod =
                        if (it) HideReturnsTransformationMethod.getInstance()
                        else CustomPasswordTransformationMethod()
                })
                isLoginErrorButtonVisible.observe(this@LoginFragment, Observer {
                    loginErrorButton.visibility =
                        if (it) View.VISIBLE
                        else View.GONE
                })
                isLoggedIn.observe(this@LoginFragment, Observer {
                    if (it) {
                        if (mAuthenticationToken.isNotEmpty() && mRefreshToken.isNotEmpty()) {
                            PrefUtil.put(PrefUtil.AUTHENTICATION_TOKEN, mAuthenticationToken)
                            PrefUtil.put(PrefUtil.REFRESH_TOKEN, mRefreshToken)

                            PrefUtil.put(PrefUtil.CONSTELLATION, constellation)

                            PrefUtil.put(PrefUtil.HOROSCOPE_ALARM_FLAG, horoscopeAlarmFlag)
                            PrefUtil.put(PrefUtil.HOROSCOPE_TIME, horoscopeAlarmTime)

                            PrefUtil.put(PrefUtil.QUESTION_ALARM_FLAG, questionAlarmFlag)
                            PrefUtil.put(PrefUtil.QUESTION_TIME, questionAlarmTime)
                        }

                        activity?.let { activity ->
                            activity.startActivity(
                                Intent(
                                    context,
                                    MainActivity::class.java
                                ).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                })
                            activity.overridePendingTransition(
                                R.anim.enter_from_right,
                                R.anim.exit_to_left
                            )
                            activity.finish()
                        }
                    }
                })
            }
        }
    }

    private fun performStartButtonClick() {
        mLoginViewModel.getFcmToken()?.let {
            mLoginViewModel.requestLogin(
                "KST", // 현재 기기 위치에 따른 시간대 넘기도록 변경해야함
                mRootView.idEditText.text.toString(),
                mRootView.passwordEditText.text.toString()
            )

            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(
                    mRootView.passwordEditText.windowToken,
                    0
                )
        }
    }

    private fun performForgotIdPasswordTextViewClick() {
        val dialog = AlertDialog
            .Builder(mRootView.context)
            .create()
        val dialogView =
            LayoutInflater
                .from(mRootView.context)
                .inflate(R.layout.dialog_forgot_id_password, null).apply {
                    this.forgotIdPasswordDialogIdTextView.setOnClickListener {
                        mFragmentListener.replaceFragment(
                            initForgotIdFragment(),
                            R.anim.enter_from_right,
                            R.anim.exit_to_left
                        )
                        dialog.dismiss()
                    }
                    this.forgotIdPasswordDialogPasswordTextView.setOnClickListener {
                        mFragmentListener.replaceFragment(
                            ForgotPasswordFragment.createForgotPasswordFragmentWithListener(
                                mFragmentListener
                            ),
                            R.anim.enter_from_right,
                            R.anim.exit_to_left
                        )

                        dialog.dismiss()
                    }
                }

        dialog.also {
            it.setView(dialogView)
            it.show()
        }
    }

    private fun performPasswordVisibilityLinearLayoutClick() {
        mLoginViewModel.requestTogglePasswordVisibility()
        moveEditTextCursorEnd(mRootView.passwordEditText)
    }

    private fun moveEditTextCursorEnd(editText: EditText) {
        mCompositeDisposable.add(
            Single.just(1)
                .subscribeOn(Schedulers.io())
                .delay(50, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    editText.setSelection(editText.text.length)
                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun initForgotIdFragment() = ForgotIdFragment().apply {
        setFragmentListener(mFragmentListener)
    }
}
