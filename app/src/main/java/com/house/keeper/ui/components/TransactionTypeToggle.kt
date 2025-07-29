package com.house.keeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.ui.theme.HouseKeeperTheme

@Composable
fun TransactionTypeToggle(
    selectedType: TransactionType,
    onTypeChanged: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TransactionTypeTab(
            text = "支出",
            icon = "💸",
            isSelected = selectedType == TransactionType.EXPENSE,
            onClick = { onTypeChanged(TransactionType.EXPENSE) },
            selectedColor = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f)
        )
        
        TransactionTypeTab(
            text = "收入",
            icon = "💰",
            isSelected = selectedType == TransactionType.INCOME,
            onClick = { onTypeChanged(TransactionType.INCOME) },
            selectedColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TransactionTypeTab(
    text: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        selectedColor
    } else {
        androidx.compose.ui.graphics.Color.Transparent
    }
    
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionTypeTogglePreview() {
    HouseKeeperTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TransactionTypeToggle(
                selectedType = TransactionType.EXPENSE,
                onTypeChanged = {}
            )
            
            TransactionTypeToggle(
                selectedType = TransactionType.INCOME,
                onTypeChanged = {}
            )
        }
    }
}
