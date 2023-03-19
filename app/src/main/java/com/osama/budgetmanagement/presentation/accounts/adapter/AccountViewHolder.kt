package com.osama.budgetmanagement.presentation.accounts.adapter

import androidx.recyclerview.widget.RecyclerView
import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.databinding.ItemAccountBinding
import com.osama.budgetmanagement.presentation.accounts.AccountsViewModel

class AccountViewHolder(private val binding: ItemAccountBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(account: Account, viewModel: AccountsViewModel){
        binding.account = account
        binding.viewModel = viewModel
    }
}