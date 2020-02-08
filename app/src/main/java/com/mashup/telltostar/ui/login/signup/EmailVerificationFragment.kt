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
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import com.mashup.telltostar.databinding.FragmentEmailVerificationBinding
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.util.VibratorUtil

class EmailVerificationFragment(
        private val mFragmentListener: LoginActivity.FragmentListener
) : Fragment() {
    private lateinit var mBinding: FragmentEmailVerificationBinding

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
            this.emailVerificationViewModel = EmailVerificationViewModel
        }

        setListeners()
        setViewModelObserver()

        return mBinding.root
    }

    private fun setListeners() {
        mBinding.verificationRequestButton.setOnClickListener {
            EmailVerificationViewModel.requestVerificationNumber(mBinding.emailEditText.text.toString())
        }
        mBinding.verificationRequestAgainButton.setOnClickListener {
            EmailVerificationViewModel.requestVerificationNumber(mBinding.emailEditText.text.toString())
        }
        mBinding.emailEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mFragmentListener.expandBottomSheet()
            }
        }
        mBinding.verificationNumberEditText.addTextChangedListener {
            EmailVerificationViewModel.mInputVerificationNumber.postValue(it.toString())

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
    }

    private fun setViewModelObserver() {
        with(EmailVerificationViewModel) {
            isEmailPattern.observe(this@EmailVerificationFragment, Observer { isEmailPattern ->
                if (isEmailPattern) {
                    mBinding.inputEmailWarningTextView.visibility = View.GONE
                } else {
                    mBinding.inputEmailWarningTextView.visibility = View.VISIBLE

                    context?.let {
                        VibratorUtil.vibrate(it)
                    }
                }

                activity?.let {
                    mBinding.emailEditText.backgroundTintList =
                            if (isEmailPattern) null
                            else ColorStateList.valueOf(ContextCompat.getColor(it, R.color.coral))
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
        }
    }
}
