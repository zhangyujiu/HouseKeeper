package com.house.keeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.model.Category
import com.house.keeper.ui.theme.HouseKeeperTheme

@Composable
fun CategorySelector(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    if (categories.isEmpty()) {
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
                    text = "📂",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "暂无分类",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 将分类按行分组，每行4个
            categories.chunked(4).forEach { rowCategories ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowCategories.forEach { category ->
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategory?.id == category.id,
                            onClick = { onCategorySelected(category) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // 填充空白位置
                    repeat(4 - rowCategories.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = category.icon,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor,
            textAlign = TextAlign.Center,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategorySelectorPreview() {
    HouseKeeperTheme {
        val sampleCategories = listOf(
            Category(1, "餐饮", "🍽️", TransactionType.EXPENSE, "#FF6B6B"),
            Category(2, "交通", "🚗", TransactionType.EXPENSE, "#4ECDC4"),
            Category(3, "购物", "🛒", TransactionType.EXPENSE, "#45B7D1"),
            Category(4, "娱乐", "🎮", TransactionType.EXPENSE, "#96CEB4"),
            Category(5, "医疗", "🏥", TransactionType.EXPENSE, "#FFEAA7"),
            Category(6, "教育", "📚", TransactionType.EXPENSE, "#DDA0DD"),
            Category(7, "工资", "💰", TransactionType.INCOME, "#98D8C8"),
            Category(8, "奖金", "🎁", TransactionType.INCOME, "#F7DC6F")
        )
        
        CategorySelector(
            categories = sampleCategories.filter { it.type == TransactionType.EXPENSE },
            selectedCategory = sampleCategories[0],
            onCategorySelected = {}
        )
    }
}
