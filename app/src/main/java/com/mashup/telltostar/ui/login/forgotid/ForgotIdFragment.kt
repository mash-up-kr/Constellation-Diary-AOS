package com.mashup.telltostar.ui.login.forgotid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean

import com.mashup.telltostar.R
import com.mashup.telltostar.databinding.FragmentForgotIdBinding
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.util.VibratorUtil
import kotlinx.android.synthetic.main.fragment_forgot_id.view.*

class ForgotIdFragment : Fragment() {
    private lateinit var mBinding: FragmentForgotIdBinding
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private val emailEmptyWarningObservableCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    if ((it as ObservableBoolean).get()) {
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
        mBinding = DataBindingUtil.inflate<FragmentForgotIdBinding>(
            inflater,
            R.layout.fragment_forgot_id,
            container,
            false
        ).apply {
            this.viewModel = ForgotIdViewModel()
            this.fragment = this@ForgotIdFragment
        }

        setListeners()

        return mBinding.root
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners() {
        with(activity as LoginActivity) {
            mBinding.arrowBackImageViewContainer.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mLoginFragment,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            }
            mBinding.emailEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    mFragmentListener.expandBottomSheet()
                }
            }
            mBinding.forgotPasswordTextView.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mForgotPasswordFragment,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            }
            with(mBinding.emailEditText) {
                emailEditText.addTextChangedListener {
                    it?.let {
                        context?.let { context ->
                            if (it.isNotEmpty()) {
                                mBinding.nextButton.isEnabled = true
                                mBinding.nextButton.background = ContextCompat.getDrawable(
                                    context,
                                    R.drawable.custom_corner_navy_button
                                )
                                mBinding.nextButton.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        android.R.color.white
                                    )
                                )
                            } else {
                                mBinding.nextButton.isEnabled = false
                                mBinding.nextButton.background = ContextCompat.getDrawable(
                                    context,
                                    R.drawable.custom_corner_silver_button
                                )
                                mBinding.nextButton.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.brownish_grey_two
                                    )
                                )
                            }
                        }
                    }
                }
                setOnEditorActionListener { v, actionId, event ->
                    performNextButtonClick(mBinding.nextButton)

                    false
                }
            }
        }

        mBinding.viewModel?.isEmailEmptyWarningVisibleObservable?.addOnPropertyChangedCallback(
            emailEmptyWarningObservableCallback
        )
    }

    fun performNextButtonClick(view: View) {
        mBinding.viewModel?.requestFindId(mBinding.root.emailEditText.text.toString())
    }

    fun performLoginButtonClick(view: View) {
        mFragmentListener.replaceFragment(
            (activity as LoginActivity).mLoginFragment,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
    }

    override fun onDestroy() {
        mBinding.viewModel?.isEmailEmptyWarningVisibleObservable?.removeOnPropertyChangedCallback(
            emailEmptyWarningObservableCallback
        )
        mBinding.viewModel?.clearCompositeDisposable()

        super.onDestroy()
    }
}
