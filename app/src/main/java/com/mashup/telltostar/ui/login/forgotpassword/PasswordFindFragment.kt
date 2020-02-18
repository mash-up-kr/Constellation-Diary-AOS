package com.mashup.telltostar.ui.login.forgotpassword

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
import com.mashup.telltostar.databinding.FragmentPasswordFindBinding
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.util.VibratorUtil

class PasswordFindFragment(
    private val mForgotPasswordViewModel: ForgotPasswordViewModel,
    private val mFragmentListener: LoginActivity.FragmentListener
) : Fragment() {
    private lateinit var mBinding: FragmentPasswordFindBinding
    private val mEditTextEmptyWarningCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    if ((sender as ObservableBoolean).get()) {
                        context?.let {
                            VibratorUtil.vibrate(it)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<FragmentPasswordFindBinding>(
            inflater,
            R.layout.fragment_password_find,
            container,
            false
        ).apply {
            viewModel = mForgotPasswordViewModel
            fragment = this@PasswordFindFragment
            requestVerificationNumberButton.isEnabled = false
        }

        setListeners()
        setViewModelObserver()

        return mBinding.root
    }

    private fun setListeners() {
        with(mBinding) {
            with(idEditText) {
                addTextChangedListener {
                    replaceVerificationButtonBackground()
                }
                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        mFragmentListener.expandBottomSheet()
                    }
                }
            }
            with(emailEditText) {
                addTextChangedListener {
                    replaceVerificationButtonBackground()
                }
                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        mFragmentListener.expandBottomSheet()
                    }
                }
            }
            emailEditText.setOnEditorActionListener { v, actionId, event ->
                performRequestVerificationNumberButtonClick(mBinding.requestVerificationNumberButton)

                false
            }
            viewModel?.let {
                it.isIdEmptyWarningVisibleObservable.addOnPropertyChangedCallback(
                    mEditTextEmptyWarningCallback
                )
                it.isEmailEmptyWarningVisibleObservable.addOnPropertyChangedCallback(
                    mEditTextEmptyWarningCallback
                )
            }
            with(verificationNumberEditText) {
                setOnEditorActionListener { v, actionId, event ->
                    performResetPasswordButtonClick(mBinding.resetPasswordButton)

                    false
                }
                addTextChangedListener {
                    context?.let { context ->
                        with(mBinding.resetPasswordButton) {
                            if (it.isNullOrEmpty()) {
                                isEnabled = false
                                background = ContextCompat.getDrawable(
                                    context,
                                    R.drawable.custom_corner_silver_button
                                )
                                setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.brownish_grey_two
                                    )
                                )
                            } else {
                                isEnabled = true
                                background = ContextCompat.getDrawable(
                                    context,
                                    R.drawable.custom_corner_navy_button
                                )
                                setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        android.R.color.white
                                    )
                                )
                            }
                        }

                        if (it.toString().length >= 6) {
                            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE)
                                    as InputMethodManager)
                                .hideSoftInputFromWindow(this.windowToken, 0)
                        }
                    }
                }
            }
        }
    }

    private fun setViewModelObserver() {
        mBinding.viewModel?.let {
            it.isVerificationTimeoutLiveData.observe(
                this@PasswordFindFragment,
                Observer { isTimeout ->
                    if (isTimeout) {
                        onVerificationNumberInputErrorOccurs()

                        mBinding.verificationTimeoutWarningTextView.visibility = View.VISIBLE
                    } else {
                        mBinding.verificationNumberEditText.backgroundTintList = null
                        mBinding.verificationTimeoutWarningTextView.visibility = View.GONE
                    }
                }
            )
            it.isVerificationNumberWrongLiveData.observe(
                this@PasswordFindFragment,
                Observer { isWrong ->
                    if (isWrong) {
                        onVerificationNumberInputErrorOccurs()

                        mBinding.verificationNumberWrongWarningTextView.visibility = View.VISIBLE
                    } else {
                        mBinding.verificationNumberEditText.backgroundTintList = null
                        mBinding.verificationNumberWrongWarningTextView.visibility = View.GONE
                    }
                }
            )
            it.isNonExistentEmailLiveData.observe(
                this@PasswordFindFragment,
                Observer { isNonExistentEmail ->
                    with(mBinding) {
                        context?.let { context ->
                            nonExistentEmailWarningTextView.visibility =
                                if (isNonExistentEmail) View.VISIBLE
                                else View.GONE
                            emailEditText.backgroundTintList =
                                if (isNonExistentEmail)
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.coral
                                        )
                                    )
                                else
                                    null

                            if (isNonExistentEmail) {
                                VibratorUtil.vibrate(context)
                            }
                        }
                    }
                }
            )
        }
    }

    private fun onVerificationNumberInputErrorOccurs() {
        context?.let {
            mBinding.verificationNumberEditText.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        it,
                        R.color.coral
                    )
                )

            VibratorUtil.vibrate(it)
        }
    }

    private fun removeVerificationNumberEditTextError() {
        with(mBinding) {
            verificationNumberEditText.backgroundTintList = null
            verificationTimeoutWarningTextView.visibility = View.GONE
            verificationNumberWrongWarningTextView.visibility = View.GONE
        }
    }

    private fun replaceVerificationButtonBackground() {
        context?.let {
            if (mBinding.idEditText.text.isNotEmpty() &&
                mBinding.emailEditText.text.isNotEmpty()
            ) {
                mBinding.requestVerificationNumberButton.background =
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.custom_corner_navy_button
                    )
                mBinding.requestVerificationNumberButton.setTextColor(
                    ContextCompat.getColor(
                        it,
                        android.R.color.white
                    )
                )
                mBinding.requestVerificationNumberButton.isEnabled = true
            } else {
                mBinding.requestVerificationNumberButton.background =
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.custom_corner_silver_button
                    )
                mBinding.requestVerificationNumberButton.setTextColor(
                    ContextCompat.getColor(
                        it,
                        R.color.brownish_grey_two
                    )
                )
                mBinding.requestVerificationNumberButton.isEnabled = false
            }
        }
    }

    fun performRequestVerificationNumberButtonClick(view: View) {
        mBinding.verificationNumberEditText.text.clear()
        mBinding.viewModel?.requestVerificationNumber(
            mBinding.idEditText.text.toString(),
            mBinding.emailEditText.text.toString()
        )
    }

    fun performResetPasswordButtonClick(view: View) {
        removeVerificationNumberEditTextError()

        mBinding.viewModel?.requestSelfVerification(
            mBinding.emailEditText.text.toString(),
            mBinding.verificationNumberEditText.text.toString().toInt(),
            mBinding.idEditText.text.toString()
        )
    }

    override fun onDestroyView() {
        mBinding.viewModel?.let {
            it.isIdEmptyWarningVisibleObservable.removeOnPropertyChangedCallback(
                mEditTextEmptyWarningCallback
            )
            it.isEmailEmptyWarningVisibleObservable.removeOnPropertyChangedCallback(
                mEditTextEmptyWarningCallback
            )
        }

        super.onDestroyView()
    }
}
