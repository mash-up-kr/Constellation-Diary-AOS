package com.mashup.telltostar.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_reset_password.view.*

class ResetPasswordFragment : Fragment() {
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_reset_password, container, false)

        setListeners()

        return mRootView
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners() {
        with(mRootView) {
            newPasswordEditText.addTextChangedListener {
                it?.let {
                    loginButton.isEnabled =
                        it.isNotEmpty() && passwordConfirmEditText.text.isNotEmpty()
                }
            }
            passwordConfirmEditText.addTextChangedListener {
                it?.let {
                    loginButton.isEnabled =
                        it.isNotEmpty() && newPasswordEditText.text.isNotEmpty()
                }
            }
            loginButton.setOnClickListener {
                performLoginButtonClick()
            }
        }
    }

    private fun performLoginButtonClick() {
        activity?.let {
            mFragmentListener.replaceFragment(
                (activity as LoginActivity).mLoginFragment,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        }
    }
}
