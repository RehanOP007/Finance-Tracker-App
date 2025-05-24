package com.example.financetracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financetracker.R
import com.example.financetracker.data.model.Transaction
import com.example.financetracker.data.model.TransactionType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter(
    private val onDeleteClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amountTextView: TextView = itemView.findViewById(R.id.tv_amount)
        private val categoryTextView: TextView = itemView.findViewById(R.id.tv_category)
        private val dateTextView: TextView = itemView.findViewById(R.id.tv_date)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_description)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

        fun bind(transaction: Transaction) {
            amountTextView.text = currencyFormat.format(transaction.amount)
            amountTextView.setTextColor(
                itemView.context.getColor(
                    if (transaction.type == TransactionType.INCOME) R.color.green
                    else R.color.red
                )
            )
            categoryTextView.text = transaction.category
            dateTextView.text = dateFormat.format(transaction.date)
            descriptionTextView.text = transaction.description

            deleteButton.setOnClickListener {
                onDeleteClick(transaction)
            }
        }
    }

    private class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}
