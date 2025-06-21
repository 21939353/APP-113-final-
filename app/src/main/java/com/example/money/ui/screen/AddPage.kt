package com.example.money.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.money.model.Expense
import com.example.money.ui.components.DatePickerDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddPage(
    onAdd: (Expense) -> Unit
) {
    var category by remember { mutableStateOf("飲食") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 日期選擇按鈕
        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("選擇日期: ${selectedDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("選擇類別：", style = MaterialTheme.typography.titleMedium)

        LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
            items(count = 4) { index ->
                val item = listOf("飲食", "交通", "購物", "其他")[index]
                FilterChip(
                    selected = category == item,
                    onClick = { category = item },
                    label = { Text(item) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("金額") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("備註") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val amountValue = amount.toDoubleOrNull()
                if (amountValue != null) {
                    onAdd(
                        Expense(
                            category = category,
                            amount = amountValue,
                            note = note,
                            date = selectedDate
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("儲存")
        }

        // 日期選擇對話框
        if (showDatePicker) {
            DatePickerDialog(
                selectedDate = selectedDate,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}