package com.mashup.telltostar.ui.login.forgotid

import android.content.Context
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
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import com.mashup.telltostar.databinding.FragmentForgotIdBinding
import com.mashup.telltostar.di.DaggerForgotIdComponent
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.ui.login.forgotpassword.ForgotPasswordFragment
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
    private val mForgotIdComponent by lazy {
        DaggerForgotIdComponent.builder().build()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mForgotIdComponent.inject(this)
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
            this.viewModel = mForgotIdComponent.forgotIdViewModel()
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
                    ForgotPasswordFragment.createForgotPasswordFragmentWithListener(
                        mFragmentListener
                    ),
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

        mBinding.viewModel?.let {
            it.isEmailEmptyWarningVisibleObservable.addOnPropertyChangedCallback(
                emailEmptyWarningObservableCallback
            )
            it.isNonExistentEmailWarningVisibleLiveData.observe(this@ForgotIdFragment, Observer {
                if (it) {
                    context?.let { context ->
                        VibratorUtil.vibrate(context)
                    }
                }
            })
        }
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

    override fun onDestroyView() {
        mBinding.viewModel?.isEmailEmptyWarningVisibleObservable?.removeOnPropertyChangedCallback(
            emailEmptyWarningObservableCallback
        )
        mBinding.viewModel?.clearCompositeDisposable()

        super.onDestroyView()
    }
}
