package com.house.keeper.utils

import org.junit.Test
import org.junit.Assert.*

class CurrencyUtilsTest {

    @Test
    fun testFormatAmount() {
        assertEquals("1,234.56", CurrencyUtils.formatAmount(1234.56))
        assertEquals("0.00", CurrencyUtils.formatAmount(0.0))
        assertEquals("999,999.99", CurrencyUtils.formatAmount(999999.99))
    }

    @Test
    fun testFormatSimpleAmount() {
        assertEquals("1,234.56", CurrencyUtils.formatSimpleAmount(1234.56))
        assertEquals("1,234", CurrencyUtils.formatSimpleAmount(1234.0))
        assertEquals("0", CurrencyUtils.formatSimpleAmount(0.0))
    }

    @Test
    fun testFormatAmountWithSymbol() {
        assertEquals("+1,234.56", CurrencyUtils.formatAmountWithSymbol(1234.56, true))
        assertEquals("-1,234.56", CurrencyUtils.formatAmountWithSymbol(1234.56, false))
    }

    @Test
    fun testParseAmount() {
        assertEquals(1234.56, CurrencyUtils.parseAmount("1234.56")!!, 0.01)
        assertEquals(1234.56, CurrencyUtils.parseAmount("1,234.56")!!, 0.01)
        assertEquals(1234.0, CurrencyUtils.parseAmount("1234")!!, 0.01)
        assertNull(CurrencyUtils.parseAmount("abc"))
        assertNull(CurrencyUtils.parseAmount(""))
    }

    @Test
    fun testIsValidAmount() {
        assertTrue(CurrencyUtils.isValidAmount("1234.56"))
        assertTrue(CurrencyUtils.isValidAmount("1,234.56"))
        assertTrue(CurrencyUtils.isValidAmount("0"))
        assertFalse(CurrencyUtils.isValidAmount("abc"))
        assertFalse(CurrencyUtils.isValidAmount(""))
    }

    @Test
    fun testFormatPercentage() {
        assertEquals("50%", CurrencyUtils.formatPercentage(0.5))
        assertEquals("100%", CurrencyUtils.formatPercentage(1.0))
        assertEquals("0%", CurrencyUtils.formatPercentage(0.0))
    }
}
