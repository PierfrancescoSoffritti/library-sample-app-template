package com.psoffritti.librarysampleapptemplate.core.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.io.Serializable

data class ExampleActivityDetails(@StringRes val nameResource: Int, @DrawableRes val iconResource: Int?, val clazz: Class<*>): Serializable
