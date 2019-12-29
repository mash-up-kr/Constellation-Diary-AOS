package com.mashup.telltostar.ui.login

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_email_verification.*
import kotlinx.android.synthetic.main.fragment_email_verification.view.*
import kotlinx.android.synthetic.main.fragment_signup.*

class EmailVerificationFragment(
    private val mFragmentListener: LoginActivity.FragmentListener
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_email_verification, container, false)

        setListeners(rootView)
        setViewModelObserver(rootView)

        return rootView
    }

    private fun setListeners(rootView: View) {
        rootView.verificationRequestButton.setOnClickListener {
            rootView.verificationRequestButton.visibility = View.GONE
            EmailVerificationViewModel.requestVerificationNumber()
        }
        rootView.verificationRequestAgainButton.setOnClickListener {
            EmailVerificationViewModel.requestVerificationNumber()
        }
        rootView.emailEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mFragmentListener.expandBottomSheet()
            }
        }
        rootView.verificationNumberEditText.addTextChangedListener {
            EmailVerificationViewModel.mInputVerificationNumber.postValue(it.toString())

            it?.let { editable ->
                activity?.let {
                    if (editable.length >= 6) {
                        (it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                            .hideSoftInputFromWindow(
                                rootView.verificationNumberEditText.windowToken,
                                0
                            )
                    }
                }
            }
        }
    }

    private fun setViewModelObserver(rootView: View) {
        with(EmailVerificationViewModel) {
            mRemainTime.observe(this@EmailVerificationFragment, Observer {
                rootView.verificationTimeoutTextView.text = it
            })
            isEmailVerified.observe(this@EmailVerificationFragment, Observer {
                if (it) {
                    rootView.emailVerifiedImageView.visibility = View.VISIBLE
                    rootView.verificationRequestAgainButton.visibility = View.GONE
                    rootView.verificationNumberTextView.visibility = View.GONE
                    rootView.verificationTimeoutTextView.visibility = View.GONE
                    rootView.verificationNumberEditText.visibility = View.GONE
                    clearDisposable()
                } else {
                    rootView.emailVerifiedImageView.visibility = View.GONE
                }
            })
            isEmailSend.observe(this@EmailVerificationFragment, Observer {
                if (it) {
                    rootView.verificationRequestButton.visibility = View.GONE
                    rootView.verificationNumberTextView.visibility = View.VISIBLE
                    rootView.verificationNumberEditText.visibility = View.VISIBLE
                    rootView.verificationRequestAgainButton.visibility = View.VISIBLE
                    rootView.verificationTimeoutTextView.visibility = View.VISIBLE
                } else {
                    rootView.verificationRequestButton.visibility = View.VISIBLE
                    rootView.verificationNumberTextView.visibility = View.GONE
                    rootView.verificationNumberEditText.visibility = View.GONE
                    rootView.verificationRequestAgainButton.visibility = View.GONE
                    rootView.verificationTimeoutTextView.visibility = View.GONE
                }
            })
        }
    }
}
