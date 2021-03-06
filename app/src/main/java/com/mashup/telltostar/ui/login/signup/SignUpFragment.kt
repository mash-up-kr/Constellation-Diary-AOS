package com.mashup.telltostar.ui.login.signup

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import com.mashup.telltostar.di.DaggerSignUpComponent
import com.mashup.telltostar.di.SignUpComponent
import com.mashup.telltostar.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_email_verification.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import org.koin.android.ext.android.inject
import javax.inject.Inject

class SignUpFragment : Fragment() {
    @Inject
    lateinit var mEmailVerificationViewModel: EmailVerificationViewModel
    private lateinit var mRootView: View
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private lateinit var mEmailVerificationFragment: EmailVerificationFragment
    private val mIdRegistrationFragment by lazy {
        IdRegistrationFragment(mSignUpComponent)
    }
    private val mSignUpComponent by lazy {
        DaggerSignUpComponent.builder().build()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mSignUpComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_signup, container, false)

        mRootView = rootView
        mEmailVerificationFragment = EmailVerificationFragment(mSignUpComponent, mFragmentListener)

        setListeners()
        setViewModelObserver(mRootView)
        replaceFragment(mEmailVerificationFragment)

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
            mRootView.nextSignUpButton.setOnClickListener {
                performNextSignUpButtonClick()
            }
            mRootView.startStarStarDiaryButton.setOnClickListener {
                performStartStarStarDiaryButtonClick()
            }
        }
    }

    private fun setViewModelObserver(mRootView: View) {
        mEmailVerificationViewModel.mInputVerificationNumber.observe(this@SignUpFragment, Observer {
            if (it.length >= 6) {
                with(mRootView.nextSignUpButton) {
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
                with(mRootView.nextSignUpButton) {
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
        mEmailVerificationViewModel.isEmailVerified.observe(this@SignUpFragment, Observer {
            if (it) {
                with(mRootView.nextSignUpButton) {
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

    private fun performNextSignUpButtonClick() {
        mEmailVerificationViewModel.isEmailSend.value?.let { isEmailSend ->
            if (isEmailSend) {
                mEmailVerificationViewModel.isEmailVerified.value?.let { isEmailVerified ->
                    if (isEmailVerified) {
                        performStartStarStarDiaryButtonClick()
                    } else {
                        mEmailVerificationViewModel.requestEmailVerification(
                            mEmailVerificationFragment.emailEditText.text.toString(),
                            mEmailVerificationFragment.verificationNumberEditText.text.toString().toInt()
                        )
                    }
                } ?: mEmailVerificationViewModel.requestEmailVerification(
                    mEmailVerificationFragment.emailEditText.text.toString(),
                    mEmailVerificationFragment.verificationNumberEditText.text.toString().toInt()
                )
            }
        }
    }

    private fun performStartStarStarDiaryButtonClick() {
        if (mEmailVerificationFragment.isVisible) {
            replaceFragment(mIdRegistrationFragment, R.anim.enter_from_right, R.anim.exit_to_left)
            mEmailVerificationViewModel.clearDisposable()
            mRootView.signUpStepTextView.text = getString(R.string.sign_up_step_2)
            mRootView.startStarStarDiaryButton.visibility = View.VISIBLE
            mRootView.nextSignUpButton.visibility = View.GONE
        } else if (mIdRegistrationFragment.isVisible) {
            mIdRegistrationFragment.performStartStarStarDiaryButtonClick()
        }
    }
}
