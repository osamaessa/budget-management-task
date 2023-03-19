package com.osama.budgetmanagement.presentation.accounts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.databinding.ItemAccountBinding
import com.osama.budgetmanagement.presentation.accounts.AccountsViewModel

class AccountsAdapter(
    private val list: ArrayList<Account> = arrayListOf(),
    private val viewModel: AccountsViewModel,
) : RecyclerView.Adapter<AccountViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(list[position], viewModel)
    }

    fun addAll(arrayList: ArrayList<Account>) {
        list.clear()
        list.addAll(arrayList)
        val callBack = AccountsDiffCallBack(list, arrayList)
        val diffResult = DiffUtil.calculateDiff(callBack)
        diffResult.dispatchUpdatesTo(this)
    }

    class AccountsDiffCallBack(
        private val oldList: ArrayList<Account>,
        private val newList: ArrayList<Account>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[oldItemPosition].id &&
                    oldList[oldItemPosition].dinnar == newList[oldItemPosition].dinnar &&
                    oldList[oldItemPosition].dollar == newList[oldItemPosition].dollar
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[oldItemPosition]
        }
    }
}