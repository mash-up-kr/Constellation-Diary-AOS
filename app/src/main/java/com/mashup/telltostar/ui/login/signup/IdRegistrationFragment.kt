package com.mashup.telltostar.ui.login.signup

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import com.mashup.telltostar.di.SignUpComponent
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.ui.myconstellation.MyConstellationActivity
import com.mashup.telltostar.util.VibratorUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_id_registration.*
import kotlinx.android.synthetic.main.fragment_id_registration.view.*
import java.util.concurrent.TimeUnit

class IdRegistrationFragment(
    private val mSignUpComponent: SignUpComponent
) : Fragment() {

    private val mEmailVerificationViewModel by lazy {
        mSignUpComponent.emailVerificationViewModel()
    }
    private val mIdRegistrationViewModel by lazy {
        (activity as LoginActivity).mIdRegistrationComponent.idRegistrationViewModel()
    }
    private lateinit var mRootView: View
    private val mCompositeDisposable by lazy {
        CompositeDisposable()
    }
    private val mPasswordTransformationMethod by lazy {
        CustomPasswordTransformationMethod()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_id_registration, container, false)

        mRootView = rootView

        timber.log.Timber.d(mIdRegistrationViewModel.hashCode().toString())

        setListeners()
        setViewModelObserver()

        return rootView
    }

    private fun setListeners() {
        mRootView.idDuplicationCheckButton.setOnClickListener {
            performCheckIdDuplicationButtonClick()
        }
        mRootView.passwordVisibilityImageView.setOnClickListener {
            performVisibilityImageViewClick()
        }
        mRootView.passwordInvisibilityImageView.setOnClickListener {
            performInVisibilityImageViewClick()
        }
        mRootView.passwordConfirmVisibilityImageView.setOnClickListener {
            performConfirmVisibilityImageViewClick()
        }
        mRootView.passwordConfirmInvisibilityImageView.setOnClickListener {
            performConfirmInvisibilityImageViewClick()
        }
    }

    private fun performCheckIdDuplicationButtonClick() {
        clearIdInputWarning()
        mIdRegistrationViewModel.requestCheckIdDuplication(mRootView.idEditText.text.toString())
    }

    private fun performVisibilityImageViewClick() {
        mIdRegistrationViewModel.requestPasswordInvisible()
        mRootView.passwordVisibilityImageView.visibility = View.GONE
        mRootView.passwordInvisibilityImageView.visibility = View.VISIBLE
        moveEditTextCursorEnd(mRootView.passwordEditText)
    }

    private fun performInVisibilityImageViewClick() {
        mIdRegistrationViewModel.requestPasswordVisible()
        mRootView.passwordVisibilityImageView.visibility = View.VISIBLE
        mRootView.passwordInvisibilityImageView.visibility = View.GONE
        moveEditTextCursorEnd(mRootView.passwordEditText)
    }

    private fun performConfirmVisibilityImageViewClick() {
        mIdRegistrationViewModel.requestConfirmPasswordInvisible()
        mRootView.passwordConfirmVisibilityImageView.visibility = View.GONE
        mRootView.passwordConfirmInvisibilityImageView.visibility = View.VISIBLE
        moveEditTextCursorEnd(mRootView.passwordConfirmEditText)
    }

    private fun performConfirmInvisibilityImageViewClick() {
        mIdRegistrationViewModel.requestConfirmPasswordVisible()
        mRootView.passwordConfirmVisibilityImageView.visibility = View.VISIBLE
        mRootView.passwordConfirmInvisibilityImageView.visibility = View.GONE
        moveEditTextCursorEnd(mRootView.passwordConfirmEditText)
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

    private fun setViewModelObserver() {
        mIdRegistrationViewModel.isPasswordVisible.observe(this, Observer {
            mRootView.passwordEditText.transformationMethod =
                if (it) HideReturnsTransformationMethod.getInstance()
                else mPasswordTransformationMethod
        })
        mIdRegistrationViewModel.isConfirmPasswordVisible.observe(this, Observer {
            mRootView.passwordConfirmEditText.transformationMethod =
                if (it) HideReturnsTransformationMethod.getInstance()
                else mPasswordTransformationMethod
        })

        activity?.let { activity ->
            mIdRegistrationViewModel.shouldIdDuplicationCheck.observe(
                this@IdRegistrationFragment,
                Observer {
                    clearIdInputWarning()

                    if (it) {
                        notDuplicationCheckedIdWarningTextView.visibility = View.VISIBLE
                        mRootView.idEditText.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.coral))

                        VibratorUtil.vibrate(activity)
                    } else {
                        notDuplicationCheckedIdWarningTextView.visibility = View.GONE
                        mRootView.idEditText.backgroundTintList = null
                    }
                })
            mIdRegistrationViewModel.isInputIdWarningTextViewVisible.observe(
                this,
                Observer { isVisible ->
                    clearIdInputWarning()

                    if (isVisible) mRootView.inputIdWarningTextView.visibility = View.VISIBLE
                    else mRootView.inputIdWarningTextView.visibility = View.GONE

                    mIdRegistrationViewModel.isAvailableId.value?.let { isAvailableId ->
                        mRootView.idEditText.backgroundTintList =
                            if (isVisible || !isAvailableId)
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.coral
                                    )
                                )
                            else if (isAvailableId)
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.kelly_green
                                    )
                                )
                            else
                                null
                    }

                    if (isVisible) {
                        vibrate()
                    }
                })
            mIdRegistrationViewModel.isInputPasswordWarningTextViewVisible.observe(this, Observer {
                if (it) mRootView.inputPasswordWarningTextView.visibility = View.VISIBLE
                else mRootView.inputPasswordWarningTextView.visibility = View.GONE

                mRootView.passwordEditText.backgroundTintList =
                    if (it)
                        ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.coral))
                    else
                        null

                if (it) {
                    vibrate()
                }
            })
            mIdRegistrationViewModel.isInputConfirmPasswordWarningTextViewVisible.observe(
                this,
                Observer {
                    clearPasswordConfirmInputWarning()

                    if (it) {
                        mRootView.inputPasswordConfirmWarningTextView.visibility = View.VISIBLE
                    } else {
                        mRootView.inputPasswordConfirmWarningTextView.visibility = View.GONE
                    }

                    mRootView.passwordConfirmEditText.backgroundTintList =
                        if (it)
                            ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.coral))
                        else
                            null

                    if (it) {
                        vibrate()
                    }
                })
            mIdRegistrationViewModel.isTwoPasswordIdentical.observe(this, Observer { isIdentical ->
                clearPasswordConfirmInputWarning()

                timber.log.Timber.d("isTwoPasswordIdentical: $isIdentical")

                mRootView.inputPasswordNotIdenticalWarningTextView.visibility =
                    if (isIdentical) View.GONE
                    else View.VISIBLE

                mRootView.passwordConfirmEditText.backgroundTintList =
                    if (isIdentical)
                        null
                    else
                        ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.coral))

                if (!isIdentical) {
                    vibrate()
                } else {
                    with(mEmailVerificationViewModel) {
                        mToken?.let { token ->
                            mIdRegistrationViewModel.getFcmToken()?.let { fcmToken ->
                                if (mToken.isNullOrEmpty().not() && fcmToken.isNotEmpty()) {
                                    mVerifiedEmailObservable.get()?.let { userEmail ->
                                        MyConstellationActivity.startMyConstellationForSignUp(
                                            activity,
                                            mRootView.idEditText.text.toString(),
                                            userEmail,
                                            fcmToken,
                                            mRootView.passwordEditText.text.toString(),
                                            token
                                        )
                                    }

                                    activity.overridePendingTransition(
                                        R.anim.enter_from_right,
                                        R.anim.exit_to_left
                                    )
                                    activity.finish()
                                }
                            }
                        }
                    }
                }
            })
            mIdRegistrationViewModel.isAvailableId.observe(this, Observer {
                clearIdInputWarning()

                if (it) {
                    mRootView.idEditText.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                activity,
                                R.color.kelly_green
                            )
                        )
                    mRootView.availableIdTextView.visibility = View.VISIBLE
                } else {
                    mRootView.idEditText.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                activity,
                                R.color.coral
                            )
                        )
                    mRootView.duplicateIdWarningTextView.visibility = View.VISIBLE

                    vibrate()
                }
            })
            mIdRegistrationViewModel.isNotIdPatternWarningTextViewVisible.observe(this, Observer {
                timber.log.Timber.d("onChanged() - $it")

                mRootView.notIdPatternWarningTextView.visibility =
                    if (it) View.VISIBLE
                    else View.GONE
                mRootView.idEditText.backgroundTintList =
                    if (it)
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                activity,
                                R.color.coral
                            )
                        )
                    else
                        null

                if (it) {
                    vibrate()
                }
            })
            mIdRegistrationViewModel.isNotPasswordPatternWarningTextViewVisible.observe(
                this,
                Observer {
                    notPasswordPatternWarningTextView.visibility =
                        if (it) View.VISIBLE
                        else View.GONE
                    passwordEditText.backgroundTintList =
                        if (it) ColorStateList.valueOf(
                            ContextCompat.getColor(
                                activity,
                                R.color.coral
                            )
                        )
                        else
                            null

                    if (it) {
                        vibrate()
                    }
                })
        }
    }

    fun performStartStarStarDiaryButtonClick() {
        mIdRegistrationViewModel.requestCheckTwoPasswordIdentical(
            mRootView.idEditText.text.toString(),
            mRootView.passwordEditText.text.toString(),
            mRootView.passwordConfirmEditText.text.toString()
        )
    }

    private fun clearIdInputWarning() {
        mRootView.idEditText.backgroundTintList = null
        mRootView.inputIdWarningTextView.visibility = View.GONE
        mRootView.availableIdTextView.visibility = View.GONE
        mRootView.duplicateIdWarningTextView.visibility = View.GONE
        mRootView.notDuplicationCheckedIdWarningTextView.visibility = View.GONE
    }

    private fun clearPasswordConfirmInputWarning() {
        mRootView.passwordConfirmEditText.backgroundTintList = null
        mRootView.inputPasswordConfirmWarningTextView.visibility = View.GONE
        mRootView.inputPasswordNotIdenticalWarningTextView.visibility = View.GONE
    }

    private fun vibrate() {
        context?.let {
            VibratorUtil.vibrate(it)
        }
    }

    override fun onDestroyView() {
        mCompositeDisposable.clear()

        super.onDestroyView()
    }
}
