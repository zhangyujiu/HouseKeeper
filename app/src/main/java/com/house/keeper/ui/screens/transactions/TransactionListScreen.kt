package com.house.keeper.ui.screens.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import com.house.keeper.ui.components.SwipeableTransactionItem
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.viewmodel.TransactionListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    viewModel: TransactionListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部应用栏
        TopAppBar(
            title = {
                if (showSearchBar) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            viewModel.updateSearchQuery(it)
                        },
                        placeholder = { Text("搜索交易记录...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = "交易记录",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            actions = {
                // 筛选按钮
                Box {
                    IconButton(
                        onClick = { showFilterMenu = !showFilterMenu }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "筛选"
                        )
                    }

                    // 筛选下拉菜单
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        // 交易类型筛选
                        Text(
                            text = "交易类型",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        DropdownMenuItem(
                            text = { Text("全部") },
                            onClick = {
                                viewModel.updateTransactionTypeFilter(null)
                                showFilterMenu = false
                            },
                            leadingIcon = if (uiState.selectedTransactionType == null) {
                                { Icon(Icons.Default.Check, contentDescription = null) }
                            } else null
                        )

                        DropdownMenuItem(
                            text = { Text("收入") },
                            onClick = {
                                viewModel.updateTransactionTypeFilter(TransactionType.INCOME)
                                showFilterMenu = false
                            },
                            leadingIcon = if (uiState.selectedTransactionType == TransactionType.INCOME) {
                                { Icon(Icons.Default.Check, contentDescription = null) }
                            } else null
                        )

                        DropdownMenuItem(
                            text = { Text("支出") },
                            onClick = {
                                viewModel.updateTransactionTypeFilter(TransactionType.EXPENSE)
                                showFilterMenu = false
                            },
                            leadingIcon = if (uiState.selectedTransactionType == TransactionType.EXPENSE) {
                                { Icon(Icons.Default.Check, contentDescription = null) }
                            } else null
                        )

                        Divider()

                        // 时间筛选
                        Text(
                            text = "时间范围",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        DropdownMenuItem(
                            text = { Text("本周") },
                            onClick = {
                                viewModel.setDateRangeToThisWeek()
                                showFilterMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("本月") },
                            onClick = {
                                viewModel.setDateRangeToThisMonth()
                                showFilterMenu = false
                            }
                        )

                        if (uiState.selectedTransactionType != null || uiState.dateRange != null) {
                            Divider()
                            DropdownMenuItem(
                                text = { Text("清除筛选") },
                                onClick = {
                                    viewModel.clearAllFilters()
                                    showFilterMenu = false
                                }
                            )
                        }
                    }
                }

                // 搜索按钮
                IconButton(
                    onClick = {
                        showSearchBar = !showSearchBar
                        if (!showSearchBar) {
                            searchQuery = ""
                            viewModel.updateSearchQuery("")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = if (showSearchBar) "关闭搜索" else "搜索"
                    )
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        )

        // 交易列表
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📝",
                        style = MaterialTheme.typography.displayMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = if (uiState.searchQuery.isNotBlank() || 
                                  uiState.selectedTransactionType != null || 
                                  uiState.selectedCategory != null || 
                                  uiState.dateRange != null) {
                            "没有找到符合条件的交易记录"
                        } else {
                            "暂无交易记录"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (uiState.searchQuery.isBlank() && 
                        uiState.selectedTransactionType == null && 
                        uiState.selectedCategory == null && 
                        uiState.dateRange == null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "开始记账来查看您的交易记录",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uiState.transactions,
                    key = { it.id }
                ) { transaction ->
                    SwipeableTransactionItem(
                        transaction = transaction,
                        onEdit = {
                            navController.navigate("edit_transaction/${transaction.id}")
                        },
                        onDelete = {
                            viewModel.deleteTransaction(it)
                        }
                    )
                }
            }
        }
        
        // 统计信息
        if (uiState.transactions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val totalIncome = uiState.transactions
                        .filter { it.type.name == "INCOME" }
                        .sumOf { it.amount }
                    
                    val totalExpense = uiState.transactions
                        .filter { it.type.name == "EXPENSE" }
                        .sumOf { it.amount }
                    
                    // 收入统计
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "收入",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "+${String.format("%.2f", totalIncome)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // 支出统计
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "支出",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "-${String.format("%.2f", totalExpense)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    // 净收入统计
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "净收入",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        val netIncome = totalIncome - totalExpense
                        Text(
                            text = String.format("%.2f", netIncome),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (netIncome >= 0) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionListScreenPreview() {
    HouseKeeperTheme {
        TransactionListScreen(navController = rememberNavController())
    }
}
