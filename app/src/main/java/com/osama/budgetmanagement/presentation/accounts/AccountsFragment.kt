package com.osama.budgetmanagement.presentation.accounts

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.osama.budgetmanagement.R
import com.osama.budgetmanagement.core.utils.observe
import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.databinding.FragmentAccountsBinding
import com.osama.budgetmanagement.presentation.accounts.adapter.AccountsAdapter
import com.osama.budgetmanagement.presentation.addaccount.AddAccountFragment
import com.osama.budgetmanagement.presentation.base.BaseFragment
import com.osama.budgetmanagement.presentation.transactions.TransactionsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountsFragment : BaseFragment<FragmentAccountsBinding>(R.layout.fragment_accounts) {

    val viewModel by viewModels<AccountsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.event.observe(this, ::observeEvent)
        viewModel.list.observe(this, ::observeData)

        setFragmentResultListener(AddAccountFragment.ADD_ACCOUNT_KEY) { _, bundle ->
            if (bundle.getBoolean(AddAccountFragment.MUST_UPDATE)) {
                viewModel.getData()
            }
        }
        setFragmentResultListener(TransactionsFragment.TRANSACTIONS_KEY) { _, bundle ->
            if (bundle.getBoolean(TransactionsFragment.MUST_UPDATE)) {
                viewModel.getData()
            }
        }
    }

    private fun observeData(accounts: List<Account>) {
        val arrayList = arrayListOf<Account>()
        arrayList.addAll(accounts)
        val adapter = AccountsAdapter(viewModel = viewModel)
        binding.recyclerViewAccounts.adapter = adapter
        adapter.addAll(arrayList)
        binding.recyclerViewAccounts.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun observeEvent(event: AccountsViewModel.Event) {
        when (event) {
            AccountsViewModel.Event.Error -> showToast(getString(R.string.general_error))
            AccountsViewModel.Event.Loading -> {}
            is AccountsViewModel.Event.EditAccount -> findNavController().navigate(
                AccountsFragmentDirections.actionAccountsFragmentToAddAccountFragment(event.account)
            )
            is AccountsViewModel.Event.OnItemClick -> findNavController().navigate(
                AccountsFragmentDirections.actionAccountsFragmentToTransactionsFragment(event.account.id ?: 0)
            )
        }
    }

    override fun withBack() = false

    override fun toolbar() = binding.toolbar

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.accounts_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_add -> {
                findNavController().navigate(
                    AccountsFragmentDirections.actionAccountsFragmentToAddAccountFragment()
                )
            }
            R.id.menu_export -> {}
        }
    }
}