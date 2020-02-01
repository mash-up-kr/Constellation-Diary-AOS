package com.mashup.telltostar.ui.login

import android.content.Intent
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
import com.mashup.telltostar.ui.myconstellation.MyConstellationActivity
import com.mashup.telltostar.util.VibratorUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_id_registration.view.*
import java.util.concurrent.TimeUnit

class IdRegistrationFragment : Fragment() {

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
        IdRegistrationViewModel.requestCheckIdDuplication(mRootView.idEditText.text.toString())
    }

    private fun performVisibilityImageViewClick() {
        IdRegistrationViewModel.requestPasswordInvisible()
        mRootView.passwordVisibilityImageView.visibility = View.GONE
        mRootView.passwordInvisibilityImageView.visibility = View.VISIBLE
        moveEditTextCursorEnd(mRootView.passwordEditText)
    }

    private fun performInVisibilityImageViewClick() {
        IdRegistrationViewModel.requestPasswordVisible()
        mRootView.passwordVisibilityImageView.visibility = View.VISIBLE
        mRootView.passwordInvisibilityImageView.visibility = View.GONE
        moveEditTextCursorEnd(mRootView.passwordEditText)
    }

    private fun performConfirmVisibilityImageViewClick() {
        IdRegistrationViewModel.requestConfirmPasswordInvisible()
        mRootView.passwordConfirmVisibilityImageView.visibility = View.GONE
        mRootView.passwordConfirmInvisibilityImageView.visibility = View.VISIBLE
        moveEditTextCursorEnd(mRootView.passwordConfirmEditText)
    }

    private fun performConfirmInvisibilityImageViewClick() {
        IdRegistrationViewModel.requestConfirmPasswordVisible()
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
        IdRegistrationViewModel.isPasswordVisible.observe(this, Observer {
            mRootView.passwordEditText.transformationMethod =
                if (it) HideReturnsTransformationMethod.getInstance()
                else mPasswordTransformationMethod
        })
        IdRegistrationViewModel.isConfirmPasswordVisible.observe(this, Observer {
            mRootView.passwordConfirmEditText.transformationMethod =
                if (it) HideReturnsTransformationMethod.getInstance()
                else mPasswordTransformationMethod
        })

        activity?.let { activity ->
            IdRegistrationViewModel.isInputIdWarningTextViewVisible.observe(
                this,
                Observer { isVisible ->
                    clearIdInputWarning()

                    if (isVisible) mRootView.inputIdWarningTextView.visibility = View.VISIBLE
                    else mRootView.inputIdWarningTextView.visibility = View.GONE

                    IdRegistrationViewModel.isAvailableId.value?.let { isAvailableId ->
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
            IdRegistrationViewModel.isInputPasswordWarningTextViewVisible.observe(this, Observer {
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
            IdRegistrationViewModel.isInputConfirmPasswordWarningTextViewVisible.observe(
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
            IdRegistrationViewModel.isTwoPasswordIdentical.observe(this, Observer { isIdentical ->
                clearPasswordConfirmInputWarning()

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
                    startActivity(Intent(activity, MyConstellationActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    })
                    activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                    activity.finish()
                }
            })
            IdRegistrationViewModel.isAvailableId.observe(this, Observer {
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

                    context?.let {
                        VibratorUtil.vibrate(it)
                    }
                }
            })
        }
    }

    fun performStartStarStarDiaryButtonClick() {
        IdRegistrationViewModel.requestCheckTwoPasswordIdentical(
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
