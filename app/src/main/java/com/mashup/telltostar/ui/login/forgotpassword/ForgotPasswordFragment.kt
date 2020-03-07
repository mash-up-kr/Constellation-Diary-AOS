package com.mashup.telltostar.ui.login.forgotpassword

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean

import com.mashup.telltostar.R
import com.mashup.telltostar.di.DaggerForgotPasswordComponent
import com.mashup.telltostar.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*
import javax.inject.Inject

class ForgotPasswordFragment private constructor() : Fragment() {
    companion object {
        fun createForgotPasswordFragmentWithListener(fragmentListener: LoginActivity.FragmentListener) =
            ForgotPasswordFragment().apply {
                setFragmentListener(fragmentListener)
            }
    }

    private lateinit var mRootView: View
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private val mForgotPasswordComponent by lazy {
        DaggerForgotPasswordComponent.builder().build()
    }
    @Inject
    lateinit var mForgotPasswordViewModel: ForgotPasswordViewModel
    private val mVerifiedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    if ((it as ObservableBoolean).get()) {
                        replaceFragment(
                            initResetPasswordFragment(),
                            R.anim.enter_from_right,
                            R.anim.exit_to_left
                        )
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mForgotPasswordComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        setListeners()
        replaceFragment(
            PasswordFindFragment(mForgotPasswordComponent, mFragmentListener),
            R.anim.enter_from_right,
            R.anim.exit_to_left
        )

        return mRootView
    }

    private fun initResetPasswordFragment() =
        ResetPasswordFragment(mForgotPasswordComponent).apply {
            setFragmentListener(mFragmentListener)
        }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    fun replaceFragment(fragment: Fragment, enterAnim: Int = 0, exitAnim: Int = 0) {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.setCustomAnimations(enterAnim, exitAnim)
            ?.replace(R.id.forgotPasswordFragmentContainer, fragment)
            ?.commit()
    }

    private fun setListeners() {
        mRootView.arrowBackImageViewContainer.setOnClickListener {
            mFragmentListener.replaceFragment(
                (activity as LoginActivity).mLoginFragment,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        }

        mForgotPasswordViewModel.isVerifiedObservable.addOnPropertyChangedCallback(mVerifiedCallback)
    }

    override fun onDestroyView() {
        timber.log.Timber.d("onDestroyView()")

        with(mForgotPasswordViewModel) {
            isVerifiedObservable.removeOnPropertyChangedCallback(
                mVerifiedCallback
            )
            clearCompositeDisposable()
        }

        super.onDestroyView()
    }
}
