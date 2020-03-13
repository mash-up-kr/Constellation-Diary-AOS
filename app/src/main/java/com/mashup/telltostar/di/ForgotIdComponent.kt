package com.mashup.telltostar.di

import com.mashup.telltostar.ui.login.forgotid.ForgotIdFragment
import com.mashup.telltostar.ui.login.forgotid.ForgotIdViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by hclee on 2020-03-07.
 */

@Singleton
@Component
interface ForgotIdComponent {

    @Component.Builder
    interface Builder {

        fun build(): ForgotIdComponent
    }

    fun inject(fragment: ForgotIdFragment)

    fun forgotIdViewModel(): ForgotIdViewModel
}