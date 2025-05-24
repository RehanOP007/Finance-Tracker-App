package com.example.financetracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetracker.databinding.FragmentBudgetBinding
import com.example.financetracker.ui.fragments.BudgetDialog
import com.example.financetracker.ui.viewmodel.FinanceViewModel
import com.example.financetracker.ui.adapters.BudgetAdapter
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.example.financetracker.R
import kotlinx.coroutines.launch

class BudgetFragment : Fragment() {

    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FinanceViewModel by viewModels()
    private lateinit var budgetAdapter: BudgetAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up FAB to open BudgetDialog
        binding.fabAddBudget.setOnClickListener {
            BudgetDialog(requireContext()) { newBudget ->
                viewModel.addBudget(newBudget)
            }.show()
        }

        budgetAdapter = BudgetAdapter(
            onDeleteClick = { budgetToDelete -> viewModel.deleteBudget(budgetToDelete) },
            getBalance = {
                viewModel.totalIncome.value ?: (0.0 - (viewModel.totalExpense.value ?: 0.0))
            }

        )

        binding.rvBudget.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBudget.adapter = budgetAdapter

        // Observe and update the budget list
        viewModel.budgets.observe(viewLifecycleOwner) { budgets ->
            budgetAdapter.submitList(budgets)
            budgetAdapter.notifyDataSetChanged()
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
