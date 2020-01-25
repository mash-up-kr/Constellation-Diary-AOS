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
        mBinding.viewModel?.requestVerificationNumber(
            mBinding.idEditText.text.toString(),
            mBinding.emailEditText.text.toString()
        )
    }

    override fun onDestroy() {
        mBinding.viewModel?.isIdEmptyWarningVisibleObservable?.removeOnPropertyChangedCallback(
            mEditTextEmptyWarningCallback
        )
        mBinding.viewModel?.isEmailEmptyWarningVisibleObservable?.removeOnPropertyChangedCallback(
            mEditTextEmptyWarningCallback
        )

        super.onDestroy()
    }
}
