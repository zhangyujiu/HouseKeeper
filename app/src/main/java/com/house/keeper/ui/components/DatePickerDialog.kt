package com.house.keeper.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.house.keeper.ui.theme.HouseKeeperTheme
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.time
    )
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(Date(millis))
                    }
                    onDismiss()
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            modifier = modifier
        )
    }
}

@Composable
fun SimpleDatePickerDialog(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate
    
    var selectedYear by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("选择日期")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 年份选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("年份:")
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { selectedYear-- }
                        ) {
                            Text("-")
                        }
                        Text(
                            text = selectedYear.toString(),
                            modifier = Modifier.width(60.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        TextButton(
                            onClick = { selectedYear++ }
                        ) {
                            Text("+")
                        }
                    }
                }
                
                // 月份选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("月份:")
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { 
                                if (selectedMonth > 0) selectedMonth--
                            }
                        ) {
                            Text("-")
                        }
                        Text(
                            text = "${selectedMonth + 1}月",
                            modifier = Modifier.width(60.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        TextButton(
                            onClick = { 
                                if (selectedMonth < 11) selectedMonth++
                            }
                        ) {
                            Text("+")
                        }
                    }
                }
                
                // 日期选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("日期:")
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { 
                                if (selectedDay > 1) selectedDay--
                            }
                        ) {
                            Text("-")
                        }
                        Text(
                            text = "${selectedDay}日",
                            modifier = Modifier.width(60.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        TextButton(
                            onClick = { 
                                val maxDay = Calendar.getInstance().apply {
                                    set(selectedYear, selectedMonth, 1)
                                }.getActualMaximum(Calendar.DAY_OF_MONTH)
                                if (selectedDay < maxDay) selectedDay++
                            }
                        ) {
                            Text("+")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newCalendar = Calendar.getInstance()
                    newCalendar.set(selectedYear, selectedMonth, selectedDay)
                    onDateSelected(newCalendar.time)
                    onDismiss()
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleDatePickerDialogPreview() {
    HouseKeeperTheme {
        SimpleDatePickerDialog(
            selectedDate = Date(),
            onDateSelected = {},
            onDismiss = {}
        )
    }
}
