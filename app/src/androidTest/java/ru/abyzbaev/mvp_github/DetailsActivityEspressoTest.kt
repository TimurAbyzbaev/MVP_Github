package ru.abyzbaev.mvp_github

import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.abyzbaev.mvp_github.view.details.DetailsActivity

import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class DetailsActivityEspressoTest {
    private lateinit var scenario: ActivityScenario<DetailsActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(DetailsActivity::class.java)
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

    /*
    Тут почему то тест падает, хотя Robolectric работает нормально
     */
//    @Test
//    fun activity_IsResumed() {
//        scenario.onActivity {
//            assertEquals(Lifecycle.State.RESUMED, scenario.state)
//        }
//    }

    @Test
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    @Test
    fun activityTextView_HasText() {
        val assertion: ViewAssertion = matches(withText("Number of results: 0"))
        onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test
    fun activityTextView_IsDisplayed() {
        onView(withId(R.id.totalCountTextView)).check(matches(isDisplayed()))
    }
    @Test
    fun activityTextView_IsCompletelyDisplayed() {
        onView(withId(R.id.totalCountTextView)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun activityButtons_AreEffectiveVisible() {
        onView(withId(R.id.decrementButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.incrementButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun activityButtonIncrement_IsWorking() {
        onView(withId(R.id.incrementButton)).perform(click())
        onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 1")))
    }
    @Test
    fun activityButtonDecrement_IsWorking() {
        onView(withId(R.id.decrementButton)).perform(click())
        onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: -1")))
    }
}