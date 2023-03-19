package com.osama.budgetmanagement.domain.repositories

import com.osama.budgetmanagement.data.models.Transaction
import com.osama.budgetmanagement.domain.common.RequestState
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getAllTransactions(accountId: Int): Flow<RequestState<List<Transaction>>>
    fun getSumInDollar(accountId: Int): Flow<RequestState<Double>>
    fun getSumInDinnar(accountId: Int): Flow<RequestState<Double>>
    fun insert(transaction: Transaction): Flow<RequestState<Boolean>>
    fun delete(transaction: Transaction): Flow<RequestState<Boolean>>
    fun update(transaction: Transaction): Flow<RequestState<Boolean>>
}