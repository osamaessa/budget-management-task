package com.osama.budgetmanagement.core.di

import com.osama.budgetmanagement.data.dao.AccountsDao
import com.osama.budgetmanagement.data.dao.TransactionsDao
import com.osama.budgetmanagement.data.repositories.AccountsRepositoryImpl
import com.osama.budgetmanagement.data.repositories.TransactionsRepositoryImpl
import com.osama.budgetmanagement.domain.repositories.AccountsRepository
import com.osama.budgetmanagement.domain.repositories.TransactionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideAccountRepository(accountsDao: AccountsDao): AccountsRepository =
        AccountsRepositoryImpl(accountsDao)

    @Provides
    fun provideTransactionRepository(transactionsDao: TransactionsDao): TransactionsRepository =
        TransactionsRepositoryImpl(transactionsDao)

}