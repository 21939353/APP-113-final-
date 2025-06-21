// 替换为以下代码
package com.example.money.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.money.model.Expense
import com.example.money.ui.components.SwipeableExpenseItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun OverviewPage(
    expenses: List<Expense>,
    onEdit: (Expense) -> Unit,
    onDelete: (Expense) -> Unit
) {
    var selectedMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var showSortMenu by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf("日期") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { selectedMonth = selectedMonth.minusMonths(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "上个月")
            }

            Text(
                text = selectedMonth.format(DateTimeFormatter.ofPattern("yyyy年MM月")),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = { selectedMonth = selectedMonth.plusMonths(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "下个月")
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("排序方式: $sortOption", modifier = Modifier.weight(1f))

            IconButton(onClick = { showSortMenu = true }) {
                Icon(Icons.AutoMirrored.Filled.Sort, "排序")
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("日期") },
                        onClick = { sortOption = "日期"; showSortMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("金額") },
                        onClick = { sortOption = "金額"; showSortMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("類別") },
                        onClick = { sortOption = "類別"; showSortMenu = false }
                    )
                }
            }
        }

        val filteredExpenses = expenses
            .filter { it.date.month == selectedMonth.month && it.date.year == selectedMonth.year }
            .sortedWith(
                when (sortOption) {
                    "金額" -> compareByDescending { it.amount }
                    "類別" -> compareBy { it.category }
                    else -> compareBy { it.date }
                }
            )

        if (filteredExpenses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("本月尚無紀錄")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredExpenses, key = { it.id }) { expense ->
                    SwipeableExpenseItem(
                        expense = expense,
                        onEdit = { onEdit(expense) },
                        onDelete = { onDelete(expense) }
                    )
                }
            }
        }
    }
}