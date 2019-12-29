package com.mashup.telltostar.ui.login


import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*

class SignUpFragment : Fragment() {
    private lateinit var mFragmentListener: LoginActivity.FragmentListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_signup, container, false)

        setListeners(rootView)

        return rootView
    }

    fun setFragmentListener(listener: LoginActivity.FragmentListener) {
        mFragmentListener = listener
    }

    private fun setListeners(rootView: View) {
        with(activity as LoginActivity) {
            rootView.arrowBackImageViewContainer.setOnClickListener {
                mFragmentListener.replaceFragment(
                    mLoginFragment,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            }
            rootView.loginSignUpButton.setOnClickListener {
                performSignUpButtonClick()
            }
        }
    }

    private fun performSignUpButtonClick() {
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

        if (passwordConfirmEditText.text.isNullOrEmpty()) {
            passwordConfirmEditText.backgroundTintList =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    ColorStateList.valueOf(resources.getColor(R.color.coral, null))
                else
                    ColorStateList.valueOf(resources.getColor(R.color.coral))
            inputPasswordConfirmWarningTextView.visibility = View.VISIBLE
        } else {
            passwordConfirmEditText.backgroundTintList = null
            inputPasswordConfirmWarningTextView.visibility = View.GONE
        }
    }
}
