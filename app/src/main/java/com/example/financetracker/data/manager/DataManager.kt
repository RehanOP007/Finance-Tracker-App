package com.example.financetracker.data.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.financetracker.data.model.Budget
import com.example.financetracker.data.model.Transaction
import com.example.financetracker.data.model.TransactionType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import com.example.financetracker.data.model.CategorySummary


class DataManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("finance_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Transaction operations
    fun saveTransaction(transaction: Transaction) {
        val transactions = getTransactions().toMutableList()
        transactions.add(transaction)
        saveTransactions(transactions)
    }

    fun updateTransaction(transaction: Transaction) {
        val transactions = getTransactions().toMutableList()
        val index = transactions.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            transactions[index] = transaction
            saveTransactions(transactions)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        val transactions = getTransactions().toMutableList()
        transactions.removeIf { it.id == transaction.id }
        saveTransactions(transactions)
    }

    fun getTransactions(): List<Transaction> {
        val json = sharedPreferences.getString(KEY_TRANSACTIONS, "[]")
        val type = object : TypeToken<List<Transaction>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getTransactionsByType(type: TransactionType): List<Transaction> {
        return getTransactions().filter { it.type == type }
    }

    fun getTransactionsByDateRange(startDate: Date, endDate: Date): List<Transaction> {
        return getTransactions().filter { 
            it.date.time in startDate.time..endDate.time 
        }
    }

    fun getTotalAmountByTypeAndDateRange(type: TransactionType, startDate: Date, endDate: Date): Double {
        return getTransactionsByDateRange(startDate, endDate)
            .filter { it.type == type }
            .sumOf { it.amount }
    }

    fun getCategorySummary(type: TransactionType, startDate: Date, endDate: Date): List<CategorySummary> {
        return getTransactionsByDateRange(startDate, endDate)
            .filter { it.type == type }
            .groupBy { it.category }
            .map { (category, transactions) ->
                CategorySummary(category, transactions.sumOf { it.amount })
            }
    }

    // Budget operations
    fun saveBudget(budget: Budget) {
        val budgets = getBudgets().toMutableList()
        budgets.add(budget)
        saveBudgets(budgets)
    }

    fun deleteBudget(budget: Budget) {
        val budgets = getBudgets().toMutableList()
        budgets.removeIf { it.id == budget.id }
        saveBudgets(budgets)
    }

    fun getBudgets(): List<Budget> {
        val json = sharedPreferences.getString(KEY_BUDGETS, "[]")
        val type = object : TypeToken<List<Budget>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getActiveBudgets(): List<Budget> {
        return getBudgets().filter { it.isActive }
    }

    fun getActiveBudgetByCategory(category: String): Budget? {
        return getActiveBudgets().find { it.category == category }
    }

    fun getActiveBudgetsForDate(date: Date): List<Budget> {
        return getActiveBudgets().filter { 
            date.time in it.startDate.time..it.endDate.time 
        }
    }

    private fun saveTransactions(transactions: List<Transaction>) {
        val json = gson.toJson(transactions)
        sharedPreferences.edit().putString(KEY_TRANSACTIONS, json).apply()
    }

    private fun saveBudgets(budgets: List<Budget>) {
        val json = gson.toJson(budgets)
        sharedPreferences.edit().putString(KEY_BUDGETS, json).apply()
    }

    companion object {
        private const val KEY_TRANSACTIONS = "transactions"
        private const val KEY_BUDGETS = "budgets"
    }
}

data class CategorySummary(
    val category: String,
    val total: Double
) 