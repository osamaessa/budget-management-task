package com.osama.budgetmanagement.data.dao

import androidx.room.*
import com.osama.budgetmanagement.data.models.Account

@Dao
interface AccountsDao {

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): List<Account>

    @Insert
    fun insert(account: Account)

    @Delete
    fun delete(account: Account)

    @Update
    fun update(account: Account)
}