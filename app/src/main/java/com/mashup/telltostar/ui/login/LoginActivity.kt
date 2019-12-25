package com.mashup.telltostar.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.loginSingUpButton

class LoginActivity : AppCompatActivity() {
    interface FragmentListener {
        fun closeBottomSheet()
        fun expandBottomSheet()
        fun replaceFragment(fragment: Fragment, enterAnim: Int, exitAnim: Int)
    }

    private val mFragmentListener by lazy {
        object : FragmentListener {
            override fun closeBottomSheet() {
                this@LoginActivity.closeBottomSheet()
            }

            override fun replaceFragment(fragment: Fragment, enterAnim: Int, exitAnim: Int) {
                replaceBottomSheetFragment(fragment, enterAnim, exitAnim)
            }

            override fun expandBottomSheet() {
                this@LoginActivity.expandBottomSheet()
            }
        }
    }
    private val mBottomSheetBehavior by lazy {
        BottomSheetBehavior.from(login_sign_up_bottom_sheet)
    }
    val mLoginFragment by lazy {
        LoginFragment()
    }
    val mSignUpFragment by lazy {
        SignUpFragment()
    }
    val mForgotIdFragment by lazy {
        ForgotIdFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initBottomSheetView()
    }

    private fun openBottomSheet() {
        login_sign_up_bottom_sheet.visibility = View.VISIBLE
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        setDimLayoutVisibility(View.VISIBLE)
    }

    private fun closeBottomSheet() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        setDimLayoutVisibility(View.GONE)
        replaceBottomSheetFragment(mLoginFragment)
    }

    private fun expandBottomSheet() {
        login_sign_up_bottom_sheet.visibility = View.VISIBLE
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        setDimLayoutVisibility(View.VISIBLE)
    }

    private fun initBottomSheetView() {
        mLoginFragment.setFragmentListener(mFragmentListener)
        mSignUpFragment.setFragmentListener(mFragmentListener)
        mForgotIdFragment.setFragmentListener(mFragmentListener)
        mBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset <= -1f) {
                    closeBottomSheet()
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })

        replaceBottomSheetFragment(mLoginFragment)
    }

    private fun replaceBottomSheetFragment(
        fragment: Fragment,
        enterAnim: Int = 0,
        exitAnim: Int = 0
    ) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(enterAnim, exitAnim)
            .replace(R.id.bottomSheetContainer, fragment)
            .commit()
    }

    fun onClick(view: View) {
        when (view) {
            loginSingUpButton -> {
                openBottomSheet()
            }
            dimLinearLayout -> {
                closeBottomSheet()
            }
        }
    }

    private fun setDimLayoutVisibility(visibility: Int) {
        dimLinearLayout.visibility = visibility
    }
}
