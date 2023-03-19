package com.osama.budgetmanagement.presentation.transactions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.osama.budgetmanagement.data.models.Transaction
import com.osama.budgetmanagement.databinding.ItemTransactionBinding
import com.osama.budgetmanagement.presentation.transactions.TransactionsViewModel

class TransactionsAdapter(
    private val list: ArrayList<Transaction> = arrayListOf(),
    private val viewModel: TransactionsViewModel,
) : RecyclerView.Adapter<TransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(list[position], viewModel)
    }

    fun addAll(arrayList: ArrayList<Transaction>) {
        list.clear()
        list.addAll(arrayList)
        val callBack = AccountsDiffCallBack(list, arrayList)
        val diffResult = DiffUtil.calculateDiff(callBack)
        diffResult.dispatchUpdatesTo(this)
    }

    class AccountsDiffCallBack(
        private val oldList: ArrayList<Transaction>,
        private val newList: ArrayList<Transaction>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[oldItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[oldItemPosition]
        }
    }

}