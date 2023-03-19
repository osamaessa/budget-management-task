package com.osama.budgetmanagement.presentation.transactions

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.osama.budgetmanagement.R
import com.osama.budgetmanagement.core.utils.observe
import com.osama.budgetmanagement.data.models.Transaction
import com.osama.budgetmanagement.databinding.FragmentTransactionsBinding
import com.osama.budgetmanagement.presentation.addtransaction.AddTransactionFragment
import com.osama.budgetmanagement.presentation.base.BaseFragment
import com.osama.budgetmanagement.presentation.transactions.adapter.TransactionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsFragment :
    BaseFragment<FragmentTransactionsBinding>(R.layout.fragment_transactions) {

    private val args by navArgs<TransactionsFragmentArgs>()
    private val viewModel by viewModels<TransactionsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.list.observe(this, ::observeData)
        viewModel.event.observe(this, ::observeEvent)

        setFragmentResultListener(AddTransactionFragment.ADD_TRANSACTION_KEY) { _, bundle ->
            if (bundle.getBoolean(AddTransactionFragment.MUST_UPDATE)) {
                viewModel.getData(accountId = args.accountId)
                setFragmentResult(TRANSACTIONS_KEY, bundleOf().apply {
                    putBoolean(AddTransactionFragment.MUST_UPDATE, true)
                })
            }
        }

        viewModel.getData(accountId = args.accountId)
    }

    private fun observeEvent(event: TransactionsViewModel.Event) {
        when (event) {
            TransactionsViewModel.Event.DeletedTransaction -> {
                setFragmentResult(TRANSACTIONS_KEY, bundleOf().apply {
                    putBoolean(AddTransactionFragment.MUST_UPDATE, true)
                })
            }
            TransactionsViewModel.Event.Error -> showToast(getString(R.string.general_error))
            TransactionsViewModel.Event.Loading -> {}
        }
    }

    private fun observeData(transactions: List<Transaction>) {
        val arrayList = arrayListOf<Transaction>()
        arrayList.addAll(transactions)
        val adapter = TransactionsAdapter(viewModel = viewModel)
        binding.recyclerViewTransactions.adapter = adapter
        adapter.addAll(arrayList)
        binding.recyclerViewTransactions.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun withBack() = true

    override fun toolbar() = binding.toolbar

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.transactions_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem) {
        if (menuItem.itemId == R.id.menu_add) {
            findNavController().navigate(
                TransactionsFragmentDirections.actionTransactionsFragmentToAddTransactionFragment(
                    args.accountId
                )
            )
        }
    }


    companion object {
        const val TRANSACTIONS_KEY = "TransactionsKey"
        const val MUST_UPDATE = "mustUpdate"
    }
}