package com.osama.budgetmanagement.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.osama.budgetmanagement.data.dao.AccountsDao
import com.osama.budgetmanagement.data.dao.TransactionsDao
import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.data.models.Transaction

@Database(entities = [Account::class, Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountsDao(): AccountsDao
    abstract fun transactionsDao(): TransactionsDao
}