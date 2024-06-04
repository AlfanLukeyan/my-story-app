package com.a004d4ky3983.mystoryapp.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.a004d4ky3983.mystoryapp.R
import com.a004d4ky3983.mystoryapp.util.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    private val email = "anakabah20@gmail.com"
    private val password = "qwertyuio"

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login_logout_success() {
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_email)).perform(
            ViewActions.typeText(email),
            closeSoftKeyboard()
        )

        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).perform(
            ViewActions.typeText(password),
            closeSoftKeyboard()
        )

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.rvStories)).check(matches(isDisplayed()))

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.action_logout)).check(matches(isDisplayed()))
        onView(withText(R.string.action_logout)).perform(click())

        onView(withText(R.string.confirmation_logout)).check(matches(isDisplayed()))

        onView(withText(R.string.yes)).check(matches(isDisplayed()))
        onView(withText(R.string.yes)).perform(click())

        onView(withText(R.string.title_welcome_page)).check(matches(isDisplayed()))
    }
}