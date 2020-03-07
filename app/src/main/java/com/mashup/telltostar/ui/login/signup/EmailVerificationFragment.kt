package com.mashup.telltostar.ui.login.signup

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import com.mashup.telltostar.databinding.FragmentEmailVerificationBinding
import com.mashup.telltostar.di.SignUpComponent
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.util.VibratorUtil
import kotlinx.android.synthetic.main.fragment_email_verification.*

class EmailVerificationFragment(
    private val mSignUpComponent: SignUpComponent,
    private val mFragmentListener: LoginActivity.FragmentListener
) : Fragment() {
    private lateinit var mBinding: FragmentEmailVerificationBinding
    private val mEmailPatternChangeCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            (sender as ObservableBoolean).let {
                activity?.let { activity ->
                    mBinding.emailEditText.backgroundTintList =
                        if (it.get()) null
                        else ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.coral))
                }
            }
        }
    }
    private val mVerificationTimeoutCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            (sender as ObservableBoolean).let {
                activity?.let { activity ->
                    mBinding.verificationNumberEditText.backgroundTintList =
                        if (it.get()) ColorStateList.valueOf(
                            ContextCompat.getColor(
                                activity,
                                R.color.coral
                            )
                        )
                        else null
                }
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<FragmentEmailVerificationBinding>(
                inflater,
                R.layout.fragment_email_verification,
                container,
                false
        ).apply {
            this.emailVerificationViewModel = mSignUpComponent.emailVerificationViewModel()
        }

        setListeners()
        setViewModelObserver()

        return mBinding.root
    }

    private fun setListeners() {
        with(mBinding) {
            verificationRequestButton.setOnClickListener {
                emailVerificationViewModel?.requestVerificationNumber(mBinding.emailEditText.text.toString())
            }
            verificationRequestAgainButton.setOnClickListener {
                emailVerificationViewModel?.requestVerificationNumber(mBinding.emailEditText.text.toString())
            }
            emailEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    mFragmentListener.expandBottomSheet()
                }
            }
            verificationNumberEditText.addTextChangedListener {
                emailVerificationViewModel?.mInputVerificationNumber?.postValue(it.toString())

                it?.let { editable ->
                    activity?.let {
                        if (editable.length >= 6) {
                            (it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                                .hideSoftInputFromWindow(
                                    verificationNumberEditText.windowToken,
                                    0
                                )
                        }
                    }
                }
            }
            emailVerificationViewModel?.isVerificationTimeoutWarningVisibleObservable?.addOnPropertyChangedCallback(
                mVerificationTimeoutCallback
            )
        }
    }

    private fun setViewModelObserver() {
        mBinding.emailVerificationViewModel?.let {
            it.isEmailPatternObservable.addOnPropertyChangedCallback(mEmailPatternChangeCallback)
            it.isEmailPattern.observe(this@EmailVerificationFragment, Observer { isEmailPattern ->
                if (isEmailPattern.not()) {
                    context?.let {
                        VibratorUtil.vibrate(it)
                    }
                }
            })
            it.isExistEmailLiveData.observe(this@EmailVerificationFragment, Observer { isExist ->
                context?.let { context ->
                    if (isExist) {
                        VibratorUtil.vibrate(context)
                        existEmailWarningTextView.visibility = View.VISIBLE
                        mBinding.emailEditText.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.coral))
                    } else {
                        existEmailWarningTextView.visibility = View.GONE
                        mBinding.emailEditText.backgroundTintList = null
                    }
                }
            })
            it.isEmailVerified.observe(this@EmailVerificationFragment, Observer { isEmailVerified ->
                if (isEmailVerified) {
                    it.clearDisposable()
                }

                mBinding.emailEditText.isEnabled = !isEmailVerified
                mBinding.inputVerificationNumberWarningTextView.visibility =
                        if (isEmailVerified) View.GONE
                        else View.VISIBLE

                activity?.let {
                    mBinding.verificationNumberEditText.backgroundTintList =
                            if (isEmailVerified) null
                            else ColorStateList.valueOf(ContextCompat.getColor(it, R.color.coral))
                }
            })
            it.isVerificationTimeoutLiveData.observe(this@EmailVerificationFragment, Observer {
                if (it) {
                    activity?.let {
                        VibratorUtil.vibrate(it)
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        mBinding.emailVerificationViewModel?.isEmailPatternObservable?.removeOnPropertyChangedCallback(
            mEmailPatternChangeCallback
        )
        mBinding.emailVerificationViewModel?.isVerificationTimeoutWarningVisibleObservable?.removeOnPropertyChangedCallback(
            mVerificationTimeoutCallback
        )

        super.onDestroyView()
    }
}
