package com.osama.budgetmanagement.presentation.addaccount

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.osama.budgetmanagement.R
import com.osama.budgetmanagement.core.utils.observe
import com.osama.budgetmanagement.databinding.FragmentAddAccountBinding
import com.osama.budgetmanagement.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAccountFragment : BaseFragment<FragmentAddAccountBinding>(R.layout.fragment_add_account) {

    private val viewModel by viewModels<AddAccountViewModel>()
    private val args by navArgs<AddAccountFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setUpdate(args.account != null)
        viewModel.account = args.account

        binding.viewModel = viewModel
        viewModel.name.value = args.account?.name ?: ""
        viewModel.event.observe(this, ::observeEvent)
    }

    private fun observeEvent(event: AddAccountViewModel.Event) {
        when (event) {
            AddAccountViewModel.Event.AccountAddedSuccess -> {
                showToast(getString(R.string.account_saved_success))
                setFragmentResult(ADD_ACCOUNT_KEY, bundleOf().apply {
                    putBoolean(MUST_UPDATE,true)
                })
                findNavController().popBackStack()
            }
            AddAccountViewModel.Event.Error -> showToast(getString(R.string.general_error))
            AddAccountViewModel.Event.NameEmpty -> showToast(getString(R.string.account_name_validation_message))
        }
    }

    override fun withBack() = true

    override fun toolbar() = binding.toolbar

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

    override fun onMenuItemSelected(menuItem: MenuItem) {}

    companion object {
        const val ADD_ACCOUNT_KEY = "addAccountKey"
        const val MUST_UPDATE = "mustUpdate"
    }
}