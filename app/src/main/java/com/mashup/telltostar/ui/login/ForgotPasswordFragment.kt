package com.mashup.telltostar.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*

class ForgotPasswordFragment : Fragment() {
    private lateinit var mRootView: View
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private val mVerifiedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    if ((it as ObservableBoolean).get()) {
                        replaceFragment(
                            (activity as LoginActivity).mResetPasswordFragment,
                            R.anim.enter_from_right,
                            R.anim.exit_to_left
                        )
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        setListeners()
        replaceFragment(PasswordFindFragment(), R.anim.enter_from_right, R.anim.exit_to_left)

        return mRootView
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

        ForgotPasswordViewModel.isVerifiedObservable.addOnPropertyChangedCallback(mVerifiedCallback)
    }

    override fun onDestroy() {
        ForgotPasswordViewModel.isVerifiedObservable.removeOnPropertyChangedCallback(
            mVerifiedCallback
        )

        super.onDestroy()
    }
}
