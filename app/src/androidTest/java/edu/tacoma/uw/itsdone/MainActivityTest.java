package edu.tacoma.uw.itsdone;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void testLoginIncorrect() throws InterruptedException {
        Random random = new Random();
        //Generate an email address
        String username = "username" + (random.nextInt(10000) + 1)
                + (random.nextInt(900) + 1) + (random.nextInt(700) + 1);

        // Type text and then press the button.
        onView(withId(R.id.sign_in_edit_text))
                .perform(typeText(username));
        onView(withId(R.id.password_edit_text))
                .perform(typeText("test1@#"));
        closeSoftKeyboard();
        Thread.sleep(250);;
        onView(withId(R.id.login_button))
                .perform(click());

        onView(withText("incorrect username/password pairNo value for error"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginCorrect() throws InterruptedException {
        Random random = new Random();
        //Generate an email address
        String username = "Fadepaw";

        // Type text and then press the button.
        onView(withId(R.id.sign_in_edit_text))
                .perform(typeText(username));
        onView(withId(R.id.password_edit_text))
                .perform(typeText("Fadepaw1@3"));
        closeSoftKeyboard();
        Thread.sleep(250);
        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.FindJobButton))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

}
