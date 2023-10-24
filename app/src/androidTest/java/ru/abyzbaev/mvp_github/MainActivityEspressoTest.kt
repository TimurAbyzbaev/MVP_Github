package ru.abyzbaev.mvp_github

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.abyzbaev.mvp_github.view.search.MainActivity

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }
    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test
    fun activityButton_IsEffectiveVisible() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
    @Test
    fun activityButton_IsHaveCorrectText() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withText("TO DETAIL")))
    }
    @Test
    fun activityButton_IsClickable() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(isClickable()))
    }

    @Test
    fun activityTextEdit_IsEffectiveVisible() {
        onView(withId(R.id.searchEditText)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
    @Test
    fun activityTextEdit_IsFocusable() {
        onView(withId(R.id.searchEditText)).check(matches(isFocusable()))
    }
    @Test
    fun activityTextEdit_IsHaveHint() {
        onView(withId(R.id.searchEditText)).check(matches(withHint("Enter keyword e.g. android")))
    }




    @Test
    fun activitySearch_IsWorking() {
        val assert = withId(R.id.searchEditText)
        onView(assert).perform(click())
        onView(assert).perform(replaceText("algol"), closeSoftKeyboard())
        onView(assert).perform(pressImeActionButton())
        //if (BuildConfig.TYPE == MainActivity.FAKE) {
        if (BuildConfig.TYPE == "FAKE") {
            onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 999")))
        } else {
            onView(isRoot()).perform(delay())
            /**
             * the number of results may change over time.
             * Check the number of results manually and change the number in the test if necessary.
             */
            onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 3971")))
        }
    }

    private fun delay(): ViewAction? {
        return object : ViewAction {
            override fun getDescription(): String = "wait for $2 seconds"

            override fun getConstraints(): Matcher<View> = isRoot()

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }
}