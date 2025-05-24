package com.example.financetracker.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.model.CategorySummary
import com.example.financetracker.data.manager.DataManager
import com.example.financetracker.data.model.Budget
import com.example.financetracker.data.model.Transaction
import com.example.financetracker.data.model.TransactionType
import com.example.financetracker.utils.DateUtils
import kotlinx.coroutines.launch
import java.util.Date

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val dataManager: DataManager = DataManager(application)

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    private val _budgets = MutableLiveData<List<Budget>>()
    val budgets: LiveData<List<Budget>> = _budgets

    private val _categorySummary = MutableLiveData<List<CategorySummary>>()
    val categorySummary: LiveData<List<CategorySummary>> = _categorySummary

    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome: LiveData<Double> = _totalIncome

    private val _totalExpense = MutableLiveData<Double>()
    val totalExpense: LiveData<Double> = _totalExpense

    init {
        loadCurrentMonthData()
    }

    // Line chart data
    private val _incomeTrend = MutableLiveData<List<Pair<Date, Double>>>()
    val incomeTrend: LiveData<List<Pair<Date, Double>>> = _incomeTrend

    private val _expenseTrend = MutableLiveData<List<Pair<Date, Double>>>()
    val expenseTrend: LiveData<List<Pair<Date, Double>>> = _expenseTrend

    fun loadTrends() {
        viewModelScope.launch {
            val transactions = dataManager.getTransactions()
            val grouped = transactions.groupBy {
                DateUtils.getStartOfMonth(it.date)
            }

            _incomeTrend.value = grouped.mapValues { (_, list) ->
                list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            }.toList()

            _expenseTrend.value = grouped.mapValues { (_, list) ->
                list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            }.toList()
        }
    }


    private fun loadCurrentMonthData() {
        val startDate = DateUtils.getStartOfMonth()
        val endDate = DateUtils.getEndOfMonth()
        
        viewModelScope.launch {
            _transactions.value = dataManager.getTransactionsByDateRange(startDate, endDate)
            _categorySummary.value = dataManager.getCategorySummary(TransactionType.EXPENSE, startDate, endDate)
            _totalIncome.value = dataManager.getTotalAmountByTypeAndDateRange(TransactionType.INCOME, startDate, endDate)
            _totalExpense.value = dataManager.getTotalAmountByTypeAndDateRange(TransactionType.EXPENSE, startDate, endDate)
            _budgets.value = dataManager.getActiveBudgets()
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            dataManager.saveTransaction(transaction)
            loadCurrentMonthData()
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            dataManager.updateTransaction(transaction)
            loadCurrentMonthData()
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            dataManager.deleteTransaction(transaction)
            loadCurrentMonthData()
        }
    }

    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            dataManager.saveBudget(budget)
            _budgets.value = dataManager.getActiveBudgets() // <- ensures LiveData is updated
            loadCurrentMonthData() // (optional if budgets affect chart/progress)
        }
    }


    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            dataManager.deleteBudget(budget)
            loadCurrentMonthData()
        }
    }

    fun getTotalExpenseForCategory(category: String, startDate: Date, endDate: Date): Double {
        return dataManager.getTransactionsByDateRange(startDate, endDate)
            .filter { it.category == category && it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
    }






    /*fun addMockTransactions() {
        val mockList = listOf(
            Transaction(category = "Groceries", amount = 120.0, type = TransactionType.EXPENSE, description = "Weekly groceries"),
            Transaction(category = "Salary", amount = 3000.0, type = TransactionType.INCOME, description = "Monthly salary"),
            Transaction(category = "Coffee", amount = 45.0, type = TransactionType.EXPENSE, description = "Coffee with friend"),
            Transaction(category = "Freelance", amount = 800.0, type = TransactionType.INCOME, description = "Freelance project")
        )



        viewModelScope.launch {
            for (t in mockList) {
                dataManager.saveTransaction(t)
            }
            loadCurrentMonthData()
        }
    }*/

} 