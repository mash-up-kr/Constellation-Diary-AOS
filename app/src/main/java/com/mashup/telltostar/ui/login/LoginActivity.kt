package com.mashup.telltostar.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.loginSingUpButton
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private val mBottomSheetBehavior by lazy {
        BottomSheetBehavior.from(login_sign_up_bottom_sheet)
    }
    private val mLoginFragment by lazy {
        LoginFragment()
    }
    private val mSignUpFragment by lazy {
        SignUpFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initBottomSheetView()
    }

    private fun initBottomSheetView() {
        mBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset <= -1f) {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    setDimLayoutVisibility(View.GONE)
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
            closeImageView -> {
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                setDimLayoutVisibility(View.GONE)
            }
            arrowBackImageView -> {
                replaceBottomSheetFragment(mLoginFragment, R.anim.enter_from_left, R.anim.exit_to_right)
            }
            signupTextView -> {
                replaceBottomSheetFragment(
                    mSignUpFragment,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            }
            forgotIdTextView -> {

            }
            loginSingUpButton -> {
                setDimLayoutVisibility(View.VISIBLE)

                login_sign_up_bottom_sheet.visibility = View.VISIBLE
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun setDimLayoutVisibility(visibility: Int) {
        dimLinearLayout.visibility = visibility
    }
}
