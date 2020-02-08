package com.mashup.telltostar.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import com.mashup.telltostar.databinding.FragmentResetPasswordBinding
import com.mashup.telltostar.util.VibratorUtil

class ResetPasswordFragment(private val mForgotPasswordViewModel: ForgotPasswordViewModel) :
    Fragment() {
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private lateinit var mBinding: FragmentResetPasswordBinding
    private val mWarningCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    if ((sender as ObservableBoolean).get()) {
                        context?.let { context ->
                            VibratorUtil.vibrate(context)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<FragmentResetPasswordBinding>(
            inflater,
            R.layout.fragment_reset_password,
            container,
            false
        ).apply {
            viewModel = mForgotPasswordViewModel
            fragment = this@ResetPasswordFragment
        }

        setListeners()
        setViewModelObserver()

        return mBinding.root
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners() {
        with(mBinding) {
            with(newPasswordEditText) {
                addTextChangedListener {
                    it?.let {
                        loginButton.isEnabled =
                            it.isNotEmpty() && passwordConfirmEditText.text.isNotEmpty()
                    }
                }
                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        mFragmentListener.expandBottomSheet()
                    }
                }
            }
            with(passwordConfirmEditText) {
                addTextChangedListener {
                    it?.let {
                        loginButton.isEnabled =
                            it.isNotEmpty() && newPasswordEditText.text.isNotEmpty()
                    }
                }
                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        mFragmentListener.expandBottomSheet()
                    }
                }
                setOnEditorActionListener { v, actionId, event ->
                    performLoginButtonClick(mBinding.loginButton)

                    false
                }
            }
            viewModel?.let {
                it.isPasswordEmptyWarningVisibleObservable.addOnPropertyChangedCallback(
                    mWarningCallback
                )
                it.isPasswordNotIdenticalWarningVisibleObservable.addOnPropertyChangedCallback(
                    mWarningCallback
                )
            }
        }
    }

    private fun setViewModelObserver() {
        mBinding.viewModel?.isPasswordInputIdenticalLiveData?.observe(this, Observer {
            if (it) {
                activity?.let {
                    mFragmentListener.replaceFragment(
                        (activity as LoginActivity).mLoginFragment,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                }
            }
        })
    }

    fun performLoginButtonClick(view: View) {
        with(mBinding) {
            viewModel?.requestResetPassword(
                newPasswordEditText.text.toString(),
                passwordConfirmEditText.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        mBinding.viewModel?.let {
            it.isPasswordEmptyWarningVisibleObservable.removeOnPropertyChangedCallback(
                mWarningCallback
            )
            it.isPasswordNotIdenticalWarningVisibleObservable.removeOnPropertyChangedCallback(
                mWarningCallback
            )
        }

        super.onDestroyView()
    }
}
