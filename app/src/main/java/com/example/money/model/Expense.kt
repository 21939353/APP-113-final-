package com.example.money.model

import java.time.LocalDate
import java.util.*

data class Expense(
    val id: Int = UUID.randomUUID().hashCode(),
    val category: String,
    val amount: Double,
    val note: String = "",
    val date: LocalDate,
    val type: String = "支出"
)