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
import com.house.keeper.ui.theme.HouseKeeperTheme

@Composable
fun NumberKeyboard(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 第一行：1, 2, 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NumberKey("1", onNumberClick, Modifier.weight(1f))
            NumberKey("2", onNumberClick, Modifier.weight(1f))
            NumberKey("3", onNumberClick, Modifier.weight(1f))
        }
        
        // 第二行：4, 5, 6
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NumberKey("4", onNumberClick, Modifier.weight(1f))
            NumberKey("5", onNumberClick, Modifier.weight(1f))
            NumberKey("6", onNumberClick, Modifier.weight(1f))
        }
        
        // 第三行：7, 8, 9
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NumberKey("7", onNumberClick, Modifier.weight(1f))
            NumberKey("8", onNumberClick, Modifier.weight(1f))
            NumberKey("9", onNumberClick, Modifier.weight(1f))
        }
        
        // 第四行：., 0, 删除
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NumberKey(".", onNumberClick, Modifier.weight(1f))
            NumberKey("0", onNumberClick, Modifier.weight(1f))
            ActionKey(
                text = "⌫",
                onClick = onDeleteClick,
                modifier = Modifier.weight(1f),
                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        }
        
        // 确认按钮
        ActionKey(
            text = "确认",
            onClick = onConfirmClick,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun NumberKey(
    number: String,
    onNumberClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onNumberClick(number) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ActionKey(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NumberKeyboardPreview() {
    HouseKeeperTheme {
        NumberKeyboard(
            onNumberClick = {},
            onDeleteClick = {},
            onConfirmClick = {}
        )
    }
}
