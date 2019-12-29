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
import kotlinx.android.synthetic.main.fragment_email_verification.view.*
import kotlinx.android.synthetic.main.fragment_signup.*

class EmailVerificationFragment(
    private val mParentFragment: SignUpFragment,
    private val mFragmentListener: LoginActivity.FragmentListener
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_email_verification, container, false)

        setListeners(rootView)
        setRemainTimeObserver(rootView)
        showEmailVerificationInfoInput(rootView)

        return rootView
    }

    private fun setListeners(rootView: View) {
        rootView.verificationRequestButton.setOnClickListener {
            EmailVerificationViewModel.isEmailSend = true

            showEmailVerificationInfoInput(rootView)
            EmailVerificationViewModel.requestEmailVerification()
        }
        rootView.emailEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mFragmentListener.expandBottomSheet()
            }
        }
        rootView.verificationNumberEditText.addTextChangedListener {
            it?.let { editable ->
                activity?.let {
                    mParentFragment.nextSignUpButton.run {
                        if (editable.length >= 6) {
                            background = ContextCompat.getDrawable(
                                it.applicationContext,
                                R.drawable.custom_corner_navy_button
                            )
                            setTextColor(
                                ContextCompat.getColor(
                                    it.applicationContext,
                                    android.R.color.white
                                )
                            )

                            (it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                                .hideSoftInputFromWindow(
                                    rootView.verificationNumberEditText.windowToken,
                                    0
                                )
                        } else {
                            background = ContextCompat.getDrawable(
                                it.applicationContext,
                                R.drawable.custom_corner_silver_button
                            )
                            setTextColor(
                                ContextCompat.getColor(
                                    it.applicationContext,
                                    R.color.brownish_grey_two
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showEmailVerificationInfoInput(rootView: View) {
        if (EmailVerificationViewModel.isEmailSend) {
            rootView.verificationNumberTextView.visibility = View.VISIBLE
            rootView.verificationNumberEditText.visibility = View.VISIBLE
            rootView.verificationRequestButton.text =
                getString(R.string.request_again_verification_mail)
            rootView.verificationTimeoutTextView.visibility = View.VISIBLE
        }
    }

    private fun setRemainTimeObserver(rootView: View) {
        EmailVerificationViewModel.mRemainTime.observe(this@EmailVerificationFragment, Observer {
            timber.log.Timber.d("onChanged()")

            rootView.verificationTimeoutTextView.text = it
        })
    }
}
