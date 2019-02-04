package com.psoffritti.librarysampleapptemplate.sampleapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.psoffritti.librarysampleapptemplate.core.SampleAppTemplateActivity
import com.psoffritti.librarysampleapptemplate.core.utils.Constants
import com.psoffritti.librarysampleapptemplate.core.utils.ExampleActivityDetails


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, SampleAppTemplateActivity::class.java)

        intent.putExtra(Constants.TITLE.name, getString(R.string.title))
        intent.putExtra(Constants.HOMEPAGE_URL.name, "https://github.com/PierfrancescoSoffritti/library-sample-app-template/blob/master/README.md")
        intent.putExtra(Constants.GITHUB_URL.name, "https://github.com/PierfrancescoSoffritti/library-sample-app-template")
        intent.putExtra(Constants.PLAYSTORE_PACKAGE_NAME.name, "com.psoffritti.librarysampleapptemplate.sampleapp")

        val examples = arrayOf(
            ExampleActivityDetails(
                R.string.example_activity_1_name,
                R.drawable.ic_android_24dp,
                ExampleActivity1::class.java
            ),
            ExampleActivityDetails(
                R.string.example_activity_2_name,
                R.drawable.ic_insert_emoticon_24dp,
                ExampleActivity2::class.java
            )
        )

        intent.putExtra(Constants.EXAMPLES.name, examples)

        startActivity(intent)
        finish()
    }
}
