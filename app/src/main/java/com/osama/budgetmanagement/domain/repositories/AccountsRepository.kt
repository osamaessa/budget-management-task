package com.osama.budgetmanagement.domain.repositories

import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.domain.common.RequestState
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {

    fun getAllAccounts(): Flow<RequestState<List<Account>>>
    fun insert(account: Account): Flow<RequestState<Boolean>>
    fun delete(account: Account): Flow<RequestState<Boolean>>
    fun update(account: Account): Flow<RequestState<Boolean>>
}