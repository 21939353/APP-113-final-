package com.example.money.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.money.model.Expense
import com.example.money.ui.components.SwipeableExpenseItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    expenses: List<Expense>,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onEdit: (Expense) -> Unit,
    onDelete: (Expense) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("選取日期：${selectedDate.format(DateTimeFormatter.ISO_DATE)}")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                val newDate = Instant.ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                onDateChange(newDate)
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("確認")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("取消")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        val filtered = expenses.filter { it.date == selectedDate }

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("當日尚無記錄", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filtered, key = { it.id }) { expense ->
                    SwipeableExpenseItem(
                        expense = expense,
                        onEdit = { onEdit(expense) },
                        onDelete = { onDelete(expense) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}