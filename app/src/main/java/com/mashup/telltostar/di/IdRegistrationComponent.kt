package com.mashup.telltostar.di

import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.ui.login.signup.IdRegistrationViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by hclee on 2020-03-07.
 */

@Singleton
@Component
interface IdRegistrationComponent {

    @Component.Builder
    interface Builder {

        fun build(): IdRegistrationComponent
    }

    fun idRegistrationViewModel(): IdRegistrationViewModel

    fun inject(activity: LoginActivity)
}