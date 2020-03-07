package com.mashup.telltostar.di

import com.mashup.telltostar.ui.login.forgotpassword.ForgotPasswordFragment
import com.mashup.telltostar.ui.login.forgotpassword.ForgotPasswordViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by hclee on 2020-03-08.
 */

@Singleton
@Component
interface ForgotPasswordComponent {

    @Component.Builder
    interface Builder {

        fun build(): ForgotPasswordComponent
    }

    fun forgotPasswordViewModel(): ForgotPasswordViewModel

    fun inject(fragment: ForgotPasswordFragment)
}