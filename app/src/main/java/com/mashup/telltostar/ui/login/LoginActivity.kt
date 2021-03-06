package com.mashup.telltostar.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mashup.telltostar.R
import com.mashup.telltostar.di.DaggerIdRegistrationComponent
import com.mashup.telltostar.di.IdRegistrationComponent
import com.mashup.telltostar.ui.login.forgotpassword.ForgotPasswordFragment
import com.mashup.telltostar.ui.login.signup.SignUpFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.loginSingUpButton

class LoginActivity : AppCompatActivity() {
    interface FragmentListener {
        fun closeBottomSheet()
        fun expandBottomSheet()
        fun replaceFragment(fragment: Fragment, enterAnim: Int = 0, exitAnim: Int = 0)
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
    private var mBottomSheetBehavior: BottomSheetBehavior<View>? = null
    lateinit var mLoginFragment: LoginFragment
    lateinit var mSignUpFragment: SignUpFragment
    val mIdRegistrationComponent by lazy {
        DaggerIdRegistrationComponent.builder().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    private fun initLoginFragment() = LoginFragment().apply {
        setFragmentListener(mFragmentListener)
    }

    private fun initSignUpFragment() = SignUpFragment().apply {
        setFragmentListener(mFragmentListener)
    }

    private fun openBottomSheet() {
        login_sign_up_bottom_sheet.visibility = View.VISIBLE
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        setDimLayoutVisibility(View.VISIBLE)
    }

    private fun closeBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        setDimLayoutVisibility(View.GONE)
    }

    private fun expandBottomSheet() {
        login_sign_up_bottom_sheet.visibility = View.VISIBLE
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        setDimLayoutVisibility(View.VISIBLE)
    }

    private fun initBottomSheetView() {
        mLoginFragment = initLoginFragment()
        mSignUpFragment = initSignUpFragment()
        mBottomSheetBehavior = BottomSheetBehavior.from(login_sign_up_bottom_sheet).apply {
            addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset <= -1f) {
                        closeBottomSheet()
                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {}
            })
        }

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
                initBottomSheetView()
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

    override fun onBackPressed() {
        mBottomSheetBehavior?.let {
            if (it.state != BottomSheetBehavior.STATE_HIDDEN) {
                if (mLoginFragment.isVisible) {
                    closeBottomSheet()
                } else {
                    replaceBottomSheetFragment(
                        mLoginFragment,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                }
            } else {
                super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }
}
