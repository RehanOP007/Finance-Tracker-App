package com.example.financetracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetracker.databinding.FragmentTransactionsBinding
import com.example.financetracker.ui.adapters.TransactionAdapter
import com.example.financetracker.ui.viewmodel.FinanceViewModel
import kotlinx.coroutines.launch

class TransactionsFragment : Fragment(){
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinanceViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddTransaction.setOnClickListener {
            AddTransactionDialog(requireContext()) { newTransaction ->
                viewModel.addTransaction(newTransaction)
            }.show()
        }

        transactionAdapter = TransactionAdapter { transactionToDelete ->
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete Transaction")
                .setMessage("Are you sure you want to delete this transaction?")
                .setPositiveButton("Delete") { _, _ ->
                    viewModel.deleteTransaction(transactionToDelete)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = transactionAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
                transactionAdapter.submitList(transactions)
            }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}