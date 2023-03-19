package com.osama.budgetmanagement.presentation.transactions.adapter

import androidx.recyclerview.widget.RecyclerView
import com.osama.budgetmanagement.data.models.Transaction
import com.osama.budgetmanagement.databinding.ItemTransactionBinding
import com.osama.budgetmanagement.presentation.transactions.TransactionsViewModel

class TransactionViewHolder(private val binding: ItemTransactionBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Transaction, viewModel: TransactionsViewModel){
        binding.transaction = item
        binding.viewModel = viewModel
    }
}