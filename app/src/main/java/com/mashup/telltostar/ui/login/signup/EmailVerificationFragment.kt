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
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.util.VibratorUtil

class EmailVerificationFragment(
    private val mEmailVerificationViewModel: EmailVerificationViewModel,
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
            this.emailVerificationViewModel = mEmailVerificationViewModel
        }

        setListeners()
        setViewModelObserver()

        return mBinding.root
    }

    private fun setListeners() {
        mBinding.verificationRequestButton.setOnClickListener {
            mEmailVerificationViewModel.requestVerificationNumber(mBinding.emailEditText.text.toString())
        }
        mBinding.verificationRequestAgainButton.setOnClickListener {
            mEmailVerificationViewModel.requestVerificationNumber(mBinding.emailEditText.text.toString())
        }
        mBinding.emailEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mFragmentListener.expandBottomSheet()
            }
        }
        mBinding.verificationNumberEditText.addTextChangedListener {
            mEmailVerificationViewModel.mInputVerificationNumber.postValue(it.toString())

            it?.let { editable ->
                activity?.let {
                    if (editable.length >= 6) {
                        (it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                                .hideSoftInputFromWindow(
                                        mBinding.verificationNumberEditText.windowToken,
                                        0
                                )
                    }
                }
            }
        }
        mBinding.emailVerificationViewModel?.isVerificationTimeoutWarningVisibleObservable?.addOnPropertyChangedCallback(
            mVerificationTimeoutCallback
        )
    }

    private fun setViewModelObserver() {
        with(mEmailVerificationViewModel) {
            isEmailPatternObservable.addOnPropertyChangedCallback(mEmailPatternChangeCallback)
            isEmailPattern.observe(this@EmailVerificationFragment, Observer { isEmailPattern ->
                if (isEmailPattern.not()) {
                    context?.let {
                        VibratorUtil.vibrate(it)
                    }
                }
            })
            isEmailVerified.observe(this@EmailVerificationFragment, Observer { isEmailVerified ->
                if (isEmailVerified) {
                    clearDisposable()
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
            isVerificationTimeoutLiveData.observe(this@EmailVerificationFragment, Observer {
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
