package com.house.keeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.utils.DateUtils
import com.house.keeper.viewmodel.DailyData
import java.util.Calendar
import java.util.Date

@Composable
fun MonthCalendar(
    currentMonth: Date,
    dailyData: Map<Int, DailyData>,
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    val calendar = Calendar.getInstance()
    calendar.time = currentMonth
    
    // 获取当月第一天是星期几（0=周日，1=周一...）
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    
    // 获取当月天数
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    
    // 创建日历数据
    val calendarDays = mutableListOf<CalendarDay>()
    
    // 添加上个月的空白天数
    repeat(firstDayOfWeek) {
        calendarDays.add(CalendarDay.Empty)
    }
    
    // 添加当月的天数
    for (day in 1..daysInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val date = calendar.time
        val isSelected = selectedDate?.let { DateUtils.formatDate(it) == DateUtils.formatDate(date) } ?: false
        val isToday = DateUtils.isToday(date)
        
        calendarDays.add(
            CalendarDay.Day(
                date = date,
                day = day,
                dailyData = dailyData[day],
                isSelected = isSelected,
                isToday = isToday
            )
        )
    }
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 星期标题
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val weekDays = listOf("日", "一", "二", "三", "四", "五", "六")
            weekDays.forEach { weekDay ->
                Text(
                    text = weekDay,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 日历网格 - 使用固定高度避免嵌套滚动问题
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp), // 固定高度：6行 × 40dp/行
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            userScrollEnabled = false // 禁用滚动，因为内容固定
        ) {
            items(calendarDays) { calendarDay ->
                when (calendarDay) {
                    is CalendarDay.Empty -> {
                        Spacer(modifier = Modifier.aspectRatio(1f))
                    }
                    is CalendarDay.Day -> {
                        CalendarDayItem(
                            calendarDay = calendarDay,
                            onClick = { onDateSelected(calendarDay.date) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayItem(
    calendarDay: CalendarDay.Day,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        calendarDay.isSelected -> MaterialTheme.colorScheme.primary
        calendarDay.isToday -> MaterialTheme.colorScheme.primaryContainer
        calendarDay.dailyData?.hasTransactions == true -> MaterialTheme.colorScheme.surfaceVariant
        else -> Color.Transparent
    }
    
    val contentColor = when {
        calendarDay.isSelected -> MaterialTheme.colorScheme.onPrimary
        calendarDay.isToday -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    val borderColor = when {
        calendarDay.isToday && !calendarDay.isSelected -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }
    
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = if (borderColor != Color.Transparent) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = calendarDay.day.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (calendarDay.isSelected || calendarDay.isToday) FontWeight.Bold else FontWeight.Normal,
                color = contentColor
            )
            
            // 显示交易指示器
            calendarDay.dailyData?.let { data ->
                if (data.hasTransactions) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        if (data.income > 0) {
                            Box(
                                modifier = Modifier
                                    .size(3.dp)
                                    .background(
                                        if (calendarDay.isSelected) {
                                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        },
                                        RoundedCornerShape(1.5.dp)
                                    )
                            )
                        }
                        if (data.expense > 0) {
                            Box(
                                modifier = Modifier
                                    .size(3.dp)
                                    .background(
                                        if (calendarDay.isSelected) {
                                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                        } else {
                                            MaterialTheme.colorScheme.error
                                        },
                                        RoundedCornerShape(1.5.dp)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class CalendarDay {
    object Empty : CalendarDay()
    data class Day(
        val date: Date,
        val day: Int,
        val dailyData: DailyData?,
        val isSelected: Boolean,
        val isToday: Boolean
    ) : CalendarDay()
}

@Preview(showBackground = true)
@Composable
fun MonthCalendarPreview() {
    HouseKeeperTheme {
        val sampleDailyData = mapOf(
            5 to DailyData(5, 0.0, 150.0, 2),
            12 to DailyData(12, 5000.0, 0.0, 1),
            18 to DailyData(18, 0.0, 280.0, 3),
            25 to DailyData(25, 0.0, 95.0, 1)
        )
        
        MonthCalendar(
            currentMonth = Date(),
            dailyData = sampleDailyData,
            selectedDate = Date(),
            onDateSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
