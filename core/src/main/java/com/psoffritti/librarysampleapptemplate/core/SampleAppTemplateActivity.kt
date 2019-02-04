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
import android.webkit.WebView
import android.webkit.WebViewClient

import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.material.navigation.NavigationView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class SampleAppTemplateActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var webview: WebView
    private lateinit var drawerLayout: DrawerLayout

    private var toolbarHeight = 0

    private lateinit var state: State

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_app_template)

        state = State.getInstance(intent.extras!!)

        initWebView(state.homepageUrl)

        adjustStatusBarTranslucency()
        initToolbar(state.title)
        initNavDrawer(state.examples, state.githubUrl, state.playStorePackageName)

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
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.open_on_github -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(state.githubUrl)))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            webview.canGoBack() -> webview.goBack()
            else -> super.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(homePageUrl: String?) {
        webview = findViewById(R.id.main_activity_webview)
        webview.settings.javaScriptEnabled = true
        webview.loadUrl(homePageUrl)

        webview.webViewClient = object : WebViewClient() {

            override fun onPageCommitVisible(view: WebView, url: String) {
                super.onPageCommitVisible(view, url)
                findViewById<View>(R.id.progressbar).visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (url.startsWith("http://") || url.startsWith("https://")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    true
                } else
                    false
            }
        }
    }

    private fun adjustStatusBarTranslucency() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            val windowParams = window.attributes
            windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.attributes = windowParams
        }
    }

    private fun initToolbar(appTitle: String?) {
        toolbar = findViewById(R.id.toolbar)
        toolbarHeight = toolbar.layoutParams.height

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
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        setNavigationViewWidth(navigationView)

        val menu = navigationView.menu
        examplesDetails?.forEachIndexed { index, element ->
            menu.add(R.id.nav_drawer_examples_group, index, 0, element.nameResource).setIcon(element.iconResource)
        }

        if(githubUrl == null)
            menu.removeItem(R.id.star_on_github)
        if(playStorePackageName == null)
            menu.removeItem(R.id.rate_on_playstore)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawers()

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

    private fun setNavigationViewWidth(navigationView: NavigationView) {
        val params = navigationView.layoutParams
        val width = getScreenWidth() - toolbarHeight
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

        val toolbar: Toolbar = findViewById(R.id.toolbar)
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
