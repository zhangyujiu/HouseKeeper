package com.house.keeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.utils.CurrencyUtils

@Composable
fun StatisticsOverview(
    totalIncome: Double,
    totalExpense: Double,
    transactionCount: Int,
    modifier: Modifier = Modifier
) {
    val netIncome = totalIncome - totalExpense
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 标题
            Text(
                text = "财务概览",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            // 主要指标
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 收入
                StatisticsCard(
                    title = "总收入",
                    amount = totalIncome,
                    color = MaterialTheme.colorScheme.primary,
                    icon = "💰",
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 支出
                StatisticsCard(
                    title = "总支出",
                    amount = totalExpense,
                    color = MaterialTheme.colorScheme.error,
                    icon = "💸",
                    modifier = Modifier.weight(1f)
                )
            }
            
            // 净收入和交易笔数
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 净收入
                StatisticsCard(
                    title = "净收入",
                    amount = netIncome,
                    color = if (netIncome >= 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    icon = if (netIncome >= 0) "📈" else "📉",
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 交易笔数
                StatisticsInfoCard(
                    title = "交易笔数",
                    value = "${transactionCount}笔",
                    icon = "📝",
                    modifier = Modifier.weight(1f)
                )
            }
            
            // 储蓄率
            if (totalIncome > 0) {
                val savingsRate = (netIncome / totalIncome) * 100
                
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "储蓄率",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${String.format("%.1f", savingsRate)}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (savingsRate >= 0) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // 储蓄率进度条
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(
                                    fraction = (savingsRate / 100).toFloat().coerceIn(0f, 1f)
                                )
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (savingsRate >= 0) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.error
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticsCard(
    title: String,
    amount: Double,
    color: androidx.compose.ui.graphics.Color,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 24.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = CurrencyUtils.formatAmount(amount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun StatisticsInfoCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 24.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsOverviewPreview() {
    HouseKeeperTheme {
        StatisticsOverview(
            totalIncome = 8000.0,
            totalExpense = 5500.0,
            transactionCount = 45,
            modifier = Modifier.padding(16.dp)
        )
    }
}
