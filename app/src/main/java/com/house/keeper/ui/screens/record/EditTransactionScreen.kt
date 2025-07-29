package com.house.keeper.ui.screens.record

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.house.keeper.ui.components.CategorySelector
import com.house.keeper.ui.components.SimpleDatePickerDialog
import com.house.keeper.ui.components.TransactionTypeToggle
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.utils.DateUtils
import com.house.keeper.viewmodel.EditTransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    navController: NavController,
    transactionId: Long,
    viewModel: EditTransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    
    // 加载交易数据
    LaunchedEffect(transactionId) {
        viewModel.loadTransaction(transactionId)
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部应用栏
        TopAppBar(
            title = {
                Text(
                    text = "编辑交易",
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
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.transaction == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "❌",
                        style = MaterialTheme.typography.displayMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "交易记录不存在",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("返回")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 交易类型切换
                TransactionTypeToggle(
                    selectedType = uiState.transactionType,
                    onTypeChanged = viewModel::updateTransactionType
                )
                
                // 金额输入
                OutlinedTextField(
                    value = uiState.amount,
                    onValueChange = { newValue ->
                        // 只允许数字和一个小数点
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                            viewModel.updateAmount(newValue)
                        }
                    },
                    label = { Text("金额") },
                    placeholder = { Text("请输入金额") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    leadingIcon = {
                        Text(
                            text = if (uiState.transactionType.name == "EXPENSE") "-¥" else "+¥",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (uiState.transactionType.name == "EXPENSE") {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                
                // 分类选择
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "选择分类",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                        
                        if (uiState.isLoadingCategories) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            CategorySelector(
                                categories = viewModel.getFilteredCategories(),
                                selectedCategory = uiState.selectedCategory,
                                onCategorySelected = viewModel::updateSelectedCategory
                            )
                        }
                    }
                }
                
                // 日期和备注
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 日期选择
                    OutlinedCard(
                        modifier = Modifier.weight(1f),
                        onClick = { showDatePicker = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "选择日期",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Column {
                                Text(
                                    text = "日期",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = DateUtils.formatDate(uiState.date),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                
                // 备注输入
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::updateDescription,
                    label = { Text("备注（可选）") },
                    placeholder = { Text("添加备注信息...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    singleLine = false
                )
                
                // 操作按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 删除按钮
                    OutlinedButton(
                        onClick = {
                            viewModel.deleteTransaction(
                                onSuccess = {
                                    Toast.makeText(context, "交易已删除", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                },
                                onError = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        enabled = !uiState.isSaving
                    ) {
                        Text("删除")
                    }
                    
                    // 保存按钮
                    Button(
                        onClick = {
                            viewModel.updateTransaction(
                                onSuccess = {
                                    Toast.makeText(context, "更新成功！", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                },
                                onError = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("保存")
                        }
                    }
                }
            }
        }

        // 日期选择器对话框
        if (showDatePicker) {
            SimpleDatePickerDialog(
                selectedDate = uiState.date,
                onDateSelected = { date ->
                    viewModel.updateDate(date)
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditTransactionScreenPreview() {
    HouseKeeperTheme {
        EditTransactionScreen(
            navController = rememberNavController(),
            transactionId = 1L
        )
    }
}
