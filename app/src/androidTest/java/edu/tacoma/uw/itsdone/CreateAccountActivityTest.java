package edu.tacoma.uw.itsdone;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.Matchers.is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CreateAccountActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void testCreateAccountBadPassword() throws InterruptedException {
        Random random = new Random();
        //Generate an email address
        String username = "username" + (random.nextInt(10000) + 1)
                + (random.nextInt(900) + 1) + (random.nextInt(700) + 1);

        onView(withId(R.id.create_account_button_login_screen))
                .perform(click());
        Thread.sleep(1000);
        // Type text and then press the button.
        onView(withId(R.id.username))
                .perform(typeText(username));
        onView(withId(R.id.password))
                .perform(typeText("test"));
        onView(withId(R.id.first_name))
                .perform(typeText("test"));
        onView(withId(R.id.email))
                .perform(typeText("test@uw.edu"));
        onView(withId(R.id.last_name))
                .perform(typeText("test"));
        closeSoftKeyboard();
        Thread.sleep(250);
        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withText("Password must be 6 or more characters long"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCreateAccountBadEmail() throws InterruptedException {
        Random random = new Random();
        //Generate an email address
        String username = "username" + (random.nextInt(10000) + 1)
                + (random.nextInt(900) + 1) + (random.nextInt(700) + 1);

        onView(withId(R.id.create_account_button_login_screen))
                .perform(click());
        Thread.sleep(1000);
        // Type text and then press the button.
        onView(withId(R.id.username))
                .perform(typeText(username));
        onView(withId(R.id.password))
                .perform(typeText("test1@3"));
        onView(withId(R.id.first_name))
                .perform(typeText("test"));
        onView(withId(R.id.email))
                .perform(typeText("test"));
        onView(withId(R.id.last_name))
                .perform(typeText("test"));
        closeSoftKeyboard();
        Thread.sleep(250);;
        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withText("please enter a valid email"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCreateAccount() throws InterruptedException {
        Random random = new Random();
        //Generate an email address
        String username = "username" + (random.nextInt(10000) + 1)
                + (random.nextInt(900) + 1) + (random.nextInt(700) + 1);
        String email = "email" + (random.nextInt(10000) + 1)
                + (random.nextInt(900) + 1) + (random.nextInt(700) + 1)
                + (random.nextInt(400) + 1) + (random.nextInt(100) + 1)
                + "@uw.edu";

        onView(withId(R.id.create_account_button_login_screen))
                .perform(click());
        Thread.sleep(1000);
        // Type text and then press the button.
        onView(withId(R.id.username))
                .perform(typeText(username));
        onView(withId(R.id.password))
                .perform(typeText("test1@3"));
        onView(withId(R.id.first_name))
                .perform(typeText("test"));
        onView(withId(R.id.email))
                .perform(typeText(email));
        onView(withId(R.id.last_name))
                .perform(typeText("test"));
        closeSoftKeyboard();
        Thread.sleep(250);;
        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withId(R.id.sign_in_edit_text))
                .perform(typeText(username));
        onView(withId(R.id.password_edit_text))
                .perform(typeText("test1@3"));
        closeSoftKeyboard();
        Thread.sleep(250);
        onView(withId(R.id.login_button))
                .perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.FindJobButton))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
