package com.mashup.telltostar.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.fragment_forgot_id.*
import kotlinx.android.synthetic.main.fragment_forgot_id.view.*

class ForgotIdFragment : Fragment() {
    private lateinit var mFragmentListener: LoginActivity.FragmentListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_forgot_id, container, false)

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
            rootView.verificationRequestButton.setOnClickListener {
                if (!emailEditText.text.isNullOrEmpty()) {
                    verificationNumberTextView.visibility = View.VISIBLE
                    verificationNumberEditText.visibility = View.VISIBLE
                    verificationRequestButton.text =
                        getString(R.string.request_again_verification_mail)
                }
            }
            rootView.emailEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    mFragmentListener.expandBottomSheet()
                }
            }
            rootView.verificationNumberEditText.addTextChangedListener {
                rootView.disabledNextButton.visibility =
                    if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
                rootView.forgotIdButton.visibility =
                    if (it.isNullOrEmpty()) View.GONE
                    else View.VISIBLE
            }
            rootView.forgotIdButton.setOnClickListener {
                // TODO: 서버 통해 인증 요청, 인증 성공 시 "아이디 찾기 완료하기"로 텍스트 변경
            }
        }
    }
}
