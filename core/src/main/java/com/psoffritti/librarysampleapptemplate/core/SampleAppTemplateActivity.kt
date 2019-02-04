package com.psoffritti.librarysampleapptemplate.core

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*

import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.material.navigation.NavigationView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_sample_app_template.*

class SampleAppTemplateActivity : AppCompatActivity() {

    private lateinit var state: State

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_app_template)

        state = State.getInstance(intent.extras!!)

        adjustStatusBarTranslucency()
        initToolbar(state.title)
        initNavDrawer(state.examples, state.githubUrl, state.playStorePackageName)
        if(state.homepageUrl != null) initWebView(state.homepageUrl) else no_home_page_view.visibility = View.VISIBLE

        showTutorial()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)

        if(state.githubUrl == null)
            menu.removeItem(R.id.open_on_github)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { drawer_layout.openDrawer(GravityCompat.START); true }
            R.id.open_on_github -> { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(state.githubUrl))); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }

    private fun adjustStatusBarTranslucency() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window: Window = window
            val windowParams: WindowManager.LayoutParams = window.attributes
            windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.attributes = windowParams
        }
    }

    private fun initToolbar(appTitle: String?) {
        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar?.title = appTitle
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_nav_drawer_menu_24dp)
    }

    private fun initNavDrawer(
        examplesDetails: List<ExampleActivityDetails>?,
        githubUrl: String?,
        playStorePackageName: String?
    ) {
        setNavigationViewWidth(navigation_view)

        val menu = navigation_view.menu
        examplesDetails?.forEachIndexed { index, element ->
            menu.add(R.id.nav_drawer_examples_group, index, 0, element.nameResource).setIcon(element.iconResource)
        }

        if(githubUrl == null)
            menu.removeItem(R.id.star_on_github)
        if(playStorePackageName == null)
            menu.removeItem(R.id.rate_on_playstore)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            drawer_layout.closeDrawers()

            val intent = when {
                examplesDetails != null && menuItem.itemId >= 0 && menuItem.itemId < examplesDetails.size -> Intent(this, examplesDetails[menuItem.itemId].clazz)
                menuItem.itemId == R.id.star_on_github -> Intent(Intent.ACTION_VIEW, Uri.parse("$githubUrl/stargazers"))
                menuItem.itemId == R.id.rate_on_playstore -> {
                    try {
                        Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$playStorePackageName"))
                    } catch (exception: ActivityNotFoundException) {
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$playStorePackageName"))
                    }
                }
                else -> return@setNavigationItemSelectedListener false
            }

            startActivity(intent)

            return@setNavigationItemSelectedListener true
        }
    }

    private fun initWebView(homePageUrl: String?) {
        webview.enableJavascript(true)
        webview.loadUrl(homePageUrl)
        webview.onUrlClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) }
    }

    private fun setNavigationViewWidth(navigationView: NavigationView) {
        val params = navigationView.layoutParams
        val width = getScreenWidth() - toolbar.layoutParams.height
        val _320dp = resources.getDimensionPixelSize(R.dimen._320dp)
        params.width = if (width > _320dp) _320dp else width
        navigationView.layoutParams = params
    }

    private fun getScreenWidth() : Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }


    private fun showTutorial() {
        val preferenceKey = "featureDiscoveryShown"
        val sharedPreferencesKey = "sampleApp_MainActivity_SharedPreferences"
        val prefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val featureDiscoveryShown = prefs.getBoolean(preferenceKey, false)

        if (featureDiscoveryShown)
            return
        else
            prefs.edit().putBoolean(preferenceKey, true).apply()

        val target = toolbar.getChildAt(1)

        TapTargetView.showFor(
            this,
            TapTarget.forView(
                target,
                getString(R.string.tutorial_title),
                getString(R.string.tutorial_description)
            )
                .outerCircleColor(R.color.github_black)
                .outerCircleAlpha(1f)
                .targetCircleColor(android.R.color.white)
                .titleTextColor(android.R.color.white)
                .drawShadow(true)
                .transparentTarget(true), object : TapTargetView.Listener() {
                override fun onTargetClick(view: TapTargetView) {
                    super.onTargetClick(view)
                    target.performClick()
                }
            })
    }
}
