package com.mashup.telltostar.util

import java.util.regex.Pattern

/**
 * Created by hclee on 2020-02-13.
 */

object RegexUtil {
    val STAR_STAR_DIARY_ID = Pattern.compile("[a-z][a-z0-9]{3,}")
    val STAR_STAR_DIARY_PASSWORD = Pattern.compile("[a-z0-9]{4,}")

    fun isIdPattern(id: String) = STAR_STAR_DIARY_ID.matcher(id).matches()

    fun isPasswordPattern(password: String) = STAR_STAR_DIARY_PASSWORD.matcher(password).matches()
}