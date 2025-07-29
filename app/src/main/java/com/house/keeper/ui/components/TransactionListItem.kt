package com.house.keeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.model.Category
import com.house.keeper.data.model.Transaction
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.utils.CurrencyUtils
import com.house.keeper.utils.DateUtils
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListItem(
    transaction: Transaction,
    onEdit: (Transaction) -> Unit,
    onDelete: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEdit(transaction) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 分类图标
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (transaction.type == TransactionType.EXPENSE) {
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        } else {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = transaction.category.icon,
                    fontSize = 20.sp
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 交易信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = transaction.category.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = if (transaction.type == TransactionType.EXPENSE) {
                            "-${CurrencyUtils.formatAmount(transaction.amount)}"
                        } else {
                            "+${CurrencyUtils.formatAmount(transaction.amount)}"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (transaction.type == TransactionType.EXPENSE) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (transaction.description.isNotBlank()) {
                        Text(
                            text = transaction.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    
                    Text(
                        text = DateUtils.formatDateTime(transaction.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 操作按钮
            Row {
                IconButton(
                    onClick = { onEdit(transaction) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "编辑",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                IconButton(
                    onClick = { showDeleteDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
    
    // 删除确认对话框
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("确认删除") },
            text = { Text("确定要删除这笔交易记录吗？此操作无法撤销。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(transaction)
                        showDeleteDialog = false
                    }
                ) {
                    Text("删除", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("取消")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionListItemPreview() {
    HouseKeeperTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TransactionListItem(
                transaction = Transaction(
                    id = 1,
                    amount = 25.50,
                    type = TransactionType.EXPENSE,
                    category = Category(1, "餐饮", "🍽️", TransactionType.EXPENSE, "#FF6B6B"),
                    description = "午餐",
                    date = Date()
                ),
                onEdit = {},
                onDelete = {}
            )
            
            TransactionListItem(
                transaction = Transaction(
                    id = 2,
                    amount = 5000.00,
                    type = TransactionType.INCOME,
                    category = Category(2, "工资", "💰", TransactionType.INCOME, "#98D8C8"),
                    description = "月薪",
                    date = Date()
                ),
                onEdit = {},
                onDelete = {}
            )
        }
    }
}
