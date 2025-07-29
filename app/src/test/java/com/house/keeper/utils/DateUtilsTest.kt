package com.house.keeper.utils

import org.junit.Test
import org.junit.Assert.*
import java.util.*

class DateUtilsTest {

    @Test
    fun testFormatDate() {
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        val date = calendar.time
        
        val formatted = DateUtils.formatDate(date)
        assertEquals("2024-01-15", formatted)
    }

    @Test
    fun testGetStartOfMonth() {
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        val date = calendar.time
        
        val startOfMonth = DateUtils.getStartOfMonth(date)
        val startCalendar = Calendar.getInstance()
        startCalendar.time = startOfMonth
        
        assertEquals(2024, startCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, startCalendar.get(Calendar.MONTH))
        assertEquals(1, startCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(0, startCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, startCalendar.get(Calendar.MINUTE))
        assertEquals(0, startCalendar.get(Calendar.SECOND))
    }

    @Test
    fun testGetEndOfMonth() {
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        val date = calendar.time
        
        val endOfMonth = DateUtils.getEndOfMonth(date)
        val endCalendar = Calendar.getInstance()
        endCalendar.time = endOfMonth
        
        assertEquals(2024, endCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, endCalendar.get(Calendar.MONTH))
        assertEquals(31, endCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(23, endCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, endCalendar.get(Calendar.MINUTE))
        assertEquals(59, endCalendar.get(Calendar.SECOND))
    }

    @Test
    fun testIsToday() {
        val today = Date()
        assertTrue(DateUtils.isToday(today))
        
        val yesterday = Date(today.time - 24 * 60 * 60 * 1000)
        assertFalse(DateUtils.isToday(yesterday))
    }
}
