package com.fashionism.fashionismuserapp

import com.fashionism.fashionismuserapp.tools.Helper.inSampleSizeFor
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

    @Test
    fun inSampleSizeFor_decodesBigPhotosSmallerButNotBelowTheTarget() {
        assertEquals(1, inSampleSizeFor(1000, 800, maxDimension = 1920))
        assertEquals(1, inSampleSizeFor(1920, 1080, maxDimension = 1920))
        assertEquals(2, inSampleSizeFor(4000, 3000, maxDimension = 1920)) // 2000 px long side
        assertEquals(4, inSampleSizeFor(6000, 8160, maxDimension = 1920)) // 50 MP portrait -> 2040 px
    }

    @Test
    fun inSampleSizeFor_unreadableImageKeepsSampleSizeOne() {
        assertEquals(1, inSampleSizeFor(-1, -1, maxDimension = 1920))
    }
}
