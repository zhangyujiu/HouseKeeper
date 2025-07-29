package com.house.keeper.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.house.keeper.ui.theme.HouseKeeperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部应用栏
        TopAppBar(
            title = {
                Text(
                    text = "设置",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        )

        // 设置列表
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            // 数据管理分组
            item {
                SettingsSectionHeader(title = "数据管理", isFirst = true)
            }

            items(dataManagementItems) { item ->
                SettingsItem(
                    icon = item.icon,
                    title = item.title,
                    subtitle = item.subtitle,
                    onClick = {
                        when (item.title) {
                            "数据导出" -> {
                                Toast.makeText(context, "数据导出功能开发中...", Toast.LENGTH_SHORT).show()
                            }
                            "数据导入" -> {
                                Toast.makeText(context, "数据导入功能开发中...", Toast.LENGTH_SHORT).show()
                            }
                            "数据备份" -> {
                                Toast.makeText(context, "数据备份功能开发中...", Toast.LENGTH_SHORT).show()
                            }
                            "清空数据" -> {
                                Toast.makeText(context, "清空数据功能开发中...", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }

            // 预算管理分组
            item {
                SettingsSectionHeader(title = "预算管理")
            }

            items(budgetManagementItems) { item ->
                SettingsItem(
                    icon = item.icon,
                    title = item.title,
                    subtitle = item.subtitle,
                    onClick = {
                        when (item.title) {
                            "预算设置" -> {
                                Toast.makeText(context, "预算设置功能开发中...", Toast.LENGTH_SHORT).show()
                            }
                            "分类管理" -> {
                                Toast.makeText(context, "分类管理功能开发中...", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }

            // 应用设置分组
            item {
                SettingsSectionHeader(title = "应用设置")
            }

            items(appSettingsItems) { item ->
                SettingsItem(
                    icon = item.icon,
                    title = item.title,
                    subtitle = item.subtitle,
                    onClick = {
                        when (item.title) {
                            "主题设置" -> {
                                Toast.makeText(context, "主题设置功能开发中...", Toast.LENGTH_SHORT).show()
                            }
                            "通知设置" -> {
                                Toast.makeText(context, "通知设置功能开发中...", Toast.LENGTH_SHORT).show()
                            }
                            "关于应用" -> {
                                Toast.makeText(context, "HouseKeeper v1.0.0", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }
        }
    }
}

// 设置项数据类
data class SettingsItemData(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val subtitle: String
)

// 设置项数据
private val dataManagementItems = listOf(
    SettingsItemData(
        icon = Icons.Default.Share,
        title = "数据导出",
        subtitle = "导出交易记录到文件"
    ),
    SettingsItemData(
        icon = Icons.Default.Add,
        title = "数据导入",
        subtitle = "从文件导入交易记录"
    ),
    SettingsItemData(
        icon = Icons.Default.Home,
        title = "数据备份",
        subtitle = "备份数据到云端"
    ),
    SettingsItemData(
        icon = Icons.Default.Delete,
        title = "清空数据",
        subtitle = "清空所有交易记录"
    )
)

private val budgetManagementItems = listOf(
    SettingsItemData(
        icon = Icons.Default.AccountBox,
        title = "预算设置",
        subtitle = "设置月度预算限额"
    ),
    SettingsItemData(
        icon = Icons.Default.List,
        title = "分类管理",
        subtitle = "管理收支分类"
    )
)

private val appSettingsItems = listOf(
    SettingsItemData(
        icon = Icons.Default.Settings,
        title = "主题设置",
        subtitle = "切换应用主题"
    ),
    SettingsItemData(
        icon = Icons.Default.Notifications,
        title = "通知设置",
        subtitle = "管理通知提醒"
    ),
    SettingsItemData(
        icon = Icons.Default.Info,
        title = "关于应用",
        subtitle = "版本信息和帮助"
    )
)

@Composable
private fun SettingsSectionHeader(
    title: String,
    isFirst: Boolean = false
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            start = 0.dp,
            end = 0.dp,
            top = if (isFirst) 0.dp else 16.dp,
            bottom = 8.dp
        )
    )
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "进入",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    HouseKeeperTheme {
        SettingsScreen(navController = rememberNavController())
    }
}
