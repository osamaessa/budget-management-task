package com.osama.budgetmanagement.presentation.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.data.models.Transaction
import com.osama.budgetmanagement.data.models.TransactionCurrency
import com.osama.budgetmanagement.data.models.TransactionType
import com.osama.budgetmanagement.domain.common.RequestState
import com.osama.budgetmanagement.domain.usecase.transaction.AddTransactionUseCase
import com.osama.budgetmanagement.presentation.addaccount.AddAccountViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
) : ViewModel() {

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event

    val isUpdate = MutableStateFlow(false)
    var transaction: Transaction? = null
    var accountId: Int? = null

    val description = MutableStateFlow("")
    val amount = MutableStateFlow("")
    var date: Calendar? = null
    var type: TransactionType? = null
    var currency: TransactionCurrency? = null

    fun addTransaction() {
        viewModelScope.launch {
            if (description.value.isNullOrEmpty()) {
                _event.emit(Event.DescriptionEmpty)
            } else if (amount.value.isNullOrEmpty()) {
                _event.emit(Event.AmountEmpty)
            } else if (date == null) {
                _event.emit(Event.DateEmpty)
            } else if (type == null) {
                _event.emit(Event.TypeEmpty)
            } else if (currency == null) {
                _event.emit(Event.CurrencyEmpty)
            } else {
                val transaction = Transaction(
                    date = date?.timeInMillis,
                    type = type?.value,
                    description = description.value,
                    dollars = if (currency == TransactionCurrency.Dollar) amount.value.toDouble() else 0.0,
                    dinnars = if (currency == TransactionCurrency.Dinnar) amount.value.toDouble() else 0.0,
                    accountId = accountId
                )
                withContext(Dispatchers.IO) {
                    addTransactionUseCase(transaction).collect {
                        when (it) {
                            is RequestState.Error -> _event.emit(Event.Error)
                            RequestState.Loading -> {}
                            is RequestState.Success -> _event.emit(Event.TransactionAddedSuccess)
                        }
                    }
                }
            }
        }
    }

    fun openDatePicker() {
        viewModelScope.launch {
            _event.emit(Event.OpenDatePicker)
        }
    }

    sealed class Event {
        object DescriptionEmpty : Event()
        object AmountEmpty : Event()
        object TypeEmpty : Event()
        object CurrencyEmpty : Event()
        object DateEmpty : Event()
        object OpenDatePicker : Event()
        object TransactionAddedSuccess : Event()
        object Error : Event()
    }
}