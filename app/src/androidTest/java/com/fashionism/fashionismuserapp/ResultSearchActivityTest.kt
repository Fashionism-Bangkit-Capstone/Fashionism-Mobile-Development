package com.fashionism.fashionismuserapp

import android.content.Intent
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fashionism.fashionismuserapp.data.model.FashionRecommendation
import com.fashionism.fashionismuserapp.ui.home.ResultSearchActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResultSearchActivityTest {

    private fun launch(links: List<String>, prices: List<String>): ActivityScenario<ResultSearchActivity> {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, ResultSearchActivity::class.java)
            .putExtra(ResultSearchActivity.EXTRA_RECOMMENDATION, FashionRecommendation(prices, links))
        return ActivityScenario.launch(intent)
    }

    @Test
    fun fewerThanFiveResults_showsOnlyTheCardsThatHaveData() {
        launch(links = listOf("a", "b", "c"), prices = listOf("1", "2", "3")).use { scenario ->
            scenario.onActivity { activity ->
                fun visibility(id: Int) = activity.findViewById<View>(id).visibility
                assertEquals(View.VISIBLE, visibility(R.id.CV1))
                assertEquals(View.VISIBLE, visibility(R.id.CV2))
                assertEquals(View.GONE, visibility(R.id.CV3))
                assertEquals(View.GONE, visibility(R.id.CV4))
            }
        }
    }

    @Test
    fun listsOfDifferentLength_onlyUseTheShorterOne() {
        launch(links = listOf("a", "b", "c", "d", "e"), prices = listOf("1", "2")).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals(View.VISIBLE, activity.findViewById<View>(R.id.CV1).visibility)
                assertEquals(View.GONE, activity.findViewById<View>(R.id.CV2).visibility)
            }
        }
    }

    @Test
    fun noResults_closesTheScreenInsteadOfCrashing() {
        launch(links = emptyList(), prices = emptyList()).use { scenario ->
            assertEquals(Lifecycle.State.DESTROYED, scenario.state)
        }
    }
}
