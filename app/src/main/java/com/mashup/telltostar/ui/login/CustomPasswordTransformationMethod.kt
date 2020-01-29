package com.mashup.telltostar.ui.login

import android.text.method.PasswordTransformationMethod
import android.view.View

/**
 * Created by hclee on 2020-01-11.
 */

class CustomPasswordTransformationMethod : PasswordTransformationMethod() {
    companion object {
        class PasswordCharSequence(private val mSource: CharSequence) : CharSequence {

            override val length: Int
                get() = mSource.length

            override fun get(index: Int) = '*'

            override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
                mSource.subSequence(startIndex, endIndex)
        }
    }

    override fun getTransformation(source: CharSequence, view: View?): CharSequence {
        return PasswordCharSequence(source)
    }
}