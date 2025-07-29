package com.house.keeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
fun RecentTransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                    .clip(CircleShape)
                    .background(
                        Color(android.graphics.Color.parseColor(transaction.category.color))
                            .copy(alpha = 0.2f)
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
                Text(
                    text = transaction.category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (transaction.description.isNotEmpty()) {
                    Text(
                        text = transaction.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Text(
                    text = if (DateUtils.isToday(transaction.date)) {
                        "今天 ${DateUtils.formatTime(transaction.date)}"
                    } else {
                        DateUtils.formatDateTime(transaction.date)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // 金额
            Text(
                text = CurrencyUtils.formatAmountWithSymbol(
                    transaction.amount,
                    transaction.type == TransactionType.INCOME
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == TransactionType.INCOME) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecentTransactionItemPreview() {
    HouseKeeperTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RecentTransactionItem(
                transaction = Transaction(
                    id = 1,
                    amount = 25.50,
                    type = TransactionType.EXPENSE,
                    category = Category(
                        id = 1,
                        name = "餐饮",
                        icon = "🍽️",
                        type = TransactionType.EXPENSE,
                        color = "#FF6B6B"
                    ),
                    description = "午餐",
                    date = Date()
                ),
                onClick = {}
            )
            
            RecentTransactionItem(
                transaction = Transaction(
                    id = 2,
                    amount = 5000.0,
                    type = TransactionType.INCOME,
                    category = Category(
                        id = 2,
                        name = "工资",
                        icon = "💰",
                        type = TransactionType.INCOME,
                        color = "#2ECC71"
                    ),
                    description = "",
                    date = Date()
                ),
                onClick = {}
            )
        }
    }
}
