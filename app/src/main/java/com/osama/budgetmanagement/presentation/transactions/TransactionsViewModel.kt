package com.osama.budgetmanagement.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.data.models.Transaction
import com.osama.budgetmanagement.domain.common.RequestState
import com.osama.budgetmanagement.domain.usecase.transaction.DeleteTransactionUseCase
import com.osama.budgetmanagement.domain.usecase.transaction.GetAllTransactionsUseCase
import com.osama.budgetmanagement.presentation.accounts.AccountsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
): ViewModel() {

    private val _list = MutableStateFlow<List<Transaction>>(listOf())
    val list: StateFlow<List<Transaction>> = _list

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event

    var accountId = 0

    fun getData(accountId: Int) {
        this.accountId = accountId
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getAllTransactionsUseCase(accountId).collect {
                    when (it) {
                        is RequestState.Error -> {
                            _event.emit(Event.Error)
                        }
                        RequestState.Loading -> {
                            _event.emit(Event.Loading)
                        }
                        is RequestState.Success -> {
                            _list.emit(it.data)
                        }
                    }
                }
            }
        }
    }

    fun deleteAccount(transaction: Transaction) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteTransactionUseCase(transaction).collect {
                    when (it) {
                        is RequestState.Error -> _event.emit(Event.Error)
                        RequestState.Loading -> {}
                        is RequestState.Success -> {
                            _event.emit(Event.DeletedTransaction)
                            getData(accountId = accountId)
                        }
                    }
                }
            }
        }
    }

    sealed class Event {
        object Loading : Event()
        object Error : Event()
        object DeletedTransaction : Event()
    }
}