package com.mashup.telltostar.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*

class ForgotPasswordFragment : Fragment() {
    private lateinit var mRootView: View
    private lateinit var mFragmentListener: LoginActivity.FragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        setListeners()
        setViewModelObserver()

        mRootView.requestVerificationNumberButton.isEnabled = false

        return mRootView
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
        }

        mRootView.idEditText.addTextChangedListener {
            replaceVerificationButtonBackground()
        }
        mRootView.emailEditText.addTextChangedListener {
            replaceVerificationButtonBackground()
        }
        mRootView.requestVerificationNumberButton.setOnClickListener {
            performRequestVerificationNumberButtonClick()
        }
    }

    private fun replaceVerificationButtonBackground() {
        context?.let {
            if (mRootView.idEditText.text.isNotEmpty() &&
                mRootView.emailEditText.text.isNotEmpty()
            ) {
                mRootView.requestVerificationNumberButton.background =
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.custom_corner_navy_button
                    )
                mRootView.requestVerificationNumberButton.isEnabled = true
            } else {
                mRootView.requestVerificationNumberButton.background =
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.custom_corner_silver_button
                    )
                mRootView.requestVerificationNumberButton.isEnabled = false
            }
        }
    }

    private fun performRequestVerificationNumberButtonClick() {
        ForgotPasswordViewModel.requestVerificationNumber(
            mRootView.idEditText.text.toString(),
            mRootView.emailEditText.text.toString()
        )
    }

    private fun setViewModelObserver() {
        ForgotPasswordViewModel.isIdCheckWarningTextViewVisible.observe(this, Observer {
            mRootView.checkIdWarningTextView.visibility =
                if (it) View.VISIBLE
                else View.GONE
        })
        ForgotPasswordViewModel.isEmailCheckWarningTextViewVisible.observe(this, Observer {
            mRootView.checkEmailWarningTextView.visibility =
                if (it) View.VISIBLE
                else View.GONE
        })
        ForgotPasswordViewModel.isVerificationNumberInputVisible.observe(this, Observer {
            if (it) {
                mRootView.verificationNumberInputConstraintLayout.visibility = View.VISIBLE
                mRootView.requestVerificationNumberButton.visibility = View.GONE
                mRootView.resetPasswordButton.visibility = View.VISIBLE
            } else {
                mRootView.verificationNumberInputConstraintLayout.visibility = View.GONE
            }
        })
    }
}
