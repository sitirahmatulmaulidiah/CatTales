package com.example.latihan_praktikum_7;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.latihan_praktikum_7.presentation.ui.login.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginFragmentTest {

    @Before
    public void setUp() {
        Intents.init();
        ActivityScenario.launch(LoginActivity.class);
    }

    @Test
    public void testEmailPasswordInputAndButton() {
        onView(withId(R.id.email_input))
                .perform(typeText("testuser@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.password_input))
                .perform(typeText("1234567890"), closeSoftKeyboard());

        onView(withId(R.id.email_login_btn)).perform(click());

        // Cek tombol masih tampil (tidak crash)
        onView(withId(R.id.email_login_btn)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
