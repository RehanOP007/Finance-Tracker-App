package com.example.financetracker.data.model

import java.util.Date
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val description: String,
    val date: Date = Date()
)

enum class TransactionType {
    INCOME,
    EXPENSE
} 