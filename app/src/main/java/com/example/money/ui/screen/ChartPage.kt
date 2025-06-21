// 替换为以下代码
package com.example.money.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.money.model.Expense

@Composable
fun ChartPage(
    expenses: List<Expense>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "支出统计图表",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 简单分类统计
        val categories = expenses
            .filter { it.type == "支出" }
            .groupBy { it.category }
            .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }

        Column(modifier = Modifier.fillMaxWidth()) {
            categories.forEach { (category, amount) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(category)
                    Text("${"%.2f".format(amount)}元")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}