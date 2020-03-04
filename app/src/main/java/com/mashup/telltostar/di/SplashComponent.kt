package com.mashup.telltostar.di

import com.mashup.telltostar.ui.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by hclee on 2020-03-04.
 */

@Singleton
@Component
interface SplashComponent {

    @Component.Builder
    interface Builder {

        fun build(): SplashComponent
    }

    fun inject(activity: SplashActivity)
}