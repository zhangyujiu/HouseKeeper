package com.house.keeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.model.Category
import com.house.keeper.ui.theme.HouseKeeperTheme

@Composable
fun TransactionFilter(
    selectedTransactionType: TransactionType?,
    selectedCategory: Category?,
    categories: List<Category>,
    hasDateRange: Boolean,
    onTransactionTypeSelected: (TransactionType?) -> Unit,
    onCategorySelected: (Category?) -> Unit,
    onThisMonthClicked: () -> Unit,
    onThisWeekClicked: () -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 筛选按钮行
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 筛选菜单按钮
            FilterChip(
                onClick = { showFilterMenu = !showFilterMenu },
                label = { Text("筛选") },
                selected = showFilterMenu,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "筛选",
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
            
            // 交易类型筛选
            FilterChip(
                onClick = {
                    onTransactionTypeSelected(
                        when (selectedTransactionType) {
                            null -> TransactionType.EXPENSE
                            TransactionType.EXPENSE -> TransactionType.INCOME
                            TransactionType.INCOME -> null
                        }
                    )
                },
                label = {
                    Text(
                        when (selectedTransactionType) {
                            TransactionType.EXPENSE -> "支出"
                            TransactionType.INCOME -> "收入"
                            null -> "全部"
                        }
                    )
                },
                selected = selectedTransactionType != null
            )
            
            // 时间范围筛选
            if (hasDateRange) {
                FilterChip(
                    onClick = { onClearFilters() },
                    label = { Text("已筛选时间") },
                    selected = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "清除",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
            
            // 分类筛选
            if (selectedCategory != null) {
                FilterChip(
                    onClick = { onCategorySelected(null) },
                    label = { Text(selectedCategory.name) },
                    selected = true,
                    leadingIcon = {
                        Text(
                            text = selectedCategory.icon,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "清除",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 清除所有筛选
            if (selectedTransactionType != null || selectedCategory != null || hasDateRange) {
                TextButton(
                    onClick = onClearFilters
                ) {
                    Text("清除全部")
                }
            }
        }
        
        // 展开的筛选菜单
        if (showFilterMenu) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 时间范围快捷选择
                    Column {
                        Text(
                            text = "时间范围",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onThisWeekClicked,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("本周")
                            }
                            
                            OutlinedButton(
                                onClick = onThisMonthClicked,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("本月")
                            }
                        }
                    }
                    
                    // 分类选择
                    if (categories.isNotEmpty()) {
                        Column {
                            Text(
                                text = "分类",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(categories) { category ->
                                    CategoryFilterChip(
                                        category = category,
                                        isSelected = selectedCategory?.id == category.id,
                                        onClick = {
                                            onCategorySelected(
                                                if (selectedCategory?.id == category.id) null else category
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterChip(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = category.icon,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionFilterPreview() {
    HouseKeeperTheme {
        val sampleCategories = listOf(
            Category(1, "餐饮", "🍽️", TransactionType.EXPENSE, "#FF6B6B"),
            Category(2, "交通", "🚗", TransactionType.EXPENSE, "#4ECDC4"),
            Category(3, "购物", "🛒", TransactionType.EXPENSE, "#45B7D1"),
            Category(4, "工资", "💰", TransactionType.INCOME, "#98D8C8")
        )
        
        TransactionFilter(
            selectedTransactionType = TransactionType.EXPENSE,
            selectedCategory = sampleCategories[0],
            categories = sampleCategories,
            hasDateRange = true,
            onTransactionTypeSelected = {},
            onCategorySelected = {},
            onThisMonthClicked = {},
            onThisWeekClicked = {},
            onClearFilters = {}
        )
    }
}
