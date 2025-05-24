package com.example.financetracker.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import com.example.financetracker.R
import com.example.financetracker.data.model.Transaction
import com.example.financetracker.data.model.TransactionType

class AddTransactionDialog(
    context: Context,
    private val onAdd: (Transaction) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_transactions)

        val amountInput = findViewById<EditText>(R.id.etAmount)
        val categoryInput = findViewById<EditText>(R.id.etCategory)
        val descInput = findViewById<EditText>(R.id.etDescription)
        val typeSpinner = findViewById<Spinner>(R.id.spinnerType)
        val addButton = findViewById<Button>(R.id.btnAddTransaction)

        // Set spinner options
        typeSpinner.adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            TransactionType.values()
        )

        addButton.setOnClickListener {
            val amount = amountInput.text.toString().toDoubleOrNull()
            val category = categoryInput.text.toString()
            val description = descInput.text.toString()
            val type = typeSpinner.selectedItem as TransactionType

            if (amount != null && category.isNotEmpty()) {
                val transaction = Transaction(
                    category = category,
                    amount = amount,
                    type = type,
                    description = description
                )
                onAdd(transaction)
                dismiss()
            }
        }
    }
}

