package com.example.financetracker.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financetracker.R
import com.example.financetracker.data.model.Budget
import java.text.SimpleDateFormat
import java.util.*


class BudgetAdapter(
    private val onDeleteClick: (Budget) -> Unit,
    private val getBalance: () -> Double
) : ListAdapter<Budget, BudgetAdapter.BudgetViewHolder>(BudgetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_budget, parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTotalBudget: TextView = itemView.findViewById(R.id.tvTotalBudget)
        private val budgetProgress: ProgressBar = itemView.findViewById(R.id.budgetProgress)
        private val tvBudgetStatus: TextView = itemView.findViewById(R.id.tvBudgetStatus)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteBudget)
        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        @SuppressLint("SetTextI18n")
        fun bind(budget: Budget) {

            val balance = getBalance() // Add this lambda in constructor
            val remaining = (budget.amount - balance).coerceAtLeast(0.0)
            val usagePercent = if (budget.amount > 0) ((balance / budget.amount) * 100).toInt() else 0


            tvTotalBudget.text = "Target budget for ${budget.category}: $${"%.2f".format(budget.amount)}"
            tvBudgetStatus.text = "Remaining: $${"%.2f".format(remaining)}"
            budgetProgress.progress = usagePercent

            btnDelete.setOnClickListener {
                onDeleteClick(budget)
            }

        }
    }

    class BudgetDiffCallback : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem == newItem
        }
    }
}
