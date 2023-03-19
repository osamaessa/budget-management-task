package com.osama.budgetmanagement.data.repositories

import com.osama.budgetmanagement.data.dao.AccountsDao
import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.domain.common.RequestState
import com.osama.budgetmanagement.domain.repositories.AccountsRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val accountsDao: AccountsDao
) : AccountsRepository {

    override fun getAllAccounts() = flow {
        runCatching {
            val data = accountsDao.getAllAccounts()
            emit(RequestState.Success(data))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }

    override fun insert(account: Account) = flow {
        runCatching {
            accountsDao.insert(account)
            emit(RequestState.Success(true))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }

    override fun delete(account: Account) = flow {
        runCatching {
            accountsDao.delete(account)
            emit(RequestState.Success(true))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }

    override fun update(account: Account) = flow {
        runCatching {
            accountsDao.update(account)
            emit(RequestState.Success(true))
        }.onFailure {
            emit(RequestState.Error(it))
        }
    }.onStart {
        emit(RequestState.Loading)
    }
}