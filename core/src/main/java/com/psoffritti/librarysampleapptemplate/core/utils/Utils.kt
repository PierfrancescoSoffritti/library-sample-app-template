package com.psoffritti.librarysampleapptemplate.core.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.view.Menu
import android.view.WindowManager
import com.google.android.material.navigation.NavigationView
import com.psoffritti.librarysampleapptemplate.core.R

object Utils {
    fun Activity.setStatusBarTranslucency() {
        if (Build.VERSION.SDK_INT >= 21) {
            val windowParams: WindowManager.LayoutParams = window.attributes
            windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.attributes = windowParams
        }
    }

    fun Activity.getScreenWidth() : Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    fun Activity.openUri(uri: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }

    fun Menu.addItems(examplesDetails: List<ExampleActivityDetails>?) {
        examplesDetails?.forEachIndexed { index, element ->
            add(R.id.nav_drawer_examples_group, index, 0, element.nameResource).setIcon(element.iconResource)
        }
    }

    fun NavigationView.setWidth(width: Int) {
        val params = layoutParams
        val dim320dp = resources.getDimensionPixelSize(R.dimen._320dp)
        params.width = if (width > dim320dp) dim320dp else width
        layoutParams = params
    }
}