package com.osama.budgetmanagement.presentation.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.domain.common.RequestState
import com.osama.budgetmanagement.domain.usecase.account.DeleteAccountUseCase
import com.osama.budgetmanagement.domain.usecase.account.GetAllAccountsUseCase
import com.osama.budgetmanagement.domain.usecase.transaction.TransactionsSumInDinnarUseCase
import com.osama.budgetmanagement.domain.usecase.transaction.TransactionsSumInDollarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val transactionsSumInDollarUseCase: TransactionsSumInDollarUseCase,
    private val transactionsSumInDinnarUseCase: TransactionsSumInDinnarUseCase
) : ViewModel() {

    private val _list = MutableStateFlow<List<Account>>(listOf())
    val list: StateFlow<List<Account>> = _list

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getAllAccountsUseCase().collect {
                    when (it) {
                        is RequestState.Error -> {
                            _event.emit(Event.Error)
                        }
                        RequestState.Loading -> {
                            _event.emit(Event.Loading)
                        }
                        is RequestState.Success -> {
                            runBlocking {
                                it.data.map { account ->
                                    getAmountsValues(account)
                                }
                            }
                            _list.emit(it.data)
                        }
                    }
                }
            }
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteAccountUseCase(account).collect {
                    when (it) {
                        is RequestState.Error -> _event.emit(Event.Error)
                        RequestState.Loading -> {}
                        is RequestState.Success -> getData()
                    }
                }
            }
        }
    }

    fun editAccount(account: Account) {
        viewModelScope.launch {
            _event.emit(Event.EditAccount(account))
        }

    }

    fun onItemClick(account: Account) {
        viewModelScope.launch {
            _event.emit(Event.OnItemClick(account))
        }

    }

    private fun getAmountsValues(account: Account) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                transactionsSumInDollarUseCase(accountId = account.id ?: 0).collect {
                    when (it) {
                        is RequestState.Error -> _event.emit(Event.Error)
                        RequestState.Loading -> {}
                        is RequestState.Success -> {
                            account.dollar = it.data
                        }
                    }
                }
                transactionsSumInDinnarUseCase(accountId = account.id ?: 0).collect {
                    when (it) {
                        is RequestState.Error -> _event.emit(Event.Error)
                        RequestState.Loading -> {}
                        is RequestState.Success -> {
                            account.dinnar = it.data
                        }
                    }
                }
            }
        }
    }

    sealed class Event {
        object Loading : Event()
        object Error : Event()
        class EditAccount(val account: Account) : Event()
        class OnItemClick(val account: Account) : Event()
    }
}