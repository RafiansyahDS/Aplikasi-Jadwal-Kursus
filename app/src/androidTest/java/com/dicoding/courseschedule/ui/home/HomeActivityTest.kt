import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.home.HomeActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest {

    @Before
    fun setup(){
        ActivityScenario.launch(HomeActivity::class.java)
    }


    @Test
    fun addCourseActivity() {
        onView(withId(R.id.action_add)).check(matches(isDisplayed()))
        onView(withId(R.id.action_add)).perform(click())
        onView(withId(R.id.edt_course_name)).check(matches(isDisplayed()))
        onView(withId(R.id.edt_lecturer)).check(matches(isDisplayed()))
        onView(withId(R.id.edt_note)).check(matches(isDisplayed()))
        onView(withId(R.id.imb_end_time)).check(matches(isDisplayed()))
        onView(withId(R.id.imb_start_time)).check(matches(isDisplayed()))
    }
}
