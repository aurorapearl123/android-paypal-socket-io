package com.example.ian.paypalpayment;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private LoginActivity loginActivity = null;

    @Before
    public void setUp() throws Exception {

        loginActivity = loginActivityActivityTestRule.getActivity();
    }

    @Test
    public void testAllButton()
    {
        assertNotNull(loginActivity.findViewById(R.id.id_user_name));
        assertNotNull(loginActivity.findViewById(R.id.id_password));

        ViewInteraction loginButton = onView(withId(R.id.id_btn_login));

        loginButton.perform(click());
        //username rosales@gmail.com
       ViewInteraction editTextUserName = onView(withId(R.id.id_user_name)).perform(typeText(""), closeSoftKeyboard());
       editTextUserName.check(matches(hasErrorText("enter a valid email address")));
       //password rosales123
        ViewInteraction editTextPassword = onView(withId(R.id.id_password)).perform(typeText(""), closeSoftKeyboard());
        editTextPassword.check(matches(hasErrorText("between 4 and 20 alphanumeric characters")));
    }

    @After
    public void tearDown() throws Exception {
        loginActivity =  null;
    }
}