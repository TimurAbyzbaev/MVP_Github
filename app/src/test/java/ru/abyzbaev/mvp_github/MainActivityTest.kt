package ru.abyzbaev.mvp_github

import android.content.Intent
import android.os.Build
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockito_kotlin.mock
import org.mockito.Mockito.*
import org.mockito.Mock
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowToast
import ru.abyzbaev.mvp_github.view.details.DetailsActivity
import ru.abyzbaev.mvp_github.view.search.MainActivity

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {
    lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity { it ->
            assertNotNull(it)
        }
    }

    @Test
    fun activity_isResumed() {
        scenario.onActivity {
            assertEquals(Lifecycle.State.RESUMED, scenario.state)
        }
    }

    @Test
    fun activityTextEdit_NotNull() {
        scenario.onActivity {
            val editText: EditText = it.findViewById(R.id.searchEditText)
            assertNotNull(editText)
        }
    }

    @Test
    fun activityTextEdit_IsVisible() {
        scenario.onActivity {
            val editText: EditText = it.findViewById(R.id.searchEditText)
            assertEquals(View.VISIBLE, editText.visibility)
        }
    }

    @Test
    fun activityTextEditSetText_Test() {
        scenario.onActivity {
            val editText: EditText = it.findViewById(R.id.searchEditText)
            editText.setText(TEST_QUERY, TextView.BufferType.EDITABLE)
            assertNotNull(editText.text)
            assertEquals(TEST_QUERY, editText.text.toString())
        }
    }

    @Test
    fun activityTextEdit_Search() {
        scenario.onActivity {
            val editText: EditText = it.findViewById(R.id.searchEditText)
            editText.setText(TEST_QUERY, TextView.BufferType.EDITABLE)
            editText.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
            Toast.makeText(
                ApplicationProvider.getApplicationContext(),
                editText.text.toString(),
                Toast.LENGTH_SHORT).show()
            assertEquals(TEST_QUERY, ShadowToast.getTextOfLatestToast())
        }
    }

    @Test
    fun activityToDetailsActivityButton_NotNull() {
        scenario.onActivity {
            val button = it.findViewById<Button>(R.id.toDetailsActivityButton)
            assertNotNull(button)
        }
    }
    @Test
    fun activityToDetailsActivityButton_IsVisible() {
        scenario.onActivity {
            val button = it.findViewById<Button>(R.id.toDetailsActivityButton)
            assertEquals(View.VISIBLE, button.visibility)
        }
    }
    @Test
    fun activityToDetailsActivityButton_IsWorking() {

        scenario.onActivity {
            val button = it.findViewById<Button>(R.id.toDetailsActivityButton)
            button.performClick()
            val nextActivity = Shadows.shadowOf(it).nextStartedActivity
            assertEquals(DetailsActivity::class.java.name, nextActivity.component?.className)
        }
    }
}