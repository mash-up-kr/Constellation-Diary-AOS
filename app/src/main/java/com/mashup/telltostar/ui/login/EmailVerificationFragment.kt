package com.mashup.telltostar.ui.login

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
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_email_verification.view.*

class EmailVerificationFragment(
    private val mFragmentListener: LoginActivity.FragmentListener
) : Fragment() {
    private lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_email_verification, container, false)
        mRootView = rootView

        setListeners()
        setViewModelObserver()

        return rootView
    }

    private fun setListeners() {
        mRootView.verificationRequestButton.setOnClickListener {
            EmailVerificationViewModel.requestVerificationNumber(mRootView.emailEditText.text.toString())
        }
        mRootView.verificationRequestAgainButton.setOnClickListener {
            EmailVerificationViewModel.requestVerificationNumber(mRootView.emailEditText.text.toString())
        }
        mRootView.emailEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mFragmentListener.expandBottomSheet()
            }
        }
        mRootView.verificationNumberEditText.addTextChangedListener {
            EmailVerificationViewModel.mInputVerificationNumber.postValue(it.toString())

            it?.let { editable ->
                activity?.let {
                    if (editable.length >= 6) {
                        (it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                            .hideSoftInputFromWindow(
                                mRootView.verificationNumberEditText.windowToken,
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
                mRootView.verificationRequestButton.visibility =
                    if (isEmailPattern) View.GONE
                    else View.VISIBLE
                mRootView.inputEmailWarningTextView.visibility =
                    if (isEmailPattern) View.GONE
                    else View.VISIBLE

                activity?.let {
                    mRootView.emailEditText.backgroundTintList =
                        if (isEmailPattern) null
                        else ColorStateList.valueOf(ContextCompat.getColor(it, R.color.coral))
                }
            })
            mRemainTime.observe(this@EmailVerificationFragment, Observer {
                mRootView.verificationTimeoutTextView.text = it
            })
            isEmailVerified.observe(this@EmailVerificationFragment, Observer { isEmailVerified ->
                if (isEmailVerified) {
                    mRootView.emailVerifiedImageView.visibility = View.VISIBLE
                    mRootView.verificationRequestAgainButton.visibility = View.GONE
                    mRootView.verificationNumberTextView.visibility = View.GONE
                    mRootView.verificationTimeoutTextView.visibility = View.GONE
                    mRootView.verificationNumberEditText.visibility = View.GONE
                    clearDisposable()
                } else {
                    mRootView.emailVerifiedImageView.visibility = View.GONE
                }

                mRootView.emailEditText.isEnabled = !isEmailVerified
                mRootView.inputVerificationNumberWarningTextView.visibility =
                    if (isEmailVerified) View.GONE
                    else View.VISIBLE

                activity?.let {
                    mRootView.verificationNumberEditText.backgroundTintList =
                        if (isEmailVerified) null
                        else ColorStateList.valueOf(ContextCompat.getColor(it, R.color.coral))
                }
            })
            isEmailSend.observe(this@EmailVerificationFragment, Observer {
                if (it) {
                    mRootView.verificationRequestButton.visibility = View.GONE
                    mRootView.verificationNumberTextView.visibility = View.VISIBLE
                    mRootView.verificationNumberEditText.visibility = View.VISIBLE
                    mRootView.verificationRequestAgainButton.visibility = View.VISIBLE
                    mRootView.verificationTimeoutTextView.visibility = View.VISIBLE
                } else {
                    mRootView.verificationRequestButton.visibility = View.VISIBLE
                    mRootView.verificationNumberTextView.visibility = View.GONE
                    mRootView.verificationNumberEditText.visibility = View.GONE
                    mRootView.verificationRequestAgainButton.visibility = View.GONE
                    mRootView.verificationTimeoutTextView.visibility = View.GONE
                }
            })
        }
    }
}
