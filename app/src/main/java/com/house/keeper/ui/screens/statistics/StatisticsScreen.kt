package com.house.keeper.ui.screens.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.ui.components.CategoryStatistics
import com.house.keeper.ui.components.SimpleBarChart
import com.house.keeper.ui.components.StatisticsOverview
import com.house.keeper.ui.navigation.Screen
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.viewmodel.StatisticsPeriod
import com.house.keeper.viewmodel.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部应用栏
        TopAppBar(
            title = {
                Text(
                    text = "统计分析",
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "设置"
                    )
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 时间段选择
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "时间段",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(StatisticsPeriod.values()) { period ->
                                    FilterChip(
                                        onClick = { viewModel.updatePeriod(period) },
                                        label = { Text(period.displayName) },
                                        selected = uiState.selectedPeriod == period
                                    )
                                }
                            }
                        }
                    }
                }

                // 交易类型筛选
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                onClick = { viewModel.updateTransactionType(null) },
                                label = { Text("全部") },
                                selected = uiState.selectedTransactionType == null
                            )
                        }
                        item {
                            FilterChip(
                                onClick = { viewModel.updateTransactionType(TransactionType.INCOME) },
                                label = { Text("收入") },
                                selected = uiState.selectedTransactionType == TransactionType.INCOME
                            )
                        }
                        item {
                            FilterChip(
                                onClick = { viewModel.updateTransactionType(TransactionType.EXPENSE) },
                                label = { Text("支出") },
                                selected = uiState.selectedTransactionType == TransactionType.EXPENSE
                            )
                        }
                    }
                }

                // 财务概览
                item {
                    StatisticsOverview(
                        totalIncome = uiState.totalIncome,
                        totalExpense = uiState.totalExpense,
                        transactionCount = uiState.transactions.size
                    )
                }

                // 月度趋势图表
                if (uiState.monthlyData.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "月度趋势",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                SimpleBarChart(
                                    data = uiState.monthlyData,
                                    showIncome = uiState.selectedTransactionType != TransactionType.EXPENSE,
                                    showExpense = uiState.selectedTransactionType != TransactionType.INCOME
                                )
                            }
                        }
                    }
                }

                // 分类统计
                if (uiState.categoryData.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "分类统计",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                CategoryStatistics(
                                    categoryData = uiState.categoryData.take(10) // 只显示前10个分类
                                )
                            }
                        }
                    }
                }

                // 空状态
                if (uiState.transactions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "📊",
                                    style = MaterialTheme.typography.displayMedium
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "暂无统计数据",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "开始记账来查看统计分析",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    HouseKeeperTheme {
        StatisticsScreen(navController = rememberNavController())
    }
}
