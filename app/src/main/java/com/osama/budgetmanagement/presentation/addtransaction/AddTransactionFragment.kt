package com.osama.budgetmanagement.presentation.addtransaction

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
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.osama.budgetmanagement.R
import com.osama.budgetmanagement.core.utils.observe
import com.osama.budgetmanagement.data.models.TransactionCurrency
import com.osama.budgetmanagement.data.models.TransactionType
import com.osama.budgetmanagement.databinding.FragmentAddTransactionBinding
import com.osama.budgetmanagement.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddTransactionFragment :
    BaseFragment<FragmentAddTransactionBinding>(R.layout.fragment_add_transaction) {

    private val viewModel by viewModels<AddTransactionViewModel>()
    private val args by navArgs<AddTransactionFragmentArgs>()
    private var datePicker: MaterialDatePicker<Long>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.transaction = args.transaction
        viewModel.accountId = args.accountId
        viewModel.event.observe(this, ::observeEvent)

        binding.toggleCurrency.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (group.id == R.id.toggleCurrency) {
                when (checkedId) {
                    R.id.buttonDollar -> {
                        if (isChecked) {
                            viewModel.currency = TransactionCurrency.Dollar
                        } else {
                            viewModel.currency = null
                        }
                    }
                    R.id.buttonDinnar -> {
                        if (isChecked) {
                            viewModel.currency = TransactionCurrency.Dinnar
                        } else {
                            viewModel.currency = null
                        }
                    }
                }
            }
        }
        binding.toggleType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (group.id == R.id.toggleType) {
                when (checkedId) {
                    R.id.buttonIncome -> {
                        if (isChecked) {
                            viewModel.type = TransactionType.Income
                        } else {
                            viewModel.type = null
                        }
                    }
                    R.id.buttonSpend -> {
                        if (isChecked) {
                            viewModel.type = TransactionType.Spend
                        } else {
                            viewModel.type = null
                        }
                    }
                }
            }
        }
    }

    private fun observeEvent(event: AddTransactionViewModel.Event) {
        when (event) {
            AddTransactionViewModel.Event.AmountEmpty -> showToast(getString(R.string.transaction_amount_validation_message))
            AddTransactionViewModel.Event.DescriptionEmpty -> showToast(getString(R.string.transaction_description_validation_message))
            AddTransactionViewModel.Event.Error -> showToast(getString(R.string.general_error))
            AddTransactionViewModel.Event.OpenDatePicker -> showDatePicker()
            AddTransactionViewModel.Event.TransactionAddedSuccess -> {
                showToast(getString(R.string.transaction_saved_success))
                setFragmentResult(ADD_TRANSACTION_KEY, bundleOf().apply {
                    putBoolean(MUST_UPDATE, true)
                })
                findNavController().popBackStack()
            }
            AddTransactionViewModel.Event.CurrencyEmpty -> showToast(getString(R.string.transaction_currency_validation_message))
            AddTransactionViewModel.Event.DateEmpty -> showToast(getString(R.string.transaction_date_validation_message))
            AddTransactionViewModel.Event.TypeEmpty -> showToast(getString(R.string.transaction_type_validation_message))
        }
    }

    private fun showDatePicker() {
        datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
                .setSelection(Calendar.getInstance().timeInMillis)
                .setCalendarConstraints(
                    CalendarConstraints.Builder().setEnd(Calendar.getInstance().timeInMillis)
                        .build()
                )
                .build()


        datePicker?.addOnPositiveButtonClickListener { timeInMillis ->
            val date = Date(timeInMillis)
            viewModel.date = Calendar.getInstance()
            viewModel.date?.time = date
            binding.textViewDate.text =
                SimpleDateFormat("yyyy, MMM dd", Locale.getDefault()).format(date)
        }
        datePicker?.show(childFragmentManager, "datePicker")
    }

    override fun withBack() = true

    override fun toolbar() = binding.toolbar

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.add_transaction_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem) {
        if (menuItem.itemId == R.id.menu_save) {
            viewModel.addTransaction()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        datePicker?.clearOnPositiveButtonClickListeners()
    }

    companion object {
        const val ADD_TRANSACTION_KEY = "addTransactionKey"
        const val MUST_UPDATE = "mustUpdate"
    }
}