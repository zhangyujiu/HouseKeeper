package com.house.keeper.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    
    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
    
    fun formatTime(date: Date): String {
        return timeFormat.format(date)
    }
    
    fun formatDateTime(date: Date): String {
        return dateTimeFormat.format(date)
    }
    
    fun formatMonth(date: Date): String {
        return monthFormat.format(date)
    }
    
    fun formatYear(date: Date): String {
        return yearFormat.format(date)
    }
    
    fun getStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    fun getEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
    
    fun getStartOfMonth(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    fun getEndOfMonth(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
    
    fun getStartOfYear(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    fun getEndOfYear(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 31)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
    
    fun isToday(date: Date): Boolean {
        val today = Date()
        return formatDate(date) == formatDate(today)
    }
    
    fun isThisMonth(date: Date): Boolean {
        val today = Date()
        return formatMonth(date) == formatMonth(today)
    }
    
    fun isThisYear(date: Date): Boolean {
        val today = Date()
        return formatYear(date) == formatYear(today)
    }

    fun formatMonthYear(date: Date): String {
        val formatter = SimpleDateFormat("yyyy年MM月", Locale.getDefault())
        return formatter.format(date)
    }
}
