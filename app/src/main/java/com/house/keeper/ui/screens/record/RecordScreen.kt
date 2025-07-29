package com.house.keeper.ui.screens.record

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.ui.components.CategorySelector
import com.house.keeper.ui.components.SimpleDatePickerDialog
import com.house.keeper.ui.components.TransactionTypeToggle
import com.house.keeper.ui.theme.HouseKeeperTheme
import com.house.keeper.utils.CurrencyUtils
import com.house.keeper.utils.DateUtils
import com.house.keeper.viewmodel.RecordViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    navController: NavController,
    presetTransactionType: String? = null,
    viewModel: RecordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    // 处理预设交易类型
    LaunchedEffect(presetTransactionType) {
        presetTransactionType?.let { type ->
            when (type) {
                "INCOME" -> viewModel.updateTransactionType(TransactionType.INCOME)
                "EXPENSE" -> viewModel.updateTransactionType(TransactionType.EXPENSE)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部标题栏
        TopAppBar(
            title = {
                Text(
                    text = "记一笔",
                    fontWeight = FontWeight.Bold
                )
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        )

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

                    if (uiState.isLoading) {
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

            // 保存按钮
            Button(
                onClick = {
                    viewModel.saveTransaction(
                        onSuccess = {
                            Toast.makeText(context, "记账成功！", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "保存",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
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
fun RecordScreenPreview() {
    HouseKeeperTheme {
        RecordScreen(navController = rememberNavController())
    }
}
