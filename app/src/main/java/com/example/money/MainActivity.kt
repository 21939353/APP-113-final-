package com.example.money

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.money.model.Expense
import com.example.money.ui.screen.*
import com.example.money.ui.theme.MoneyTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyTheme {
                var currentPage by remember { mutableStateOf(0) }
                var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }
                var selectedDate by remember { mutableStateOf(LocalDate.now()) }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentPage == 0,
                                onClick = { currentPage = 0 },
                                icon = { Icon(Icons.Filled.Home, "首頁") },
                                label = { Text("首頁") }
                            )
                            NavigationBarItem(
                                selected = currentPage == 1,
                                onClick = { currentPage = 1 },
                                icon = { Icon(Icons.Filled.Add, "新增") },
                                label = { Text("新增") }
                            )
                            NavigationBarItem(
                                selected = currentPage == 2,
                                onClick = { currentPage = 2 },
                                icon = { Icon(Icons.AutoMirrored.Filled.List, "總覽") },
                                label = { Text("總覽") }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentPage) {
                            0 -> HomePage(
                                expenses = expenses,
                                selectedDate = selectedDate,
                                onDateChange = { selectedDate = it },
                                onEdit = { updated ->
                                    expenses = expenses.map { if (it.id == updated.id) updated else it }
                                },
                                onDelete = { deleted ->
                                    expenses = expenses.filterNot { it.id == deleted.id }
                                }
                            )
                            1 -> AddPage(
                                onAdd = { newExpense ->
                                    expenses = expenses + newExpense
                                    currentPage = 0
                                }
                            )
                            2 -> OverviewPage(
                                expenses = expenses,
                                onEdit = { updated ->
                                    expenses = expenses.map { if (it.id == updated.id) updated else it }
                                },
                                onDelete = { deleted ->
                                    expenses = expenses.filterNot { it.id == deleted.id }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}