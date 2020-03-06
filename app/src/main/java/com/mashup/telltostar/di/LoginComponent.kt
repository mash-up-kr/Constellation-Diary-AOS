package com.mashup.telltostar.di

import com.mashup.telltostar.ui.login.LoginFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by hclee on 2020-03-04.
 */

@Singleton
@Component
interface LoginComponent {

    @Component.Builder
    interface Builder {

        fun build(): LoginComponent
    }

    fun inject(fragment: LoginFragment)
}