package com.mashup.telltostar.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean

import com.mashup.telltostar.R
import com.mashup.telltostar.databinding.FragmentPasswordFindBinding
import com.mashup.telltostar.util.VibratorUtil

class PasswordFindFragment : Fragment() {
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
            viewModel = ForgotPasswordViewModel
            fragment = this@PasswordFindFragment
            requestVerificationNumberButton.isEnabled = false
        }

        setListeners()

        return mBinding.root
    }

    private fun setListeners() {
        with(mBinding) {
            idEditText.addTextChangedListener {
                replaceVerificationButtonBackground()
            }
            emailEditText.addTextChangedListener {
                replaceVerificationButtonBackground()
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
            verificationNumberEditText.setOnEditorActionListener { v, actionId, event ->
                performResetPasswordButtonClick(mBinding.resetPasswordButton)

                false
            }
            verificationNumberEditText.addTextChangedListener {
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
                }
            }
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
        timber.log.Timber.d("performResetPasswordButtonClick()")

        mBinding.viewModel?.requestResetPassword(
            mBinding.emailEditText.text.toString(),
            mBinding.verificationNumberEditText.text.toString().toInt(),
            mBinding.idEditText.text.toString()
        )
    }

    override fun onDestroy() {
        mBinding.viewModel?.let {
            it.isIdEmptyWarningVisibleObservable.removeOnPropertyChangedCallback(
                mEditTextEmptyWarningCallback
            )
            it.isEmailEmptyWarningVisibleObservable.removeOnPropertyChangedCallback(
                mEditTextEmptyWarningCallback
            )
        }

        super.onDestroy()
    }
}
