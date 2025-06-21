package com.example.money.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.money.model.RecordType

@Composable
fun ExpenseItem(
    amount: Double,
    category: String,
    type: RecordType  // 直接使用 RecordType
) {
    Text(
        text = if (type == RecordType.EXPENSE) "支出: $amount" else "收入: $amount",
        color = if (type == RecordType.EXPENSE) Color.Red else Color.Green
    )
}