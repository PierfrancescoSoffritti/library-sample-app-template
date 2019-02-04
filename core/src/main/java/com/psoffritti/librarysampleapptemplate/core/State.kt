package com.psoffritti.librarysampleapptemplate.core

import android.os.Bundle

class State private constructor(val title: String?, val homepageUrl: String?, val githubUrl: String?, val playStorePackageName: String?, val examples: List<ExampleActivityDetails>?) {

    companion object {
        fun getInstance(bundle: Bundle): State {
            return State(getAppTitle(bundle), getHomePageUrl(bundle), getGitHubUrl(bundle), getPlayStorePackageName(bundle), getExamplesActivityDetails(bundle))
        }

        private fun getPlayStorePackageName(bundle: Bundle): String? {
            return bundle.getString(Constants.PLAYSTORE_PACKAGE_NAME.name)
        }

        private fun getGitHubUrl(bundle: Bundle): String? {
            return bundle.getString(Constants.GITHUB_URL.name)
        }

        private fun getAppTitle(bundle: Bundle): String? {
            return bundle.getString(Constants.TITLE.name)
        }

        private fun getHomePageUrl(bundle: Bundle): String? {
            return bundle.getString(Constants.HONEPAGE_URL.name)
        }

        private fun getExamplesActivityDetails(bundle: Bundle): List<ExampleActivityDetails>? {
            val items: Array<*>? = bundle.get(Constants.EXAMPLES.name) as Array<*>?
            return items?.filterIsInstance<ExampleActivityDetails>()
        }
    }
}
