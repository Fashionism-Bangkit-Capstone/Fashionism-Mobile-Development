package com.fashionism.fashionismuserapp

import com.fashionism.fashionismuserapp.tools.Helper.shortenText
import org.junit.Assert.assertEquals
import org.junit.Test

class HelperTest {
    @Test
    fun shortenText_keepsTextUpToMaxLength() {
        assertEquals("", shortenText(""))
        assertEquals("a".repeat(20), shortenText("a".repeat(20)))
    }

    @Test
    fun shortenText_cutsLongerTextAndAddsEllipsis() {
        assertEquals("a".repeat(20) + "...", shortenText("a".repeat(21)))
        assertEquals("Fashi...", shortenText("Fashionism", maxLength = 5))
    }
}
