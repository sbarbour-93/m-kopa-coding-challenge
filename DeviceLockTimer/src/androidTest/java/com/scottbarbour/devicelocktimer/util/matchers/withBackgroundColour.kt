package com.scottbarbour.devicelocktimer.util.matchers

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description


class HasBackgroundColour {

    companion object {
        fun withBackgroundColour(@ColorRes expectedColourResId: Int): BoundedMatcher<View, View> {
            val matcher = object : BoundedMatcher<View, View>(View::class.java) {
                private var expectedColour: Int = 0
                private var actualColour: Int = 0

                override fun describeTo(description: Description?) {
                    if (actualColour != 0) {
                        description?.appendText("Did not match expected colour")
                    }
                }

                override fun matchesSafely(item: View?): Boolean {
                    item!!

                    val resources: Resources = item.context.resources
                    expectedColour = ResourcesCompat.getColor(resources, expectedColourResId, null)

                    actualColour = try {
                        (item.background as ColorDrawable).color
                    } catch (e: Exception) {
                        (item.background as GradientDrawable).color!!.defaultColor
                    }

                    return actualColour == expectedColour
                }
            }

            return matcher
        }
    }

}