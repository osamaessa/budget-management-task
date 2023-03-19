package com.osama.budgetmanagement.presentation.addaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.domain.common.RequestState
import com.osama.budgetmanagement.domain.usecase.account.AddAccountUseCase
import com.osama.budgetmanagement.domain.usecase.account.UpdateAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
) : ViewModel() {

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event

    val isUpdate = MutableStateFlow(false)

    var account: Account? = null
    private val _addResultFlow = MutableSharedFlow<RequestState<Boolean>>()
    val addResultFlow: SharedFlow<RequestState<Boolean>> = _addResultFlow

    val name = MutableStateFlow("")

    fun addAccount() {
        viewModelScope.launch {
            if (name.value.isNullOrEmpty()) {
                _event.emit(Event.NameEmpty)
            } else {
                val account = Account(name = name.value)
                withContext(Dispatchers.IO){
                    addAccountUseCase(account).collect {
                        when(it){
                            is RequestState.Error -> _event.emit(Event.Error)
                            RequestState.Loading -> {}
                            is RequestState.Success -> _event.emit(Event.AccountAddedSuccess)
                        }
                    }
                }
            }
        }
    }

    fun editAccount() {
        viewModelScope.launch {
            if (name.value.isNullOrEmpty()) {
                _event.emit(Event.NameEmpty)
            } else {
                account?.name = name.value
                withContext(Dispatchers.IO){
                    updateAccountUseCase(account!!).collect {
                        when(it){
                            is RequestState.Error -> _event.emit(Event.Error)
                            RequestState.Loading -> {}
                            is RequestState.Success -> _event.emit(Event.AccountAddedSuccess)
                        }
                    }
                }
            }
        }
    }

    fun setUpdate(isUpdate: Boolean){
        this@AddAccountViewModel.isUpdate.value = isUpdate
    }

    sealed class Event {
        object NameEmpty : Event()
        object AccountAddedSuccess : Event()
        object Error : Event()
    }
}