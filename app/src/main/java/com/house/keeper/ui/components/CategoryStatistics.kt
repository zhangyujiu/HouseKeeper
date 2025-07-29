package com.house.keeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.model.Category
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.utils.CurrencyUtils
import com.house.keeper.viewmodel.CategoryData

@Composable
fun CategoryStatistics(
    categoryData: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    if (categoryData.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📊",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "暂无分类数据",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categoryData.forEach { data ->
            CategoryStatisticsItem(categoryData = data)
        }
    }
}

@Composable
private fun CategoryStatisticsItem(
    categoryData: CategoryData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 分类信息行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 分类图标
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (categoryData.category.type == TransactionType.EXPENSE) {
                                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = categoryData.category.icon,
                            fontSize = 18.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = categoryData.category.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${categoryData.transactionCount}笔交易",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = CurrencyUtils.formatAmount(categoryData.amount),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (categoryData.category.type == TransactionType.EXPENSE) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                    Text(
                        text = "${String.format("%.1f", categoryData.percentage)}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 进度条
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = (categoryData.percentage / 100).toFloat().coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (categoryData.category.type == TransactionType.EXPENSE) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryStatisticsPreview() {
    HouseKeeperTheme {
        val sampleData = listOf(
            CategoryData(
                category = Category(1, "餐饮", "🍽️", TransactionType.EXPENSE, "#FF6B6B"),
                amount = 1500.0,
                transactionCount = 25,
                percentage = 35.0
            ),
            CategoryData(
                category = Category(2, "交通", "🚗", TransactionType.EXPENSE, "#4ECDC4"),
                amount = 800.0,
                transactionCount = 15,
                percentage = 20.0
            ),
            CategoryData(
                category = Category(3, "购物", "🛒", TransactionType.EXPENSE, "#45B7D1"),
                amount = 600.0,
                transactionCount = 10,
                percentage = 15.0
            ),
            CategoryData(
                category = Category(4, "工资", "💰", TransactionType.INCOME, "#98D8C8"),
                amount = 5000.0,
                transactionCount = 1,
                percentage = 100.0
            )
        )
        
        CategoryStatistics(
            categoryData = sampleData,
            modifier = Modifier.padding(16.dp)
        )
    }
}
