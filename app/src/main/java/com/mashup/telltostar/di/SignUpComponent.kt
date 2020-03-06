package com.mashup.telltostar.di

import com.mashup.telltostar.ui.login.signup.EmailVerificationViewModel
import com.mashup.telltostar.ui.login.signup.SignUpFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by hclee on 2020-03-06.
 */

@Singleton
@Component
interface SignUpComponent {

    @Component.Builder
    interface Builder {

        fun build(): SignUpComponent
    }

    fun emailVerificationViewModel(): EmailVerificationViewModel

    fun inject(fragment: SignUpFragment)
}