package com.example.financetracker.data.model

import java.util.Date
import java.util.UUID

data class Budget(
    val id: String = UUID.randomUUID().toString(),
    val category: String,
    val amount: Double,
    val startDate: Date,
    val endDate: Date,
    val isActive: Boolean = true
) 