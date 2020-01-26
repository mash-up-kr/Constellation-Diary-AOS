package com.mashup.telltostar.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil

import com.mashup.telltostar.R
import com.mashup.telltostar.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {
    private lateinit var mFragmentListener: LoginActivity.FragmentListener
    private lateinit var mBinding: FragmentResetPasswordBinding

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
            viewModel = ForgotPasswordViewModel
            fragment = this@ResetPasswordFragment
        }

        setListeners()

        return mBinding.root
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners() {
        with(mBinding) {
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
        }
    }

    fun performLoginButtonClick(view: View) {
        activity?.let {
            mFragmentListener.replaceFragment(
                (activity as LoginActivity).mLoginFragment,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        }
    }
}
