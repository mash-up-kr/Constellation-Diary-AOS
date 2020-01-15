package com.mashup.telltostar.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import com.mashup.telltostar.util.VibratorUtil
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {
    private lateinit var mRootView: View
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private val mLoginViewModel by lazy {
        LoginViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        mRootView = rootView

        setListeners()
        setViewModelObserver()

        return rootView
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners() {
        with(activity as LoginActivity) {
            mRootView.signUpTextViewContainer.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mSignUpFragment,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            }
            mRootView.forgotIdTextViewContainer.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mForgotIdFragment,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            }
            mRootView.closeImageViewContainer.setOnClickListener {
                mFragmentListener.closeBottomSheet()
            }
            mRootView.startStarStarDiaryButton.setOnClickListener {
                performStartButtonClick()
            }
            mRootView.idEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    mFragmentListener.expandBottomSheet()
                }
            }
            with(mRootView.passwordEditText) {
                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        mFragmentListener.expandBottomSheet()
                    }
                }
                setOnEditorActionListener { v, actionId, event ->
                    performStartButtonClick()

                    true
                }
            }
        }
    }

    private fun setViewModelObserver() {
        with(mLoginViewModel) {
            context?.let { context ->
                isInputIdWarningTextViewVisible.observe(this@LoginFragment, Observer {
                    if (it) {
                        VibratorUtil.vibrate(mRootView.context)
                        mRootView.idEditText.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.coral))
                        mRootView.inputIdWarningTextView.visibility = View.VISIBLE
                    } else {
                        mRootView.idEditText.backgroundTintList = null
                        mRootView.inputIdWarningTextView.visibility = View.GONE
                    }
                })
                isInputPasswordWarningTextViewVisible.observe(this@LoginFragment, Observer {
                    if (it) {
                        VibratorUtil.vibrate(mRootView.context)
                        mRootView.passwordEditText.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.coral))
                        mRootView.inputPasswordWarningTextView.visibility = View.VISIBLE
                    } else {
                        mRootView.passwordEditText.backgroundTintList = null
                        mRootView.inputPasswordWarningTextView.visibility = View.GONE
                    }
                })
                isLoggedIn.observe(this@LoginFragment, Observer {
                    if (it) {
                        timber.log.Timber.d("login success")
                    }
                })
            }
        }
    }

    private fun performStartButtonClick() {
        mLoginViewModel.requestLogin(
            mRootView.idEditText.text.toString(),
            mRootView.passwordEditText.text.toString()
        )
    }
}
