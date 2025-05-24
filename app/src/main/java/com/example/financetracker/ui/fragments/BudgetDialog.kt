package com.example.financetracker.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import com.example.financetracker.R
import com.example.financetracker.data.model.Budget
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.widget.addTextChangedListener


class BudgetDialog(
    context: Context,
    private val onAdd: (Budget) -> Unit
) : Dialog(context) {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_budget)

        val etCategory = findViewById<EditText>(R.id.etCategory)
        val tvTargetBudgetTitle = findViewById<TextView>(R.id.tvTargetBudgetTitle)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val tvStartDate = findViewById<TextView>(R.id.tvStartDate)
        val tvEndDate = findViewById<TextView>(R.id.tvEndDate)
        val btnAdd = findViewById<Button>(R.id.btnAddBudget)

        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()

        etCategory.addTextChangedListener {
            val category = it.toString()
            if (category.isNotEmpty()) {
                tvTargetBudgetTitle.text = "Target Budget for '$category':"
            } else {
                tvTargetBudgetTitle.text = "Target Budget:"
            }
        }

        fun updateDateViews() {
            tvStartDate.text = dateFormat.format(startCalendar.time)
            tvEndDate.text = dateFormat.format(endCalendar.time)
        }

        updateDateViews()

        tvStartDate.setOnClickListener {
            DatePickerDialog(context, { _, y, m, d ->
                startCalendar.set(y, m, d)
                updateDateViews()
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        tvEndDate.setOnClickListener {
            DatePickerDialog(context, { _, y, m, d ->
                endCalendar.set(y, m, d)
                updateDateViews()
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnAdd.setOnClickListener {
            val category = etCategory.text.toString()
            val amount = etAmount.text.toString().toDoubleOrNull()

            if (category.isNotEmpty() && amount != null) {
                val budget = Budget(
                    category = category,
                    amount = amount,
                    startDate = startCalendar.time,
                    endDate = endCalendar.time
                )
                onAdd(budget)
                dismiss()
            } else {
                Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}