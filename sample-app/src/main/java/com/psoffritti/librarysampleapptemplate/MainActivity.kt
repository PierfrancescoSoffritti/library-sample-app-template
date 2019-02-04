package com.psoffritti.librarysampleapptemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.psoffritti.librarysampleapptemplate.core.Constants
import com.psoffritti.librarysampleapptemplate.core.ExampleActivityDetails
import com.psoffritti.librarysampleapptemplate.core.SampleAppTemplateActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, SampleAppTemplateActivity::class.java)

        intent.putExtra(Constants.TITLE.name, "title")
        intent.putExtra(Constants.HONEPAGE_URL.name, "https://github.com/PierfrancescoSoffritti/sliding-drawer/blob/master/README.md")
        intent.putExtra(Constants.GITHUB_URL.name, "title")
        intent.putExtra(Constants.PLAYSTORE_URL.name, "title")

        val examples = arrayOf(
            ExampleActivityDetails(
                R.string.example_activity_1,
                R.drawable.ic_github_24dp,
                Example1Activity::class.java
            ),
            ExampleActivityDetails(
                R.string.example_activity_2,
                R.drawable.ic_github_24dp,
                Example1Activity::class.java
            )
        )

        intent.putExtra(Constants.EXAMPLES.name, examples)

        startActivity(intent)
        finish()
    }
}
