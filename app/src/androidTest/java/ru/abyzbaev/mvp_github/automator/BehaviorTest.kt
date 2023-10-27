package ru.abyzbaev.mvp_github.automator

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import junit.framework.TestCase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.abyzbaev.mvp_github.BuildConfig
import ru.abyzbaev.mvp_github.R

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageName = context.packageName
    private val uiDevice = UiDevice.getInstance(getInstrumentation())

    companion object {
        private const val TIMEOUT = 5000L
    }

    @Before
    fun setup() {
        uiDevice.pressHome()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }


    @Test
    fun test_MainActivityIsStarted() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        Assert.assertNotNull(editText)
    }

    @Test
    fun test_SearchIsPositive() {
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        editText.text = "UiAutomator"
        searchButton.click()

        val changedText = uiDevice.wait(
            Until.findObject(By.res(packageName, "totalCountTextViewMainActivity")),
            TIMEOUT
        )
        if(BuildConfig.TYPE == "FAKE") {
            Assert.assertEquals(changedText.text.toString(), "Number of results: 999")
        } else {
            Assert.assertNotNull(changedText)
        }

    }

    @Test
    fun test_openDetailsScreen() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.click()
        val changedText = uiDevice.wait(
            Until.findObject(By.res(packageName, "totalCountTextView")),
            TIMEOUT
        )
        Assert.assertEquals(changedText.text.toString(), "Number of results: 0")
    }

    @Test
    fun test_detailsScreenResultsIsCorrect() {
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))

        editText.text = "UiAutomator"

        searchButton.click()

        val  totalCountTextViewMainActivity = uiDevice.wait(
            Until.findObject(By.res(packageName, "totalCountTextViewMainActivity")),
            TIMEOUT
        )
        val comparableText = totalCountTextViewMainActivity.text.toString()

        toDetails.click()

        val changedText = uiDevice.wait(
            Until.findObject(By.res(packageName, "totalCountTextView")),
            TIMEOUT
        )

        Assert.assertEquals(comparableText, changedText.text.toString())
    }

    @Test
    fun detailsActivity_ButtonIncrementIsWorking() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.click()

        val totalCount = uiDevice.wait(
            Until.findObject(By.res(packageName, "totalCountTextView")),
            TIMEOUT
        )
        val incrementButton = uiDevice.findObject(By.res(packageName, "incrementButton"))
        incrementButton.click()
        Assert.assertEquals("Number of results: 1", totalCount.text.toString())
    }
    @Test
    fun detailsActivity_ButtonDecrementIsWorking() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.click()

        val totalCount = uiDevice.wait(
            Until.findObject(By.res(packageName, "totalCountTextView")),
            TIMEOUT
        )
        val decrementButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        decrementButton.click()
        Assert.assertEquals("Number of results: -1", totalCount.text.toString())
    }
}