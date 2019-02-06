# library-sample-app-template

[![core](https://api.bintray.com/packages/pierfrancescosoffritti/maven/library-sample-app-template%3Acore/images/download.svg) ](https://bintray.com/pierfrancescosoffritti/maven/library-sample-app-template%3Acore/_latestVersion)
[![share on twitter](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=An%20easier%20way%20to%20build%20sample%20apps%20for%20Android%20libraries:&url=https://github.com/PierfrancescoSoffritti/library-sample-app-template&via=PierfrancescoSo&hashtags=opensource,sampleapps,androiddev)

An Android library to bootstrap sample apps for other Android projects. This library is useful to reduce the boilerplate code needed when writing a sample app for a project.

### Projects using this library for their sample apps:
* [android-youtube-player](https://github.com/PierfrancescoSoffritti/android-youtube-player)
* [sliding-panel](https://github.com/PierfrancescoSoffritti/sliding-panel)
* [library-sample-app-template](https://github.com/PierfrancescoSoffritti/library-sample-app-template)

![showcase](./images/merged.jpg)

# Sample app
You can download the apk for the sample app of this library [at this link](./sample-app/apk), or [on the PlayStore](https://play.google.com/store/apps/details?id=com.psoffritti.librarysampleapptemplate.sampleapp).

<a href='https://play.google.com/store/apps/details?id=com.psoffritti.librarysampleapptemplate.sampleapp&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'>
    <img width='200px' alt='Get it on Google Play'
         src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/>
</a>

The code of the sample app is available [at this link](./sample-app/).

Having the sample apps installed is a good way to be notified of new releases. Although watching this repository will allow GitHub to email you whenever a new release is published.

# Download
The Gradle dependency is available via [jCenter](https://bintray.com/pierfrancescosoffritti/maven). jCenter is the default Maven repository used by Android Studio.

The minimum API level supported by this library is API 15.

```
dependencies {
  implementation 'com.psoffritti.librarysampleapptemplate:core:1.0.0'
}
```

# Usage
This library provides an Activity to be used as starting point for building sample apps.

To use the library you need to launch the library's Activity (`SampleAppTemplateActivity`) from your sample app's main Activity.

`SampleAppTemplateActivity` has a set of optional Views ([screenshot1](./images/main_screen.jpg), [screenshot2](./images/nav_drawer.jpg), [screenshot3](./images/tutorial.jpg)):
* a `Toolbar` with a title and an icon that, when clicked, sends the user to the project's GitHub page.
* a `NavigationDrawer` that contains: 
  * a set of links to launch other Activities created by the developer. Each Activity is meant to be an example of the project.
  * a link to leave a star on the project's GitHub page.
  * a link to the sample app's page on the PlayStore.
* a `WebView` that loads the homepage of the project. The homepage could be a custom website or the readme of the project.

These optional Views are configured by putting extras into the Intent that launches the Activity.

---

To use `SampleAppTemplateActivity`, first add it to your manifest.
```xml
<activity android:name="com.psoffritti.librarysampleapptemplate.core.SampleAppTemplateActivity" />
```
Make sure that the theme of the `<application>` element in your manifes extends a theme with no action bar.
```xml
<style name="AppTheme" parent="Theme.MaterialComponents.Light.NoActionBar">
  <item name="colorPrimary">@color/colorPrimary</item>
  <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
  <item name="colorAccent">@color/colorAccent</item>
</style>
```
Every Activity can use the theme you prefer, with or without actionbar. Only the main theme needs to descend from a `NoActionBar` theme.

Your manifest should look something like this.
```xml
<application
  android:theme="@style/AppTheme">
  <activity android:name=".MainActivity">
      <intent-filter>
          <action android:name="android.intent.action.MAIN" />
          <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
  </activity>
  <activity android:name=".ExampleActivity1" />
  <activity android:name=".ExampleActivity2" android:theme="@style/Base.Theme.MaterialComponents.Light.DarkActionBar"/>
  
  <activity android:name="com.psoffritti.librarysampleapptemplate.core.SampleAppTemplateActivity" />
</application>
```
Then, to start `SampleAppTemplateActivity` create the Intent in your main Activity.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  
  val intent = Intent(this, SampleAppTemplateActivity::class.java)

  intent.putExtra(Constants.TITLE.name, getString(R.string.title))
  intent.putExtra(Constants.GITHUB_URL.name, "https://github.com/username/repo")

  val examples = arrayOf(
    ExampleActivityDetails(
      R.string.name,
      R.drawable.icon,
      ExampleActivity::class.java
    )
  )

  intent.putExtra(Constants.EXAMPLES.name, examples)

  startActivity(intent)

  finish()
}
```
When you run this code, the sample app's main Activity will create this Intent, then start `SampleAppTemplateActivity` and then finish itself.

The Intent extras are used to configure the sample app to fit your project. You can [read here what each extra does](#intent-extras).

# Intent extras
Set these extras to the Intent that launches `SampleAppTemplateActivity` to customize the Activity. Every value is optional, if you don't need it just don't set it.

### Constants.TITLE
A `String` containing the title of your app.

```kotlin
intent.putExtra(Constants.TITLE.name, getString(R.string.title))
```

**If set**, the title will be displayed in the Toolbar of the `SampleAppTemplateActivity`.

**If not set**, the title section of the Toolbar will be empty.

### Constants.HOMEPAGE_URL
A `String` containing the URL of your porject's homepage. Could be a custom website or your README file.

```kotlin
intent.putExtra(Constants.HOMEPAGE_URL.name, "https://yourhomepage.com")
```

**If set**, the webpage will be loaded in `SampleAppTemplateActivity`'s WebView.

**If not set**, the WebView will be empty and the message `"This library doesn't have a homepage"` will be shown at the center of the screen.

### Constants.GITHUB_URL
A `String` containing the URL of your porject's GitHub repository.

```kotlin
intent.putExtra(Constants.HOMEPAGE_URL.name, "https://github.com/user/repo")
```

**If set** 
1. the Toolbar will have an icon with the GitHub logo, when clicked this icon sends the user to the project's GitHub page.
2. the NavigationDrawer will show a link to star the project on GitHub.

**If not set**, the icon in the Toolbar and the link in the NavigationDrawer won't be shown.

### Constants.PLAYSTORE_PACKAGE_NAME
A `String` containing the package name used to publish your sample app on the PlayStore.

```kotlin
intent.putExtra(Constants.PLAYSTORE_PACKAGE_NAME.name, "com.project.sampleapp")
```

**If set**, the NavigationDrawer will show a link to the sample app's page on the PlayStore.

**If not set**, the link in the NavigationDrawer won't be shown.

### Constants.EXAMPLES
An `Array` of `ExampleActivityDetails`. You need to use this extra to specify the Activities the showcase examples of your projects.

`ExampleActivityDetails` takes three arguments:
* the resource id of the String you want to use as the name of the Activity. It will be shown in the NavigationDrawer.
* the resource id of the Drawable you want to use as icon for this Activity. It will be shown in the NavigationDrawer. The icon is optional, you can pass null.
* the class Object of the Activity. It will be used to launch the Activity when the item in the NavigationDrawer is clicked.

```kotlin
val examples = arrayOf(
  ExampleActivityDetails(
      R.string.name1,
      R.drawable.icon1,
      ExampleActivity1::class.java
  ),
  ExampleActivityDetails(
      R.string.name2,
      null,
      ExampleActivity2::class.java
  )
)

intent.putExtra(Constants.EXAMPLES.name, examples)
```

**If set**, a list of links to launch each Activity will be shown in the NavigationDrawer.

**If not set**, the links won't be shown in the NavigationDrawer.

# Appearance
`SampleAppTemplateActivity` will inherit colors and styles from your application's theme. Change them in the same way you would for you application, by using `colorPrimary`, `colorPrimaryDark` and `accentColor`.

If you want to customize the color of the [tutorial widget](./images/tutorial.jpg), define this attributes in your app's theme: 
- `lsat_tutorial_background_color`: changes the color of the bigger background circle.
- `lsat_tutorial_target_circle_color`: changes the color of the smaller intermittent cricle.
- `lsat_tutorial_text_color`: changes the color of the text.


In the end your app's theme should look something like this.
```xml
<resources>
  <style name="AppTheme" parent="Theme.MaterialComponents.Light.NoActionBar">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>

    <item name="lsat_tutorial_background_color">@color/tutorial_background_color</item>
    <item name="lsat_tutorial_target_circle_color">@color/tutorial_target_circle_color</item>
    <item name="lsat_tutorial_text_color">@color/tutorial_text_color</item>
  </style>
</resources>
```

---

For any question feel free to [open an issue on the GitHub repository](https://github.com/PierfrancescoSoffritti/library-sample-app-template/issues).