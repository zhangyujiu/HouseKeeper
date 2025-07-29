package com.house.keeper.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.house.keeper.HouseKeeperApplication
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.model.Category
import com.house.keeper.data.model.Transaction

import com.house.keeper.ui.components.MonthlyOverviewCard
import com.house.keeper.ui.components.QuickActionButton
import com.house.keeper.ui.components.RecentTransactionItem
import com.house.keeper.ui.navigation.Screen
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.utils.CurrencyUtils
import com.house.keeper.viewmodel.HomeViewModel
import java.util.Date

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // 月度概览卡片
        item {
            MonthlyOverviewCard(
                monthlyIncome = uiState.monthlyIncome,
                monthlyExpense = uiState.monthlyExpense,
                balance = uiState.balance
            )
        }
        
        // 快速操作按钮
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    text = "收入",
                    icon = "💰",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate(Screen.Record.route)
                    }
                )
                
                QuickActionButton(
                    text = "支出",
                    icon = "💸",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate(Screen.Record.route)
                    }
                )
            }
        }
        
        // 最近交易标题
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "最近交易",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                TextButton(
                    onClick = {
                        navController.navigate(Screen.Transactions.route)
                    }
                ) {
                    Text("查看全部")
                }
            }
        }
        
        // 最近交易列表
        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (uiState.recentTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📝",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "暂无交易记录",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "点击上方按钮开始记账",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(uiState.recentTransactions) { transaction ->
                RecentTransactionItem(
                    transaction = transaction,
                    onClick = {
                        // TODO: 导航到交易详情页面
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HouseKeeperTheme {
        HomeScreen(navController = rememberNavController())
    }
}
