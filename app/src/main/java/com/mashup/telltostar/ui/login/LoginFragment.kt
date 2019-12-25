package com.mashup.telltostar.ui.login

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {
    private lateinit var mFragmentListener: LoginActivity.FragmentListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        setListeners(rootView)

        return rootView
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners(rootView: View) {
        with(activity as LoginActivity) {
            rootView.signupTextView.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mSignUpFragment,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            }
            rootView.forgotIdTextView.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mForgotIdFragment,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            }
            rootView.closeImageView.setOnClickListener {
                mFragmentListener.closeBottomSheet()
            }
            rootView.startStarStarDiaryButton.setOnClickListener {
                performStartButtonClick()
            }
            rootView.idEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    mFragmentListener.expandBottomSheet()
                }
            }
            rootView.passwordEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    mFragmentListener.expandBottomSheet()
                }
            }
        }
    }

    private fun performStartButtonClick() {
        if (idEditText.text.isNullOrEmpty()) {
            idEditText.backgroundTintList =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    ColorStateList.valueOf(resources.getColor(R.color.coral, null))
                else
                    ColorStateList.valueOf(resources.getColor(R.color.coral))
            inputIdWarningTextView.visibility = View.VISIBLE
        } else {
            idEditText.backgroundTintList = null
            inputIdWarningTextView.visibility = View.GONE
        }

        if (passwordEditText.text.isNullOrEmpty()) {
            passwordEditText.backgroundTintList =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    ColorStateList.valueOf(resources.getColor(R.color.coral, null))
                else
                    ColorStateList.valueOf(resources.getColor(R.color.coral))
            inputPasswordWarningTextView.visibility = View.VISIBLE
        } else {
            passwordEditText.backgroundTintList = null
            inputPasswordWarningTextView.visibility = View.GONE
        }
    }
}
