package com.psoffritti.librarysampleapptemplate.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.psoffritti.librarysampleapptemplate.core.utils.Configuration
import com.psoffritti.librarysampleapptemplate.core.utils.ExampleActivityDetails
import com.psoffritti.librarysampleapptemplate.core.utils.Utils.addItems
import com.psoffritti.librarysampleapptemplate.core.utils.Utils.getScreenWidth
import com.psoffritti.librarysampleapptemplate.core.utils.Utils.openUri
import com.psoffritti.librarysampleapptemplate.core.utils.Utils.setStatusBarTranslucency
import com.psoffritti.librarysampleapptemplate.core.utils.Utils.setWidth
import kotlinx.android.synthetic.main.lsat_activity_sample_app_template.*

/**
 * This Activity is meant to be used as a template for sample applications.
 *
 * You can configure many properties by passing extras to the Activity Intent. To learn more [read the documentation](https://github.com/PierfrancescoSoffritti/library-sample-app-template).
 */
class SampleAppTemplateActivity : AppCompatActivity() {

    private lateinit var configuration: Configuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lsat_activity_sample_app_template)

        configuration = Configuration.getInstance(intent.extras ?: Bundle())

        setStatusBarTranslucency()

        initToolbar(configuration.title)
        initNavDrawer(configuration.examples, configuration.githubUrl, configuration.playStorePackageName)

        if(configuration.homepageUrl != null) initWebView(configuration.homepageUrl) else no_home_page_view.visibility = View.VISIBLE

        showTutorial()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lsat_main_activity_menu, menu)
        if(configuration.githubUrl == null)
            menu.removeItem(R.id.open_on_github)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { drawer_layout.openDrawer(GravityCompat.START); true }
            R.id.open_on_github -> { openUri(configuration.githubUrl); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }

    private fun initToolbar(appTitle: String?) {
        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar?.title = appTitle
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.drawable.lsat_ic_nav_drawer_menu_24dp)
    }

    private fun initNavDrawer(
        examplesDetails: List<ExampleActivityDetails>?,
        githubUrl: String?,
        playStorePackageName: String?
    ) {
        navigation_view.setWidth(getScreenWidth() - toolbar.layoutParams.height)

        navigation_view.menu.addItems(examplesDetails)
        if(githubUrl == null)
            navigation_view.menu.removeItem(R.id.star_on_github)
        if(playStorePackageName == null)
            navigation_view.menu.removeItem(R.id.rate_on_playstore)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            drawer_layout.closeDrawers()

            when {
                examplesDetails != null && menuItem.itemId >= 0 && menuItem.itemId < examplesDetails.size -> startActivity(Intent(this, examplesDetails[menuItem.itemId].clazz))
                menuItem.itemId == R.id.star_on_github ->  openUri("$githubUrl/stargazers")
                menuItem.itemId == R.id.rate_on_playstore -> {
                    try {
                        openUri("market://details?id=$playStorePackageName")
                    } catch (exception: ActivityNotFoundException) {
                        openUri("https://play.google.com/store/apps/details?id=$playStorePackageName")
                    }
                }
                else -> return@setNavigationItemSelectedListener false
            }

            return@setNavigationItemSelectedListener true
        }
    }

    private fun initWebView(homePageUrl: String?) {
        webview.enableJavascript(true)
        webview.loadUrl(homePageUrl)
        webview.onUrlClick = { openUri(it) }
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
                getString(R.string.lsat_tutorial_title),
                getString(R.string.lsat_tutorial_description)
            )
                .outerCircleColor(R.color.tutorial_background_color)
                .outerCircleAlpha(1f)
                .targetCircleColor(R.color.tutorial_target_circle_color)
                .titleTextColor(R.color.tutorial_text_color)
                .drawShadow(true)
                .transparentTarget(true), object : TapTargetView.Listener() {
                override fun onTargetClick(view: TapTargetView) {
                    super.onTargetClick(view)
                    target.performClick()
                }
            })
    }
}
