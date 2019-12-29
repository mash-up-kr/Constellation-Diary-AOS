package com.mashup.telltostar.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_email_verification.view.*

class EmailVerificationFragment(
    private val mFragmentListener: LoginActivity.FragmentListener
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_email_verification, container, false)

        setListeners(rootView)

        return rootView
    }

    private fun setListeners(rootView: View) {
        rootView.verificationRequestButton.setOnClickListener {
            rootView.verificationNumberTextView.visibility = View.VISIBLE
            rootView.verificationNumberEditText.visibility = View.VISIBLE
            rootView.verificationRequestButton.text =
                getString(R.string.request_again_verification_mail)
        }
        rootView.emailEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mFragmentListener.expandBottomSheet()
            }
        }
    }
}
