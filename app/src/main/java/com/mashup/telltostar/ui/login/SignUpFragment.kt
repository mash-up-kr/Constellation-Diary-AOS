package com.mashup.telltostar.ui.login

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_email_verification.*
import kotlinx.android.synthetic.main.fragment_id_register.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*

class SignUpFragment : Fragment() {
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private lateinit var mEmailVerificationFragment: EmailVerificationFragment
    private val mIdRegisterFragment by lazy {
        IdRegisterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_signup, container, false)

        mEmailVerificationFragment = EmailVerificationFragment(mFragmentListener)

        setListeners(rootView)
        setViewModelObserver(rootView)
        replaceFragment(mEmailVerificationFragment)

        return rootView
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners(rootView: View) {
        with(activity as LoginActivity) {
            rootView.arrowBackImageViewContainer.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mLoginFragment,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            }
            rootView.nextSignUpButton.setOnClickListener {
                EmailVerificationViewModel.isEmailSend.value?.let { isEmailSend ->
                    if (isEmailSend) {
                        EmailVerificationViewModel.isEmailVerified.value?.let { isEmailVerified ->
                            if (isEmailVerified) {
                                performSignUpButtonClick()
                            } else {
                                EmailVerificationViewModel.requestEmailVerification(
                                    mEmailVerificationFragment.verificationNumberEditText.text.toString()
                                )
                            }
                        } ?: EmailVerificationViewModel.requestEmailVerification(
                            mEmailVerificationFragment.verificationNumberEditText.text.toString()
                        )
                    }
                }
            }
            rootView.loginSignUpButton.setOnClickListener {
                performSignUpButtonClick()
            }
        }
    }

    private fun setViewModelObserver(rootView: View) {
        EmailVerificationViewModel.mInputVerificationNumber.observe(this@SignUpFragment, Observer {
            if (it.length >= 6) {
                with(rootView.nextSignUpButton) {
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
            } else {
                with(rootView.nextSignUpButton) {
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
                }
            }
        })
        EmailVerificationViewModel.isEmailVerified.observe(this@SignUpFragment, Observer {
            if (it) {
                with(rootView.nextSignUpButton) {
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
                    text = getString(R.string.next)
                }
            }
        })
    }

    private fun replaceFragment(
        fragment: Fragment,
        enterAnim: Int = 0,
        exitAnim: Int = 0
    ) {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.setCustomAnimations(enterAnim, exitAnim)
            ?.replace(R.id.signUpFragmentContainer, fragment)
            ?.commit()
    }

    private fun performSignUpButtonClick() {
        if (mEmailVerificationFragment.isVisible) {
            replaceFragment(mIdRegisterFragment, R.anim.enter_from_right, R.anim.exit_to_left)
            EmailVerificationViewModel.clearDisposable()
            signUpStepTextView.text = getString(R.string.sign_up_step_2)
        } else {
            with(mIdRegisterFragment) {
                if (this.idEditText.text.isNullOrEmpty()) {
                    this.idEditText.backgroundTintList =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            ColorStateList.valueOf(resources.getColor(R.color.coral, null))
                        else
                            ColorStateList.valueOf(resources.getColor(R.color.coral))
                    this.inputIdWarningTextView.visibility = View.VISIBLE
                } else {
                    this.idEditText.backgroundTintList = null
                    this.inputIdWarningTextView.visibility = View.GONE
                }

                if (this.passwordEditText.text.isNullOrEmpty()) {
                    this.passwordEditText.backgroundTintList =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            ColorStateList.valueOf(resources.getColor(R.color.coral, null))
                        else
                            ColorStateList.valueOf(resources.getColor(R.color.coral))
                    this.inputPasswordWarningTextView.visibility = View.VISIBLE
                } else {
                    this.passwordEditText.backgroundTintList = null
                    this.inputPasswordWarningTextView.visibility = View.GONE
                }

                if (this.passwordConfirmEditText.text.isNullOrEmpty()) {
                    this.passwordConfirmEditText.backgroundTintList =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            ColorStateList.valueOf(resources.getColor(R.color.coral, null))
                        else
                            ColorStateList.valueOf(resources.getColor(R.color.coral))
                    this.inputPasswordConfirmWarningTextView.visibility = View.VISIBLE
                } else {
                    this.passwordConfirmEditText.backgroundTintList = null
                    this.inputPasswordConfirmWarningTextView.visibility = View.GONE
                }
            }
        }
    }
}
