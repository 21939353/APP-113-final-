package com.example.money.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.money.model.Expense
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableExpenseItem(
    expense: Expense,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> { onDelete(); true }
                SwipeToDismissBoxValue.StartToEnd -> { onEdit(); false }
                else -> false
            }
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.4f }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val (bgColor, icon, text) = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Triple(
                    Color(0xFF2196F3),
                    Icons.Default.Edit,
                    "編輯"
                )
                SwipeToDismissBoxValue.EndToStart -> Triple(
                    Color(0xFFF44336),
                    Icons.Default.Delete,
                    "刪除"
                )
                else -> Triple(Color.Transparent, null, "")
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor)
                    .padding(horizontal = 20.dp),
                contentAlignment = when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                    else -> Alignment.Center
                }
            ) {
                if (icon != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(icon, text, tint = Color.White)
                        Text(text, color = Color.White)
                    }
                }
            }
        },
        content = {
            ExpenseContent(expense = expense)
        }
    )
}

@Composable
private fun ExpenseContent(expense: Expense) {
    val amountColor = if (expense.type == "收入") Color(0xFF4CAF50) else Color(0xFFF44336)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (expense.category) {
                    "飲食" -> Icons.Default.Restaurant
                    "交通" -> Icons.Default.DirectionsBus
                    "購物" -> Icons.Default.ShoppingCart
                    else -> Icons.Default.AttachMoney
                },
                contentDescription = expense.category,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.category,
                    style = MaterialTheme.typography.titleMedium
                )
                if (expense.note.isNotBlank()) {
                    Text(
                        text = expense.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = expense.date.format(DateTimeFormatter.ofPattern("MM/dd")),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "%.2f元".format(expense.amount),
                    color = amountColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}