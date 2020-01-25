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
import com.mashup.telltostar.databinding.FragmentForgotPasswordBinding
import com.mashup.telltostar.util.VibratorUtil

class ForgotPasswordFragment : Fragment() {
    private lateinit var mBinding: FragmentForgotPasswordBinding
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
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
    private val mVerifiedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mFragmentListener.replaceFragment((activity as LoginActivity).mResetPasswordFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<FragmentForgotPasswordBinding>(
            inflater,
            R.layout.fragment_forgot_password,
            container,
            false
        ).apply {
            viewModel = ForgotPasswordViewModel
            fragment = this@ForgotPasswordFragment
        }

        setListeners()

        mBinding.requestVerificationNumberButton.isEnabled = false

        return mBinding.root
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners() {
        with(activity as LoginActivity) {
            mBinding.arrowBackImageViewContainer.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mLoginFragment,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            }
        }

        mBinding.idEditText.addTextChangedListener {
            replaceVerificationButtonBackground()
        }
        mBinding.emailEditText.addTextChangedListener {
            replaceVerificationButtonBackground()
        }
        mBinding.emailEditText.setOnEditorActionListener { v, actionId, event ->
            performRequestVerificationNumberButtonClick(mBinding.emailEditText)

            false
        }
        mBinding.viewModel?.isIdEmptyWarningVisibleObservable?.addOnPropertyChangedCallback(
            mEditTextEmptyWarningCallback
        )
        mBinding.viewModel?.isEmailEmptyWarningVisibleObservable?.addOnPropertyChangedCallback(
            mEditTextEmptyWarningCallback
        )
        mBinding.viewModel?.isVerifiedObservable?.addOnPropertyChangedCallback(mVerifiedCallback)
        mBinding.verificationNumberEditText.setOnEditorActionListener { v, actionId, event ->
            performResetPasswordButtonClick(mBinding.resetPasswordButton)

            false
        }
        mBinding.verificationNumberEditText.addTextChangedListener {
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
        mBinding.viewModel?.requestResetPassword(
            mBinding.emailEditText.text.toString(),
            mBinding.verificationNumberEditText.text.toString().toInt(),
            mBinding.idEditText.text.toString()
        )
    }

    override fun onDestroy() {
        mBinding.viewModel?.isIdEmptyWarningVisibleObservable?.removeOnPropertyChangedCallback(
            mEditTextEmptyWarningCallback
        )
        mBinding.viewModel?.isEmailEmptyWarningVisibleObservable?.removeOnPropertyChangedCallback(
            mEditTextEmptyWarningCallback
        )
        mBinding.viewModel?.isVerifiedObservable?.removeOnPropertyChangedCallback(mVerifiedCallback)

        super.onDestroy()
    }
}
