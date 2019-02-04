package com.psoffritti.librarysampleapptemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.psoffritti.librarysampleapptemplate.core.utils.Constants
import com.psoffritti.librarysampleapptemplate.core.utils.ExampleActivityDetails
import com.psoffritti.librarysampleapptemplate.core.SampleAppTemplateActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, SampleAppTemplateActivity::class.java)

        intent.putExtra(Constants.TITLE.name, getString(R.string.title))
        intent.putExtra(Constants.HOMEPAGE_URL.name, "https://github.com/PierfrancescoSoffritti/library-sample-app-template/blob/master/README.md")
        intent.putExtra(Constants.GITHUB_URL.name, "https://github.com/PierfrancescoSoffritti/library-sample-app-template")
        intent.putExtra(Constants.PLAYSTORE_PACKAGE_NAME.name, "title")

        val examples = arrayOf(
            ExampleActivityDetails(
                R.string.example_activity_1,
                R.drawable.ic_android_24dp,
                ExampleActivity1::class.java
            ),
            ExampleActivityDetails(
                R.string.example_activity_2,
                R.drawable.ic_insert_emoticon_24dp,
                ExampleActivity2::class.java
            )
        )

        intent.putExtra(Constants.EXAMPLES.name, examples)

        startActivity(intent)
        finish()
    }
}
