package com.house.keeper.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.viewmodel.MonthlyData
import kotlin.math.max

@Composable
fun SimpleBarChart(
    data: List<MonthlyData>,
    modifier: Modifier = Modifier,
    showIncome: Boolean = true,
    showExpense: Boolean = true
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无数据",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }
    
    val maxValue = data.maxOfOrNull { max(it.income, it.expense) } ?: 1.0
    val incomeColor = MaterialTheme.colorScheme.primary
    val expenseColor = MaterialTheme.colorScheme.error
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 图例
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (showIncome) {
                LegendItem(
                    color = incomeColor,
                    label = "收入"
                )
                Spacer(modifier = Modifier.width(24.dp))
            }
            if (showExpense) {
                LegendItem(
                    color = expenseColor,
                    label = "支出"
                )
            }
        }

        // 可滚动的图表容器
        val scrollState = rememberScrollState()
        val minBarWidth = 60.dp // 最小柱状图宽度
        val barSpacing = 16.dp // 柱状图间距
        val totalWidth = (minBarWidth * data.size * (if (showIncome && showExpense) 2 else 1) +
                         barSpacing * (data.size + 1)).coerceAtLeast(300.dp)

        Column {
            // 图表
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                Canvas(
                    modifier = Modifier
                        .width(totalWidth)
                        .height(220.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height - 40.dp.toPx() // 为X轴标签留空间
                    val actualBarWidth = (canvasWidth - barSpacing.toPx() * (data.size + 1)) /
                                       (data.size * (if (showIncome && showExpense) 2.2f else 1.1f))
                    val spacing = barSpacing.toPx()

                    data.forEachIndexed { index, monthData ->
                        val groupX = spacing + index * (actualBarWidth * (if (showIncome && showExpense) 2.2f else 1.1f) + spacing)

                        if (showIncome && monthData.income > 0) {
                            val incomeHeight = (monthData.income / maxValue * canvasHeight * 0.85).toFloat()
                            drawRect(
                                color = incomeColor,
                                topLeft = Offset(groupX, canvasHeight - incomeHeight),
                                size = Size(actualBarWidth, incomeHeight)
                            )
                        }

                        if (showExpense && monthData.expense > 0) {
                            val expenseHeight = (monthData.expense / maxValue * canvasHeight * 0.85).toFloat()
                            val expenseX = if (showIncome) groupX + actualBarWidth + spacing * 0.3f else groupX
                            drawRect(
                                color = expenseColor,
                                topLeft = Offset(expenseX, canvasHeight - expenseHeight),
                                size = Size(actualBarWidth, expenseHeight)
                            )
                        }
                    }
                }
            }

            // X轴标签（也需要滚动）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier
                        .width(totalWidth)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    data.forEach { monthData ->
                        Text(
                            text = formatMonthLabel(monthData.month),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

private fun formatMonthLabel(monthString: String): String {
    return try {
        // 假设格式是 "2024-01" 或 "2024-1"
        val parts = monthString.split("-")
        if (parts.size >= 2) {
            val month = parts[1].toIntOrNull()
            when (month) {
                1 -> "1月"
                2 -> "2月"
                3 -> "3月"
                4 -> "4月"
                5 -> "5月"
                6 -> "6月"
                7 -> "7月"
                8 -> "8月"
                9 -> "9月"
                10 -> "10月"
                11 -> "11月"
                12 -> "12月"
                else -> monthString.takeLast(2)
            }
        } else {
            monthString.takeLast(2)
        }
    } catch (e: Exception) {
        monthString.takeLast(2)
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleBarChartPreview() {
    HouseKeeperTheme {
        val sampleData = listOf(
            MonthlyData("2024-01", 5000.0, 3000.0),
            MonthlyData("2024-02", 5500.0, 3200.0),
            MonthlyData("2024-03", 5200.0, 2800.0),
            MonthlyData("2024-04", 5800.0, 3500.0),
            MonthlyData("2024-05", 6000.0, 3800.0),
            MonthlyData("2024-06", 5600.0, 3300.0)
        )
        
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SimpleBarChart(
                data = sampleData,
                showIncome = true,
                showExpense = true
            )
        }
    }
}
