package com.mashup.telltostar.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_forgot_id.*
import kotlinx.android.synthetic.main.fragment_forgot_id.view.*

class ForgotIdFragment : Fragment() {
    private lateinit var mRootView: View
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private val mForgotIdViewModel by lazy {
        ForgotIdViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_forgot_id, container, false)
        mRootView = rootView

        setViewModelObserver()
        setListeners()

        return rootView
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners() {
        with(activity as LoginActivity) {
            mRootView.arrowBackImageViewContainer.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mLoginFragment,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            }
            mRootView.verificationRequestButton.setOnClickListener {
                performVerificationRequestButtonClick()
            }
            mRootView.emailEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    mFragmentListener.expandBottomSheet()
                }
            }
            with(mRootView.verificationNumberEditText) {
                addTextChangedListener {
                    mRootView.disabledNextButton.visibility =
                        if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
                    mRootView.forgotIdButton.visibility =
                        if (it.isNullOrEmpty()) View.GONE
                        else View.VISIBLE
                }
                setOnEditorActionListener { v, actionId, event ->
                    performNextButtonClick()

                    false
                }
            }
            mRootView.forgotIdButton.setOnClickListener {
                performNextButtonClick()
            }
        }
    }

    private fun performVerificationRequestButtonClick() {
        with(emailEditText.text.isNullOrEmpty()) {
            activity?.let {
                mRootView.emailEditText.backgroundTintList =
                    if (this)
                        ColorStateList.valueOf(ContextCompat.getColor(it, R.color.coral))
                    else
                        null
            }
            mRootView.inputEmailWarningTextView.visibility = if (this) View.VISIBLE else View.GONE
            mRootView.verificationNumberTextView.visibility = if (!this) View.VISIBLE else View.GONE
            mRootView.verificationNumberEditText.visibility = if (!this) View.VISIBLE else View.GONE
            mRootView.verificationRequestButton.text =
                if (this) getString(R.string.request_verification_mail)
                else getString(R.string.request_again_verification_mail)
        }
    }

    private fun performNextButtonClick() {
        mForgotIdViewModel.requestEmailVerification(mRootView.verificationNumberEditText.text.toString())
    }

    private fun setViewModelObserver() {
        mForgotIdViewModel.isEmailVerified.observe(this, Observer {
            if (it) {
                mRootView.forgotIdButton.text = getString(R.string.complete_forgot_id)
            }
        })
    }

    override fun onDestroy() {
        mForgotIdViewModel.clearCompositeDisposable()

        super.onDestroy()
    }
}
