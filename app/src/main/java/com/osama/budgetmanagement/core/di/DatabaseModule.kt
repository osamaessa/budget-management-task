package com.osama.budgetmanagement.core.di

import android.content.Context
import androidx.room.Room
import com.osama.budgetmanagement.core.db.AppDatabase
import com.osama.budgetmanagement.data.dao.AccountsDao
import com.osama.budgetmanagement.data.dao.TransactionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "budgetmanagement.db"
    ).build()

    @Provides
    fun provideAccountsDao(appDatabase: AppDatabase): AccountsDao {
        return appDatabase.accountsDao()
    }
    @Provides
    fun provideTransactionsDao(appDatabase: AppDatabase): TransactionsDao {
        return appDatabase.transactionsDao()
    }

}