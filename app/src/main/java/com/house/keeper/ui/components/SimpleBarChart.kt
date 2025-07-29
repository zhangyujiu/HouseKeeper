package com.house.keeper.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
        
        // 图表
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barWidth = canvasWidth / (data.size * 2.5f)
            val spacing = barWidth * 0.5f
            
            data.forEachIndexed { index, monthData ->
                val x = index * (barWidth * 2 + spacing) + spacing
                
                if (showIncome && monthData.income > 0) {
                    val incomeHeight = (monthData.income / maxValue * canvasHeight * 0.8).toFloat()
                    drawRect(
                        color = incomeColor,
                        topLeft = Offset(x, canvasHeight - incomeHeight),
                        size = Size(barWidth, incomeHeight)
                    )
                }
                
                if (showExpense && monthData.expense > 0) {
                    val expenseHeight = (monthData.expense / maxValue * canvasHeight * 0.8).toFloat()
                    val expenseX = if (showIncome) x + barWidth + spacing * 0.5f else x
                    drawRect(
                        color = expenseColor,
                        topLeft = Offset(expenseX, canvasHeight - expenseHeight),
                        size = Size(barWidth, expenseHeight)
                    )
                }
            }
        }
        
        // X轴标签
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { monthData ->
                Text(
                    text = monthData.month.takeLast(2), // 只显示月份
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
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
