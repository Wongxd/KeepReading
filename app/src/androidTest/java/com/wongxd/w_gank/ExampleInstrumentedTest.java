package com.wongxd.w_gank;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.wongxd.w_gank", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void sayHello(){
//        onView(withId(R.id.editText)).perform(typeText(), closeSoftKeyboard()); //line 1
//
//        onView(withText("Say hello!")).perform(click()); //line 2
//
//        String expectedText = "Hello, " + STRING_TO_BE_TYPED + "!";
//        onView(withId(R.id.textView)).check(matches(withText(expectedText))); //line 3
    }
}
