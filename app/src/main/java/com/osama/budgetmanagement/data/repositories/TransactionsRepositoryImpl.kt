package com.osama.budgetmanagement.data.repositories

import com.osama.budgetmanagement.data.dao.TransactionsDao
import com.osama.budgetmanagement.data.models.Transaction
import com.osama.budgetmanagement.domain.common.RequestState
import com.osama.budgetmanagement.domain.repositories.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val transactionsDao: TransactionsDao
) : TransactionsRepository {
    override fun getAllTransactions(accountId: Int) = flow {
        runCatching {
            val data = transactionsDao.getAllTransactions(accountId)
            emit(RequestState.Success(data))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }

    override fun getSumInDollar(accountId: Int) = flow {
        runCatching {

            val income = transactionsDao.getIncomeDollarsSum(accountId)
            val spend = transactionsDao.getSpendDollarsSum(accountId)

            val data = income - spend
            emit(RequestState.Success(data))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }

    override fun getSumInDinnar(accountId: Int) = flow {
        runCatching {

            val income = transactionsDao.getIncomeDinnarsSum(accountId)
            val spend = transactionsDao.getSpendDinnarsSum(accountId)

            val data = income - spend
            emit(RequestState.Success(data))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }

    override fun insert(transaction: Transaction) = flow {
        runCatching {
            transactionsDao.insert(transaction)
            emit(RequestState.Success(true))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }

    override fun delete(transaction: Transaction) = flow {
        runCatching {
            transactionsDao.delete(transaction)
            emit(RequestState.Success(true))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }

    override fun update(transaction: Transaction) = flow {
        runCatching {
            transactionsDao.update(transaction)
            emit(RequestState.Success(true))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }
}