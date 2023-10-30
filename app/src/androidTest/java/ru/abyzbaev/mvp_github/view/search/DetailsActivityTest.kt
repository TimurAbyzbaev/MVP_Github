package ru.abyzbaev.mvp_github.view.search


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.abyzbaev.mvp_github.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailsActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun detailsActivityTest() {
        val toDetailsButton = onView(
            allOf(
                withId(R.id.toDetailsActivityButton), withText("to detail"),
                isDisplayed()
            )
        )
        toDetailsButton.perform(click())

        val totalCountTextView = onView(
            allOf(
                withId(R.id.totalCountTextView),
                isDisplayed()
            )
        )
        totalCountTextView.check(matches(withText("Number of results: 0")))

        val incrementButton = onView(
            allOf(
                withId(R.id.incrementButton),
                isDisplayed()
            )
        )
        incrementButton.check(matches(isDisplayed()))

        val decrementButton = onView(
            allOf(
                withId(R.id.decrementButton),
                isDisplayed()
            )
        )
        decrementButton.check(matches(isDisplayed()))


        incrementButton.perform(click())

        totalCountTextView.check(matches(withText("Number of results: 1")))

        decrementButton.perform(click())
        
        totalCountTextView.check(matches(withText("Number of results: 0")))
    }
}
